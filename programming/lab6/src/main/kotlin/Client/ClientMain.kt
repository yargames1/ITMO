import Client.ClientCmds
import Client.IOManager
import Client.Request
import java.io.*
import java.net.Socket

object ClientMain {
    @JvmStatic
    fun main(args: Array<String>) {
        val host = "localhost"
        val port = 65242
        while (true) {
            try {
                val socket = Socket(host, port)

                while (true) {
                    val byteArrayOut = ByteArrayOutputStream()
                    val objectOut = ObjectOutputStream(byteArrayOut)

                    IOManager.newString()
                    val msg = IOManager.read() ?: continue
                    if (msg.isBlank()) continue
                    // проверка на создание нового элемента
                    val request : Request
                    if (("insert" in msg)or("replace_if_greater" in msg) or ("replace_if_lower" in msg) or
                        ("update" in msg)){
                        val preparation = ClientCmds.prepare(msg)
                        if (preparation != "") {
                            request = Request(listOf(msg, preparation))
                        }
                        else{
                            request = Request(listOf(""))
                        }
                    }
                    else if ("execute_script" in msg){
                        request = Request(listOf(ClientCmds.perpareText(msg)))
                    }
                    else {
                        request = Request(listOf(msg))
                    }

                    objectOut.writeObject(request)
                    objectOut.flush()

                    val data = byteArrayOut.toByteArray()
                    val output = DataOutputStream(socket.getOutputStream())

                    // Сначала пишем длину
                    output.writeInt(data.size)
                    // Потом сам объект
                    output.write(data)
                    // Чтение ответа
                    val input = ObjectInputStream(socket.getInputStream())
                    val response = input.readObject() as? Response
                    IOManager.send(response?.str?:"")
                    if (msg == "exit") break
                }
                socket.close()
                break
            } catch (e: IOException) {
                IOManager.send("Сервер временно недоступен. Повтор через 3 секунды...")
                Thread.sleep(3000)
            }
        }
    }
}