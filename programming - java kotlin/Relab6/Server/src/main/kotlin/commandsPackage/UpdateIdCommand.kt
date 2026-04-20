package Server.commandsPackage

import Server.Generator
import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду обновления объекта по id
 */
class UpdateIdCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        val id = tokens[0].toIntOrNull()
        val key: String = if (id != null) {
            LabWorkCollection.getKeyById(id)
        } else{
            ""
        }
        if (key!="") {
            try {
                LabWorkCollection.replaceInCollection(key, Generator.newLab(id, tokens.drop(1)))
                ServerOutput.send("Элемент успешно обновлен")
            }catch (e: Exception){
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