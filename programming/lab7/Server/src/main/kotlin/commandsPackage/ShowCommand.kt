package commandsPackage

import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage

/**
 * Класс, реализующий команду вывода элементов коллекции в строковом представлении
 */
class ShowCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()){
            for (key in LabWorkCollection.getCollection().keys){
                ServerOutput.send("$key - ${LabWorkCollection.getCollection()[key]}")
            }
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
        return "show - выводит все элементы коллекции в строковом представлении"
    }

}