package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.IOManager

/**
 * Класс, реализующий команду очистки коллекции
 */

class ClearCommand: Command {
    /**
     * Выполняет команду без аргументов.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>) {
        if (tokens.isEmpty()) {
            LabWorkCollection.clearConnection()
            IOManager.send("Коллекция успешно очищена")
        }
        else{
            IOManager.send("Команда не должна иметь аргументов")
        }
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "clear - очищает коллекцию"
    }
}