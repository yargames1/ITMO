package managersPackage

import FlagController

object IOManager {
    var text = mutableListOf<String>()
    fun send(str: String){
        println(str)
    }
    fun read(): String{
        if (text.isEmpty()) {
            FlagController.stopAuto()
            return readln()
        }
        else{
            newString()
            println(text.first())
            return text.removeFirst()
        }
    }
    fun newString(){
        print(">>> ")
    }
    fun addText(list: MutableList<String>) {
        text.addAll(0, list)
    }

}
