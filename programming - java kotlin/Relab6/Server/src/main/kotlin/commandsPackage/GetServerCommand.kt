package commandsPackage

import ServerOutput
import Server.commandsPackage.CommandRegistry


class GetServerCommand: SpecialCommand{

    override fun execute() {
        val cmds = CommandRegistry.serverCommands.joinToString(separator = "|")
        ServerOutput.send(cmds)
    }

}