package commandsPackage

import Generator
import labWorkClass.LabWorkCollection
import managersPackage.IOManager

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
    override fun execute(tokens: List<String>) {
        if ((tokens.size == 1) and (tokens[0].toIntOrNull() != null)){
            val id = tokens[0].toInt()
            val key = LabWorkCollection.getKeyById(id)
            if (key!="") {
                try {
                    LabWorkCollection.replaceInCollection(key, Generator.newLab(id))
                    IOManager.send("Элемент успешно обновлен")
                }catch (e: Exception){
                    IOManager.send(e.message.toString())
                }
            }
            else{
                IOManager.send("Элемента с таким значением id не существует")
            }
        }

        else {
            IOManager.send("Введите команду верно - update id, где id - id элемента коллекции")}
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