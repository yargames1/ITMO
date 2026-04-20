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
        LabWorkCollection.setCollection(DataManager.load())
        while (!FlagManager.getExitFlag()) {
            if (!FlagManager.getAutoFlag()){IOManager.newString()}
            CommandManager.getCommand(IOManager.read())
        }
    }
}
