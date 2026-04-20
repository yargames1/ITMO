package commandsPackage

import Generator
import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage
import managersPackage.DbManager

/**
 * Класс, реализующий команду вставки нового элемента коллекции
 */
class InsertCommand: UpdateCommand {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        // Используем Stream API для проверки наличия ключа
        val keyExists = LabWorkCollection.getCollection().keys.stream()
            .anyMatch { it == tokens[0] }  // Проверяем, есть ли такой ключ
        if (!keyExists){
            try {
                val loginIndex = tokens.size
                LabWorkCollection.putInCollection(tokens[0], tokens.drop(1).take(loginIndex-1), tokens[loginIndex-1])
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