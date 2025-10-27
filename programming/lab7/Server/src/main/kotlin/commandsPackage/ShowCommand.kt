package commandsPackage

import labWorkClass.LabWorkCollection
import ClientState
import ServerOutput
import Stage

/**
 * Класс, реализующий команду вывода элементов коллекции в строковом представлении
 */
class ShowCommand: InformationCommand {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>, state: ClientState) {
        if (tokens.isEmpty()){
            ServerOutput.send("key - id,name,cordX,cordY,creationDate,minimalPoint,personalQualitiesMinimum,difficulty,name,birthday,height,weight,locX,locY,locZ. Кому принадлежит")
            for (key in LabWorkCollection.getCollection().keys){
                ServerOutput.send("$key - ${LabWorkCollection.getCollection()[key]}")
            }
        }
        else {
            ServerOutput.send("Команда не должна иметь аргументов")}
        state.stage = Stage.WRITE_RESULT
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "show - выводит все элементы коллекции в строковом представлении"
    }

}