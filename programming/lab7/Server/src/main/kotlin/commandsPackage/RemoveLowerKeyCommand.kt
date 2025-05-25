package commandsPackage

import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage

class RemoveLowerKeyCommand: Command {
    /**
     * Класс, реализующий команду удаления элементов, ключи которых меньше определенных
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        /**
         * Выполняет команду с переданными аргументами.
         *
         * @param tokens Список, содержащий команду и её аргументы.
         * @param auto Флаг, указывающий, выполняется ли команда автоматически
         */
        if (tokens.size == 1){
            if (tokens[0] in LabWorkCollection.getCollection().keys) {
                val referenceKey = tokens[0]
                val removedKeys = LabWorkCollection.getCollection()
                    .keys
                    .stream()
                    .filter { it < referenceKey }
                    .toList()

                removedKeys.forEach { LabWorkCollection.removeFromCollection(it) }
                ServerOutput.send("Удаление завершено")
            }
            else{
                ServerOutput.send("Элемента с таким ключем не существует")
            }
        }
        else {
            ServerOutput.send("Введите команду верно - remove_lower_key key, где key - ключ элемента коллекции")}
        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "remove_lower_key key - удаляет из коллекции все элементы, ключ которых меньше, чем заданный"
    }
}