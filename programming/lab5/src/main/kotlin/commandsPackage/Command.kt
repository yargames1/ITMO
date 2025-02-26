package commandsPackage

/**
 * Интерфейс для создания команд.
 */
interface Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    fun execute(tokens: List<String>, auto: Boolean = false)
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    fun describe(): String
}
