package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.DataManager
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
    override fun execute(tokens: List<String>) {
        DataManager.save(LabWorkCollection.collection)
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "save - сохраняет коллекцию в файл"
    }
}