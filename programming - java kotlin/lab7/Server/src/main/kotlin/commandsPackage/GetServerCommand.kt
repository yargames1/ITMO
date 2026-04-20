package commandsPackage

import ServerOutput


class GetServerCommand: SpecialCommand{

    override fun execute(tokens: List<String>) {
        val cmds = CommandRegistry.serverCommands.joinToString(separator = "|")
        ServerOutput.send(cmds)
    }

}