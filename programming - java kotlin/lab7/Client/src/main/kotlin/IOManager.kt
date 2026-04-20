/**
 * Объект для управления ввода/вывода программы.
 */
object IOManager {
    var text = mutableListOf<String>()
    var opendFiles = mutableListOf("")
    /**
     * Отправляет текст в консоль.
     */
    fun send(str: String){
        if (str != "") {println(str)}
    }
    /**
     * Отправляет данные программе, симулирует ввод пользователя при чтении из файла.
     *
     * @return строка с информацией или команда.
     */
    fun read(): String?{
        if (text.isEmpty()) {
            opendFiles.clear()
            return readlnOrNull()?.trim()
        }
        else{
            println(text.first())
            return text.removeFirst()
        }
    }
    /**
     * Создает приглашение к вводу.
     */
    fun newString(){
        print(">>> ")
    }
    /**
     * Добавляет текст из файла, для симуляции ввода.
     */
    fun addText(list: List<String>) {
        text.addAll(0, list)
    }

}
