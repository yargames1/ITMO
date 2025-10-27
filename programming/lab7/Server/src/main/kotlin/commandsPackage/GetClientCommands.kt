package commandsPackage

import ServerOutput


class GetClientCommands: SpecialCommand{

    override fun execute(tokens: List<String>) {
        val cmds = CommandRegistry.clientCommands.joinToString(separator = "|")
        ServerOutput.send(cmds)
    }

}