package commandsPackage

import ServerOutput
import Server.commandsPackage.CommandRegistry


class GetClientCommands: SpecialCommand{

    override fun execute() {
        val cmds = CommandRegistry.clientCommands.joinToString(separator = "|")
        ServerOutput.send(cmds)
    }

}