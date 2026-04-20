package Server.commandsPackage

import Server.ClientState
import Server.ServerOutput
import Server.Stage

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
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()) {
            ServerOutput.send("Выполняется выход из программы")
        }
        else {
            ServerOutput.send("Введите команду верно - без дополнительных аргументов")
        }
        state.stage = Stage.WRITE_RESULT
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