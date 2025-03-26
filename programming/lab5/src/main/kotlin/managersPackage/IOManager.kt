package managersPackage

import labWorkClass.LabWorkCollection
/**
 * Объект для управления ввода/вывода программы.
 */
object IOManager {
    var text = mutableListOf<String>()
    /**
     * Отправляет текст в консоль.
     */
    fun send(str: String){
        println(str)
    }
    /**
     * Отправляет данные программе, симулирует ввод пользователя при чтении из файла.
     *
     * @return строка с информацией или команда.
     */
    fun read(): String{
        if (text.isEmpty()) {
            LabWorkCollection.deleteFiles()
            return readln()
        }
        else{
            newString()
            println(text.first())
            if (text.size == 1){FlagManager.stopAuto()}
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
    fun addText(list: MutableList<String>) {
        text.addAll(0, list)
    }

}
