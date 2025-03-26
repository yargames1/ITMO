package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.IOManager

/**
 * Класс, реализующий команду удаления элемента с определенным значением поля personalQualitiesMinimum
 */
class RemoveAnyByPersonalQualitiesMinimumCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>) {
        if (tokens.size == 1){
            val key = LabWorkCollection.getKeyByPQM(tokens[0].toLong())
            if (key!="") {
                LabWorkCollection.removeFromCollection(key)
                IOManager.send("Элемент успешно удален")
            }
            else{
                IOManager.send("Элемента с таким значением personalQualitiesMinimum не существует")
            }
        }
        else {IOManager.send("Введите команду верно - remove_any_by_personal_qualities_minimum personalQualitiesMinimum, где " +
                "personalQualitiesMinimum - значение поля personalQualitiesMinimum")}
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "remove_any_by_personal_qualities_minimum personalQualitiesMinimum - удаляет из коллекции один элемент, " +
                "значение поля personalQualitiesMinimum которого эквивалентно заданному"
    }
}