package commandsPackage

import managersPackage.FlagManager
import managersPackage.IOManager

/**
 * Класс, реализующий команду выхода из программы.
 */
class ExitCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>) {
        IOManager.send("Выполняется выход из программы")
       FlagManager.turnOff()
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "exit - выход из программы (сохранение не происходит)"
    }
}