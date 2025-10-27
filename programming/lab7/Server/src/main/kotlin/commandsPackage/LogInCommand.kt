package commandsPackage

import managersPackage.DbManager

class LogInCommand: SpecialCommand {
    override fun execute(tokens: List<String>) {
        val rez = DbManager.logIn(tokens)
        if (rez > 0){
            ServerOutput.send("true")
        }
        else if (rez == 0){
            ServerOutput.send("false")
        }
        else{
            ServerOutput.send("lost_connection")
        }
    }
}