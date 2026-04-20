import Client.Request
import Server.*
import Server.commandsPackage.SaveCommand
import Server.labWorkClass.LabWorkCollection
import Server.managersPackage.CommandManager
import Server.managersPackage.DataManager
import java.io.*
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.*
import Server.Log.logger
import java.util.logging.*

object ServerMain {
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
        LabWorkCollection.setCollection(DataManager.load())
        val port = 65242
        val selector = Selector.open()
        val serverChannel = ServerSocketChannel.open().apply {
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
                    logger.warning("Ошибка: ${e.message}")
                    SaveCommand().execute(listOf(), ClientState())
                    channel?.close()
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
        val channel = key.channel() as SocketChannel
        val state = key.attachment() as ClientState

        if (state.stage != Stage.READ) return

        val bytesRead = channel.read(state.lengthBuffer)
        if (bytesRead == -1) {
            SaveCommand().execute(listOf(), state)
            channel.close()
            return
        }

        if (!state.lengthBuffer.hasRemaining()) {
            state.lengthBuffer.flip()
            state.objectLength = state.lengthBuffer.int
            state.dataBuffer = ByteBuffer.allocate(state.objectLength!!)
        }

        state.dataBuffer?.let { dataBuffer ->
            while (dataBuffer.hasRemaining()) {
                if (channel.read(dataBuffer) == -1) break
            }

            if (!dataBuffer.hasRemaining()) {
                dataBuffer.flip()
                val bytes = ByteArray(state.objectLength!!)
                dataBuffer.get(bytes)
                val request = ObjectInputStream(ByteArrayInputStream(bytes)).readObject() as Request
                logger.info("Получен объект: $request")
                CommandManager.getCommand(request.str, state)
                state.stage = Stage.WRITE_RESULT
                key.interestOps(SelectionKey.OP_WRITE)
            }
        }
    }

    private fun handleWrite(key: SelectionKey) {
        val channel = key.channel() as SocketChannel
        val state = key.attachment() as ClientState

        if (state.stage != Stage.WRITE_RESULT && state.stage != Stage.READ) return

        val responseText = if (state.stage == Stage.WRITE_RESULT) ServerOutput.get() else ""
        val response = Response(responseText)

        val responseBytes = ByteArrayOutputStream().use { bos ->
            ObjectOutputStream(bos).use { oos ->
                oos.writeObject(response)
                oos.flush()
                bos.toByteArray()
            }
        }

        val buffer = ByteBuffer.wrap(responseBytes)
        channel.write(buffer)

        state.stage = Stage.READ
        key.interestOps(SelectionKey.OP_READ)
        state.lengthBuffer.clear()
        state.dataBuffer = null
        logger.info("Отправлен ответ: $response")
    }
}
