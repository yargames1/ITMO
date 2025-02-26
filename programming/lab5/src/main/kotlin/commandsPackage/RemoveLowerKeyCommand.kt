package commandsPackage

import labWorkClass.LabWorkCollection

class RemoveLowerKeyCommand: Command {
    /**
     * Класс, реализующий команду удаления элементов, ключи которых меньше определенных
     */
    override fun execute(tokens: List<String>, auto: Boolean) {
        /**
         * Выполняет команду с переданными аргументами.
         *
         * @param tokens Список, содержащий команду и её аргументы.
         * @param auto Флаг, указывающий, выполняется ли команда автоматически
         */
        if (tokens.size == 1){
            if (tokens[0] in LabWorkCollection.collection.keys) {
                for (key in LabWorkCollection.collection.keys.toList()){
                    if (key < tokens[0]){
                        LabWorkCollection.collection.remove(key)
                    }
                }
                println("Удаление завершено")
            }
            else{
                println("Элемента с таким ключем не существует")
            }
        }
        else {println("Введите команду верно - remove_lower_key key, где key - ключ элемента коллекции")}
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "remove_lower_key key - удаляет из коллекции все элементы, ключ которых меньше, чем заданный"
    }
}