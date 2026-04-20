package Server.commandsPackage

import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду удаления элемента по ключу
 */
class RemoveKeyCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.size == 1){
            if (tokens[0] in LabWorkCollection.getCollection().keys) {
                LabWorkCollection.removeFromCollection(tokens[0])
                ServerOutput.send("Элемент успешно удален")
            }
            else{
                ServerOutput.send("Элемента с таким ключем не существует")
            }
        }
        else {
            ServerOutput.send("Введите команду верно - remove_key key, где key - ключ элемента коллекции")
        }
        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "remove_key - удаляет элемент из коллекции по его ключу"
    }
}