import java.io.*
import java.net.Socket

object ClientMain {
    @JvmStatic
    fun main(args: Array<String>) {
        // При подключении клиент должен запрашивать пул команд, для которых собирает объекты
        val host = "localhost"
        val port = 1894
        val cmdsNeedsAnObj = mutableListOf<String>()
        val serverCmds = mutableListOf<String>()
        var socket: Socket
        var login: String
        var passwd: String
        // Собираем при первом запросе команды, для которых нужно подтверждение
        outerLoop@ while (true) {
            innerLoop@ while (true) {
                try {
                    socket = Socket(host, port)

                    val getClientCommandsRequest = Request(listOf("get_list_of_client_cmds"))

                    val getClientCommandsResponse = sendAndRecieve(socket, getClientCommandsRequest)

                    if (getClientCommandsResponse != null) {
                        cmdsNeedsAnObj.addAll(getClientCommandsResponse.str.split("|"))
                    }

                    val getServerCommandsRequest = Request(listOf("get_list_of_server_cmds"))

                    val getClientServerResponse = sendAndRecieve(socket, getServerCommandsRequest)

                    if (getClientServerResponse != null) {
                        serverCmds.addAll(getClientServerResponse.str.split("|"))
                    }
                    break@innerLoop
                } catch (e: IOException) {
                    innerLoop2@ while (true) {
                        IOManager.send("Сервер временно недоступен. Хотите переподключиться? [Y/N]")
                        val ans = IOManager.read()
                        if (ans == "Y") {
                            break@innerLoop2
                        } else if (ans == "N") {
                            break@outerLoop
                        } else {
                            continue
                        }
                    }
                }
            }
            // Login
            IOManager.send("Войдите в сеть")
            regloop@ while (true) {
                IOManager.send("Введите логин")
                IOManager.newString()
                login = IOManager.read().toString()
                // Сделать запрос, узнать, есть ли такой логин
                val logExistingRequest = Request(listOf("isTheLoginExist", login))
                val logExistingResponse = sendAndRecieve(socket, logExistingRequest)
                // Должен вернуть true или false
                val rez = logExistingResponse?.str
                when (rez) {
                    "true" -> {
                        IOManager.send("Введите пароль")
                        IOManager.newString()
                        passwd = IOManager.read().toString()
                        // Запрос на подключение
                        val logInRequest = Request(listOf("logIn", login, passwd))
                        val logInResponse = sendAndRecieve(socket, logInRequest)
                        val logRez = logInResponse?.str

                        when (logRez) {
                            "true" -> IOManager.send("Вход выполнен")
                            "false" -> {
                                IOManager.send("Пароль неверный")
                                continue
                            }
                            else -> {
                                IOManager.send("Не удалось получить информацию от базы данных")
                                continue
                            }
                        }
                    }
                    "false" -> {
                        IOManager.send("Такой логин не зарегистрирован. Хотите зарегистрировать? [Y/N]")
                        IOManager.newString()
                        while (true) {
                            val ans = IOManager.read()
                            when (ans) {
                                "Y" -> {
                                    while (true) {
                                        IOManager.send("Введите пароль")
                                        IOManager.newString()
                                        passwd = IOManager.read().toString()
                                        IOManager.send("Подтвердите пароль")
                                        IOManager.newString()
                                        if (passwd == IOManager.read()) {
                                            val regRequest = Request(listOf("reg", login, passwd))
                                            val regResponse = sendAndRecieve(socket, regRequest)
                                            val regRez = regResponse?.str
                                            when (regRez) {
                                                "true" -> {
                                                    IOManager.send("Аккаунт создан, вход в систему выполнен")
                                                    break@regloop
                                                }
                                                else -> {
                                                    IOManager.send("Не удалось получить информацию от базы данных")
                                                    continue@regloop
                                                }
                                            }
                                        } else {
                                            IOManager.send("Пароли не совпадают")
                                            continue
                                        }
                                    }
                                }
                                "N" -> continue@regloop
                                else -> continue
                            }
                        }
                    }
                    else -> {
                        IOManager.send("Не удалось получить информацию от базы данных")
                        continue
                    }
                }

                break
            }
            //
            try {

                while (true) {

                    IOManager.newString()
                    val msg = IOManager.read() ?: continue
                    if (msg.isBlank()) continue

                    // проверка на создание нового элемента, выполнение скрипта или просто команды
                    val request : Request

                    if (cmdsNeedsAnObj.any {msg.contains(it)}){
                        val preparation = ClientCmds.prepare(msg)
                        request = if (preparation != "") {
                            Request(listOf(msg, preparation, login, passwd))
                        } else{
                            Request(listOf(""))
                        }
                    } else if (serverCmds.any {msg.contains(it)}){
                        request = Request(listOf(msg, login, passwd))
                    } else if ("execute_script" in msg){
                        request = Request(listOf(ClientCmds.perpareText(msg), login, passwd))
                    } else{
                        continue
                    }

                    if (request.str != listOf("")) {
                        val response = sendAndRecieve(socket, request)
                        IOManager.send(response?.str?:"")
                        if (response?.str == "Выполняется выход из программы") break
                    }
                }
                socket.close()
                break@outerLoop
            } catch (e: IOException) {
                innerLoop@ while (true) {
                    IOManager.send("Сервер временно недоступен. Хотите переподключиться? [Y/N]")
                    val ans = IOManager.read()
                    if (ans == "Y") {
                        break@innerLoop
                    } else if (ans == "N") {
                        break@outerLoop
                    } else {
                        continue
                    }
                }
            }
        }
    }

    private fun sendAndRecieve(socket: Socket, request: Request): Response? {
        val byteArrayOut = ByteArrayOutputStream()
        val objectOut = ObjectOutputStream(byteArrayOut)
        objectOut.writeObject(request)
        objectOut.flush()

        val data = byteArrayOut.toByteArray()
        val output = DataOutputStream(socket.getOutputStream())
        // Сначала пишем длину
        output.writeInt(data.size)
        // Потом сам объект
        output.write(data)
        // Чтение ответа
        val response = ObjectInputStream(socket.getInputStream()).readObject() as? Response
        return response
    }
}