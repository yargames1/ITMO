package commandsPackage

import Generator
import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage
import managersPackage.DbManager

/**
 * Класс, реализующий команду обновления объекта по id
 */
class UpdateIdCommand: UpdateCommand {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        val id = tokens[0].toInt()
        val key: String = LabWorkCollection.getKeyById(id)
        if (key!="") {
            try {
                val loginIndex = tokens.size
                ServerOutput.send(LabWorkCollection.replaceInCollection(key, tokens.take(loginIndex-1), login = tokens[loginIndex-1]))
                }
            catch (e: Exception){
                ServerOutput.send(e.message.toString())
            }
        }
        else{
            ServerOutput.send("Элемента с таким значением id не существует")
        }

        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "update id - обновляет значение элемента коллекции, id которого равен заданному"
    }
}