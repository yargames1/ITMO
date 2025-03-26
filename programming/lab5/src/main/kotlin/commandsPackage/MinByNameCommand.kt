package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.IOManager

/**
 * Класс, реализующий команду вывода объекта с минимальным значением поля name
 */
class MinByNameCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>) {
        if (tokens.isEmpty()) {
            IOManager.send(LabWorkCollection.getCollection().getValue(LabWorkCollection.getMinByNameKey()).toString())
        }
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "min_by_name - выводит любой объект из коллекции, значение поля name которого является минимальным"
    }
}