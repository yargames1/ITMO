package Server.commandsPackage

import Server.Generator
import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду замены элемента по ключу, если он меньше старого
 */
class ReplaceIfLowerCommand : Command {
    override fun execute(tokens: List<String>, state: ClientState) {
        /**
         * Выполняет команду с переданными аргументами.
         *
         * @param tokens Список, содержащий команду и её аргументы.
         * @param auto Флаг, указывающий, выполняется ли команда автоматически
         */
        val key = tokens[0]
        if (key in LabWorkCollection.getCollection().keys) {
            try {
                val newLab = Generator.newLab(null, tokens.drop(1))
                if (newLab < LabWorkCollection.getCollection().getValue(key)){
                    LabWorkCollection.replaceInCollection(key, newLab)
                    ServerOutput.send("Данные заменены")
                }
                else{
                    ServerOutput.send("Данные не были заменены, значение оказалось больше или равно")
                }
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