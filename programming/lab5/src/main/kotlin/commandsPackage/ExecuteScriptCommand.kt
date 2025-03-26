package commandsPackage

import labWorkClass.LabWorkCollection
import managersPackage.FlagManager
import managersPackage.CommandManager
import managersPackage.IOManager
import kotlin.io.path.*
/**
 * Класс, реализующий команду запуска из файла.
 */
class ExecuteScriptCommand: Command {
    /**
     * Перенаправляет данные из файла IOManager.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>) {
        try {
            val currentFile = tokens[0]
            LabWorkCollection.addFile(currentFile)
            val path = Path("files", currentFile)
            val list = mutableListOf<String>()
            for (line in path.readLines()){
                // отправка текста IoManager
                if (line.split(" ").all{ it !in LabWorkCollection.getOpendFiles()}) { // проверка на запуск открытого файла
                    list.add(line)
                }

            }
            IOManager.addText(list)
            FlagManager.startAuto()
            CommandManager.getCommand(IOManager.read())
        }

        catch (e: Exception){
            IOManager.send("Произошла ошибка при чтении файла $e")
        }
    }
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    override fun describe(): String {
        return "execute_script file_name - считать и исполнить скрипт из указанного файла. " +
                "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме." +
                "Скрипт должен находиться в папке files"
    }
}
