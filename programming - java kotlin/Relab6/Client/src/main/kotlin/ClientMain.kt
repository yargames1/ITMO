import Client.ClientCmds
import Client.IOManager
import Client.Request
import java.io.*
import java.net.Socket

object ClientMain {
    @JvmStatic
    fun main(args: Array<String>) {
        // При подключении клиент должен запрашивать пул команд, для которых собирает объекты
        val host = "localhost"
        val port = 10121
        val cmdsNeedsAnObj = mutableListOf<String>()
        val serverCmds = mutableListOf<String>()
        var socket: Socket
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
                            Request(listOf(msg, preparation))
                        } else{
                            Request(listOf(""))
                        }
                    } else if (serverCmds.any {msg.contains(it)}){
                        request = Request(listOf(msg))
                    } else if ("execute_script" in msg){
                        request = Request(listOf(ClientCmds.perpareText(msg)))
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