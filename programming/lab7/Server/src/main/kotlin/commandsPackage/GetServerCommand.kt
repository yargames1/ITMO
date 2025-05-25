package commandsPackage

import ServerOutput


class GetServerCommand: SpecialCommand{

    override fun execute() {
        val cmds = CommandRegistry.serverCommands.joinToString(separator = "|")
        ServerOutput.send(cmds)
    }

}