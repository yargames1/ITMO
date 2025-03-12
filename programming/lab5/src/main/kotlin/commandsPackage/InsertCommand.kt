package commandsPackage

import labWorkClass.LabWorkCollection

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
    override fun execute(tokens: List<String>, auto: Boolean) {
        if (tokens.size == 1){
            if (tokens[0] !in LabWorkCollection.collection.keys){
                LabWorkCollection.collection.put(tokens[0], LabWorkCollection.newLab(null))
                println("Новый элемент успешно создан")
            }
            else{println("Данный ключ уже используется")}
        }
        else if (auto){
            if (tokens.size == 16){
                try {
                    LabWorkCollection.collection.put(tokens[0], LabWorkCollection.autoNewLab(null, tokens.drop(1)))
                }
                catch (e: Exception){
                    println("Произошла ошибка при создании нового элемента, проверьте аргументы команды")
                }
            }
            else{
                println("Произошла ошибка при чтении команды из файла: не хватает аргументов")
            }
        }
        else {
            println("Введите команду верно - insert key, где key - ключ для нового элемента коллекции")
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