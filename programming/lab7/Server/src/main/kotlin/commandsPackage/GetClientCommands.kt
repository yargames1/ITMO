package commandsPackage

import ServerOutput


class GetClientCommands: SpecialCommand{

    override fun execute() {
        val cmds = CommandRegistry.clientCommands.joinToString(separator = "|")
        ServerOutput.send(cmds)
    }

}