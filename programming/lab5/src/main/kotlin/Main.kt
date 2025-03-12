import labWorkClass.LabWorkCollection
import managersPackage.*
/**
 * Главный объект программы, запускающий консольное приложение.
 */
object Main {
    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются).
     */
    @JvmStatic
    fun main(args: Array<String>) {
        LabWorkCollection.collection = DataManager.load()
        while (!FlagController.getFlag()) {
            print("> ")
            CommandManager.getCommand(readln())
        }
    }
}