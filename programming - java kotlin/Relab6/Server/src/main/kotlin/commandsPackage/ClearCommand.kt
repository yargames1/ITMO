package Server.commandsPackage

import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду очистки коллекции
 */

class ClearCommand: Command {
    /**
     * Выполняет команду без аргументов.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()) {
            LabWorkCollection.clearConnection()
            ServerOutput.send("Коллекция успешно очищена")
        }
        else{
            ServerOutput.send("Команда не должна иметь аргументов")
        }
        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "clear - очищает коллекцию"
    }
}