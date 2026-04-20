import labWorkClass.LabWorkCollection
import managersPackage.CommandManager
import managersPackage.DbManager
import java.io.*
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.*
import java.util.concurrent.ForkJoinPool
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.FileHandler
import java.util.logging.SimpleFormatter


object ServerMain {

    private val logger: Logger = Logger.getLogger("ServerLogger")
    private val forkJoinPool = ForkJoinPool.commonPool()

    init {
        try {
            val handler = FileHandler("server.log", true)
            handler.formatter = SimpleFormatter()
            logger.addHandler(handler)
            logger.level = Level.ALL
        } catch (e: IOException) {
            println("Не удалось инициализировать логгер: ${e.message}")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // Инициализация БД, коллекций и т.п.
        DbManager.connect()
        LabWorkCollection.setCollection(DbManager.load())

        val port = 1894
        val selector = Selector.open()

        ServerSocketChannel.open().apply {
            bind(InetSocketAddress(port))
            configureBlocking(false)
            register(selector, SelectionKey.OP_ACCEPT)
        }

        logger.info("Сервер запущен на порту $port")

        while (true) {
            selector.select()
            val keys = selector.selectedKeys().iterator()

            while (keys.hasNext()) {
                val key = keys.next()
                keys.remove()

                try {
                    when {
                        key.isAcceptable -> handleAccept(key, selector)
                        key.isReadable -> handleRead(key)
                        key.isWritable -> handleWrite(key)
                    }
                } catch (e: Exception) {
                    val channel = key.channel() as? SocketChannel
                    logger.warning("Ошибка в обработке ключа: ${e.message}")
                    try {
                        channel?.close()
                    } catch (_: Exception) {}
                    key.cancel()
                }
            }
        }
    }

    private fun handleAccept(key: SelectionKey, selector: Selector) {
        val serverChannel = key.channel() as ServerSocketChannel
        val clientChannel = serverChannel.accept()
        clientChannel.configureBlocking(false)

        val state = ClientState()
        clientChannel.register(selector, SelectionKey.OP_READ, state)
        state.stage = Stage.READ

        logger.info("Клиент подключился: ${clientChannel.remoteAddress}")
    }

    private fun handleRead(key: SelectionKey) {
        // Ставим interestOps = 0, чтобы не получать повторные события чтения пока не обработаем
        key.interestOps(0)

        Thread {
            val channel = key.channel() as SocketChannel
            val state = key.attachment() as ClientState
            synchronized(state) {
                if (state.stage != Stage.READ) return@Thread
            }

            try {
                // Читаем длину объекта (4 байта)
                synchronized(state) {
                if (state.lengthBuffer.hasRemaining()) {
                    val bytesRead = channel.read(state.lengthBuffer)
                    if (bytesRead == -1) {
                        logger.info("Клиент закрыл соединение во время чтения длины")
                        channel.close()
                        key.cancel()
                        return@Thread
                    }
                    if (state.lengthBuffer.hasRemaining()) {
                        // Ещё не все 4 байта прочитаны — ждем
                        key.interestOps(SelectionKey.OP_READ)
                        key.selector().wakeup()
                        return@Thread
                    }
                    // Длина полностью считана
                    state.lengthBuffer.flip()
                    state.objectLength = state.lengthBuffer.int
                    if (state.objectLength!! <= 0) {
                        logger.warning("Некорректная длина объекта: ${state.objectLength}")
                        channel.close()
                        key.cancel()
                        return@Thread
                    }
                    state.dataBuffer = ByteBuffer.allocate(state.objectLength!!)
                }
                }

                // Читаем тело объекта
                val dataBuffer = state.dataBuffer!!
                while (dataBuffer.hasRemaining()) {
                    val bytesRead = channel.read(dataBuffer)
                    if (bytesRead == -1) {
                        logger.info("Клиент закрыл соединение во время чтения тела")
                        channel.close()
                        key.cancel()
                        return@Thread
                    }
                    if (bytesRead == 0) break // Данных сейчас нет, ждём продолжения
                }

                if (dataBuffer.hasRemaining()) {
                    // Объект еще не полностью прочитан, ждем продолжения
                    key.interestOps(SelectionKey.OP_READ)
                    key.selector().wakeup()
                    return@Thread
                }

                // Объект прочитан полностью
                dataBuffer.flip()
                val bytes = ByteArray(state.objectLength!!)
                dataBuffer.get(bytes)

                // Сбрасываем буферы для следующего сообщения
                synchronized(state)
                    {
                        state.lengthBuffer.clear()
                        state.dataBuffer = null
                        state.objectLength = null
                    }

                val request = ObjectInputStream(ByteArrayInputStream(bytes)).readObject() as Request
                logger.info("Получен запрос: $request")

                // Смена стадии на PROCESSING и запуск обработки в ForkJoinPool
                synchronized(state) {
                    state.stage = Stage.PROCESSING
                }
                forkJoinPool.submit {
                    try {
                        CommandManager.getCommand(request.str, state)
                        // После обработки - переходим к отправке
                        synchronized(state) {
                            state.stage = Stage.WRITE_RESULT
                        }
                        // Подготавливаем ответ
                        val responseText = ServerOutput.get()
                        val response = Response(responseText)
                        logger.info("Сформирован ответ: $response")
                        val responseBytes = ByteArrayOutputStream().use { bos ->
                            ObjectOutputStream(bos).use { oos ->
                                oos.writeObject(response)
                                oos.flush()
                                bos.toByteArray()
                            }
                        }
                        synchronized(state) {
                            state.resultToSend = ByteBuffer.wrap(responseBytes)
                        }
                        // Меняем interestOps на запись
                        key.interestOps(SelectionKey.OP_WRITE)
                        key.selector().wakeup()
                    } catch (e: Exception) {
                        logger.warning("Ошибка обработки команды: ${e.message}")
                        try {
                            channel.close()
                        } catch (_: Exception) {}
                        key.cancel()
                    }
                }

            } catch (e: Exception) {
                logger.warning("Ошибка при чтении: ${e.message}")
                try {
                    channel.close()
                } catch (_: Exception) {}
                key.cancel()
            }
        }.start()
    }

    private fun handleWrite(key: SelectionKey) {
        val channel = key.channel() as SocketChannel
        val state = key.attachment() as ClientState

        // Чтобы избежать множественного submit
        synchronized(state) {
            if (state.stage != Stage.WRITE_RESULT || state.resultToSend == null) return

            // Ставим заглушку — stage временно сбрасываем, чтобы не вызвать повторный submit
            state.stage = Stage.PROCESSING
        }

        forkJoinPool.submit {
            synchronized(state) {
                try {
                    val buffer = state.resultToSend ?: return@submit
                    channel.write(buffer)

                    if (!buffer.hasRemaining()) {
                        // Очищаем буфер и возвращаемся к чтению
                        state.resultToSend = null
                        state.stage = Stage.READ
                        key.interestOps(SelectionKey.OP_READ)
                        key.selector().wakeup()
                        logger.info("Ответ отправлен клиенту")
                    } else {
                        // Ставим обратно WRITE_RESULT и ждем следующего write
                        state.stage = Stage.WRITE_RESULT
                        key.interestOps(SelectionKey.OP_WRITE)
                        key.selector().wakeup()
                    }
                } catch (e: Exception) {
                    logger.warning("Ошибка при отправке: ${e.message}")
                    try {
                        channel.close()
                    } catch (_: Exception) {}
                    key.cancel()
                }
            }
        }
    }

}
