package commandsPackage

import managersPackage.DbManager

class regCommand: SpecialCommand {
    override fun execute(tokens: List<String>) {
        val rez = DbManager.reg(tokens)
        if (rez > 0){
            ServerOutput.send("true")
        }
        else{
            ServerOutput.send("lost_connection")
        }
    }
}