package commandsPackage

/**
 * Интерфейс для создания команд.
 */
interface Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    fun execute(tokens: List<String>)
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    fun describe(): String
}
