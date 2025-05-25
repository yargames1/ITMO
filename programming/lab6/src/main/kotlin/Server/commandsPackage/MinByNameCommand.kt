package Server.commandsPackage

import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import Server.ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду вывода объекта с минимальным значением поля name
 */
class MinByNameCommand: Command {

    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param state Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()) {
            // Получаем коллекцию и сортируем по полю name, затем выбираем первый элемент
            val minElement = LabWorkCollection.getCollection()
                .values
                .stream()
                .min(compareBy { it.getName() })  // Сортируем по полю name, выбираем минимальный

            if (minElement.isPresent) {
                ServerOutput.send(minElement.get().toString())  // Отправляем минимальный элемент
            } else {
                ServerOutput.send("Коллекция пуста")
            }
        } else {
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
        return "min_by_name - выводит любой объект из коллекции, значение поля name которого является минимальным"
    }
}
