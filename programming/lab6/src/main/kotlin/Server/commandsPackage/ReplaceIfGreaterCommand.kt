package Server.commandsPackage

import Server.Generator
import Server.labWorkClass.LabWorkCollection
import Server.ClientState
import Server.ServerOutput
import Server.Stage

/**
 * Класс, реализующий команду замены элемента по ключу, если он больше старого
 */
class ReplaceIfGreaterCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        val key = tokens[0]
        if (key in LabWorkCollection.getCollection().keys) {
            try {
                val newLab = Generator.newLab(null, tokens.drop(1))
                if (newLab > LabWorkCollection.getCollection().getValue(key)){
                    LabWorkCollection.replaceInCollection(key, newLab)
                    ServerOutput.send("Данные заменены")
                }
                else{
                    ServerOutput.send("Данные не были заменены, значение оказалось меньше или равно")
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
        return "replace_if_greater - заменяет значение по ключу, если оно больше старого"
    }
}