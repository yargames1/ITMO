package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.IOManager

/**
 * Класс, реализующий команду получения информации о коллекции.
 */
class InfoCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>) {
        if (tokens.isEmpty()) {
            IOManager.send(
                "тип - HashMap, дата инициализации - ${LabWorkCollection.inicializationDate} " + // дописать дату инициализации + еще что-нибудь
                        "количество элементов - ${LabWorkCollection.collection.size}"
            )
        }
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