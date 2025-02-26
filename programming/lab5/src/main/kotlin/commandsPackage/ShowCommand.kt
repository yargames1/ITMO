package commandsPackage

import labWorkClass.LabWorkCollection
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
    override fun execute(tokens: List<String>, auto: Boolean) {
        if (tokens.isEmpty()){
            for (key in LabWorkCollection.collection.keys){
                println("$key - ${LabWorkCollection.collection[key]}")
            }
        }
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