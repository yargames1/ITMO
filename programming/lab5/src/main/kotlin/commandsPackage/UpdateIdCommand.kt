package commandsPackage

import labWorkClass.LabWorkCollection
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
    override fun execute(tokens: List<String>, auto: Boolean) {
        if ((tokens.size == 1) and (tokens[0].toIntOrNull() != null)){
            val id = tokens[0].toInt()
            val key = LabWorkCollection.getKeyById(id)
            if (key!="") {
                LabWorkCollection.collection.replace(key, LabWorkCollection.newLab(id))
                println("Элемент успешно обновлен")
            }
            else{
                println("Элемента с таким значением id не существует")
            }
        }
        else if (auto){
            val id = tokens[0].toInt()
            val key = LabWorkCollection.getKeyById(id)
            if (key!="") {
                try {
                    LabWorkCollection.collection.replace(key, LabWorkCollection.autoNewLab(id, tokens.drop(1)))
                    println("Элемент успешно обновлен")
                }
                catch (e: Exception){
                    println("Произошла ошибка при обновлении элемента, проверьте аргументы команды")
                }
            }
            else{
                println("Элемента с таким значением id не существует")
            }
        }
        else {println("Введите команду верно - update id, где id - id элемента коллекции")}
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