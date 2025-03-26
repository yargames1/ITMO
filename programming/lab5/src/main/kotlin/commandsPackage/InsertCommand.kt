package commandsPackage

import Generator
import labWorkClass.LabWorkCollection
import managersPackage.IOManager

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
    override fun execute(tokens: List<String>) {
        if (tokens.size == 1){
            if (tokens[0] !in LabWorkCollection.getCollection().keys){
                try {
                    LabWorkCollection.putInCollection(tokens[0], Generator.newLab(null))
                    IOManager.send("Новый элемент успешно создан")
                }catch (e: Exception){
                    IOManager.send(e.message.toString())
                }
            }
            else{IOManager.send("Данный ключ уже используется")}
        }
        else {
            IOManager.send("Введите команду верно - insert key, где key - ключ для нового элемента коллекции")
        }
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