package commandsPackage

import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage

/**
 * Класс, реализующий команду очистки коллекции
 */

class ClearCommand: UpdateCommand {
    /**
     * Выполняет команду без аргументов.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.size == 1) {
            LabWorkCollection.clearConnection(tokens[0])
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