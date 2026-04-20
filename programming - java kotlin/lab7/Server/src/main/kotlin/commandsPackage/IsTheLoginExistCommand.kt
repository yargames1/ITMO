package commandsPackage

import ServerOutput
import managersPackage.DbManager

class IsTheLoginExistCommand: SpecialCommand {
    override fun execute(tokens: List<String>) {
        val rez = DbManager.checkLoginExist(tokens[0])
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