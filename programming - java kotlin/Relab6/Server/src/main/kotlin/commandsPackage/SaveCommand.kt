package Server.commandsPackage

import Server.ClientState
import ServerOutput
import Server.Stage
import Server.labWorkClass.LabWorkCollection
import Server.managersPackage.DataManager
/**
 * Класс, реализующий команду сохранения коллекции в файл
 */
class SaveCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()) {
            DataManager.save(LabWorkCollection.getCollection())
        }
        else {
            ServerOutput.send("Введите команду верно - replace_if_lower key, где key - ключ элемента коллекции")}
        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return ""
    }
}