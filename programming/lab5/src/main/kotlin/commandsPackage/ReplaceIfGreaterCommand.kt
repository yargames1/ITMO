package commandsPackage

import labWorkClass.LabWorkCollection
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
    override fun execute(tokens: List<String>, auto: Boolean) {
        if (tokens.size == 1){
            val key = tokens[0]
            if (key in LabWorkCollection.collection.keys) {
                val newLab = LabWorkCollection.newLab(null)
                if (newLab > LabWorkCollection.collection.getValue(key)){
                    LabWorkCollection.collection.replace(key, newLab)
                    println("Данные заменены")
                }
                else{
                    println("Данные не были заменены, значение оказалось меньше или равно")
                }
            }
            else{
                println("Элемента с таким ключем не существует")
            }
        }
        else if (auto) {
            if (tokens.size == 14) {
                val key = tokens[0]
                if (key in LabWorkCollection.collection.keys) {
                    try {
                        val newLab = LabWorkCollection.autoNewLab(null, tokens.drop(1))
                        if (newLab > LabWorkCollection.collection.getValue(key)) {
                            LabWorkCollection.collection.replace(key, newLab)
                            println("Данные заменены")
                        } else {
                            println("Данные не были заменены, значение оказалось меньше или равно")
                        }
                    }catch (e: Exception){
                        println("Произошла ошибка при создании элемента для замены, проверьте аргументы команды")
                    }
                }
            } else {
                println("Произошла ошибка при чтении команды из файла: не хватает аргументов")
            }
        }

        else {println("Введите команду верно - replace_if_greater key, где key - ключ элемента коллекции")}
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