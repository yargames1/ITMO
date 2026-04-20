package commandsPackage

import Generator
import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage

/**
 * Класс, реализующий команду замены элемента по ключу, если он меньше старого
 */
class ReplaceIfLowerCommand : UpdateCommand {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>, state: ClientState) {

        val key = tokens[0]
        if (key in LabWorkCollection.getCollection().keys) {
            try {
                val loginIndex = tokens.size
                ServerOutput.send(LabWorkCollection.replaceInCollection(key, tokens.drop(1).take(loginIndex-1), 2, tokens[loginIndex-1]))
            }catch (e: Exception){
                ServerOutput.send(e.message.toString())
            }
        }
        else{
            ServerOutput.send("Элемента с таким ключем не существует")
        }

        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "replace_if_lower - заменяет значение по ключу, если оно меньше старого"
    }
}