package commandsPackage

/**
 * Интерфейс для создания специальных команд.
 */
interface SpecialCommand {
    fun execute(tokens: List<String> = listOf(""))
}
