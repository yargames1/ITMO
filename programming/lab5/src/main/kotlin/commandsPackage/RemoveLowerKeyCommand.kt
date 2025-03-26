package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.IOManager

class RemoveLowerKeyCommand: Command {
    /**
     * Класс, реализующий команду удаления элементов, ключи которых меньше определенных
     */
    override fun execute(tokens: List<String>) {
        /**
         * Выполняет команду с переданными аргументами.
         *
         * @param tokens Список, содержащий команду и её аргументы.
         * @param auto Флаг, указывающий, выполняется ли команда автоматически
         */
        if (tokens.size == 1){
            if (tokens[0] in LabWorkCollection.getCollection().keys) {
                for (key in LabWorkCollection.getCollection().keys.toList()){
                    if (key < tokens[0]){
                        LabWorkCollection.removeFromCollection(key)
                    }
                }
                IOManager.send("Удаление завершено")
            }
            else{
                IOManager.send("Элемента с таким ключем не существует")
            }
        }
        else {IOManager.send("Введите команду верно - remove_lower_key key, где key - ключ элемента коллекции")}
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