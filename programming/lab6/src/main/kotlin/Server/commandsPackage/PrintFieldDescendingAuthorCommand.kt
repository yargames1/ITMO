package Server.commandsPackage

import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import Server.ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду вывода значений полей author всех элементов в порядке убывания
 */
class PrintFieldDescendingAuthorCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()) {
            LabWorkCollection.getCollection()
                .values
                .sortedByDescending { it.getAuthor() }
                .forEach { author ->
                    ServerOutput.send(author.toString())
                }
        }
        else {
            ServerOutput.send("Введите команду верно - insert key, где key - ключ для нового элемента коллекции")
        }
        state.stage = Stage.WRITE_RESULT
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