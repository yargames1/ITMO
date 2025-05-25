package Server.commandsPackage

import Server.Generator
import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду вставки нового элемента коллекции
 */
class InsertCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        // Используем Stream API для проверки наличия ключа
        val keyExists = LabWorkCollection.getCollection().keys.stream()
            .anyMatch { it == tokens[0] }  // Проверяем, есть ли такой ключ
        if (!keyExists){
            try {
                LabWorkCollection.putInCollection(tokens[0], Generator.newLab(null, tokens.drop(1)))
                ServerOutput.send("Новый элемент успешно создан")
            }catch (e: Exception){
                ServerOutput.send(e.message.toString())
            }
        }
        else{
            ServerOutput.send("Данный ключ уже используется")}

        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "insert key - добавляет новый элемент с заданным ключом"
    }
}