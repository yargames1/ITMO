package commandsPackage

import managersPackage.IOManager

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
    override fun execute(tokens: List<String>) {
        if (tokens.isEmpty()) {
            for (c in CommandRegistry.commands.values) {
                IOManager.send(c.describe())
            }
        }
    }
}