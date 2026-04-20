package commandsPackage

import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage

/**
 * Класс, реализующий команду получения информации о коллекции.
 */
class InfoCommand: InformationCommand {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()) {
            ServerOutput.send(
                "тип - HashMap, дата инициализации - ${LabWorkCollection.getInicializationDate()} " +
                        "количество элементов - ${LabWorkCollection.getCollection().size}"
            )
        }
        else {
            ServerOutput.send("Введите команду верно - без дополнительных аргументов")
        }
        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "info - выводит информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)"
    }
}