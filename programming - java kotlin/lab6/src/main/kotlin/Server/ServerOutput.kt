package Server


object ServerOutput {
    var text = ""

    fun send(str: String){
        text += str+"\n"
    }
    fun get(): String{
        val txt = text.trimEnd()
        text = ""
        return txt
    }
    fun inform(str: String){
        println(str)
    }
}