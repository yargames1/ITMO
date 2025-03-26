package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.IOManager

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
    override fun execute(tokens: List<String>) {
        if (tokens.size == 1){
            if (tokens[0] in LabWorkCollection.collection.keys) {
                LabWorkCollection.collection.remove(tokens[0])
                IOManager.send("Элемент успешно удален")
            }
            else{
                IOManager.send("Элемента с таким ключем не существует")
            }
        }
        else {
            IOManager.send("Введите команду верно - remove_key key, где key - ключ элемента коллекции")
        }
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