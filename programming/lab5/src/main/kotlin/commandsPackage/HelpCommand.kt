package commandsPackage
/**
 * Класс, реализующий команду помощи.
 */
class HelpCommand: Command {
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "help - выводит справку по доступным командам"
    }
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, auto: Boolean) {
        if (tokens.isEmpty()) {
            for (c in CommandRegistry.commands.values) {
                println(c.describe())
            }
        }
    }
}