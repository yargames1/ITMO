package commandsPackage

import Generator
import labWorkClass.LabWorkCollection
import managersPackage.IOManager

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
    override fun execute(tokens: List<String>) {
        if (tokens.size == 1){
            val key = tokens[0]
            if (key in LabWorkCollection.getCollection().keys) {
                try {
                    val newLab = Generator.newLab(null)
                    if (newLab > LabWorkCollection.getCollection().getValue(key)){
                        LabWorkCollection.replaceInCollection(key, newLab)
                        IOManager.send("Данные заменены")
                    }
                    else{
                        IOManager.send("Данные не были заменены, значение оказалось меньше или равно")
                    }
                }catch (e: Exception){
                    IOManager.send(e.message.toString())
                }
            }
            else{
                IOManager.send("Элемента с таким ключем не существует")
            }
        }

        else {
            IOManager.send("Введите команду верно - replace_if_greater key, где key - ключ элемента коллекции")}
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