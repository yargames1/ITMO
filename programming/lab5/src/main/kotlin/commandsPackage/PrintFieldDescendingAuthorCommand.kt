package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.IOManager

/**
 * Класс, реализующий команду вывода значений полей author всех элементов в порядке убывания
 */
class PrintFieldDescendingAuthorCommand: Command{
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>) {
        LabWorkCollection.getDescendingAuthors().forEach { author ->
            IOManager.send(author.toString())
        }
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "print_field_descending_author - выводит значения поля author всех элементов в порядке убывания"
    }

}