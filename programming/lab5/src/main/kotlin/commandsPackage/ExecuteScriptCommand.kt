package commandsPackage

import managersPackage.CommandManager
import kotlin.io.path.*
/**
 * Класс, реализующий команду запуска из файла.
 */
class ExecuteScriptCommand: Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     * @param auto Флаг, указывающий, выполняется ли команда автоматически
     */
    override fun execute(tokens: List<String>, auto: Boolean) {
        try {
            val currentFile = tokens[0]
            val path = Path("files\\${tokens[0]}")
            var newTokens = ""
            var newCommand = ""
            for (line in path.readLines()){
                val readTokens = line.split(" ")
                if (readTokens[0] in CommandRegistry.commands.keys){
                    println("Выполняется команда $newCommand $newTokens".trim())
                    if ((currentFile !in newTokens) or (newCommand != "execute_script") ){CommandManager.autoGetCommand("$newCommand $newTokens".trim())}
                    else{ println("команда по запуску файла не будет выполнена, так как он уже открыт") }
                    newCommand = readTokens[0].toString()
                    newTokens = readTokens.drop(1).joinToString(separator = " ")
                }
                else{
                    newTokens += " "+readTokens.joinToString(separator = " ")
                }
            }
            CommandManager.autoGetCommand("$newCommand $newTokens".trim())
        }

        catch (e: Exception){
            println("Произошла ошибка при чтении файла")
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