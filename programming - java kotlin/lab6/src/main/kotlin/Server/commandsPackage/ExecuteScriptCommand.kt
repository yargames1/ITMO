package Server.commandsPackage

import Server.ClientState
/**
 * Класс, реализующий команду запуска из файла.
 */
class ExecuteScriptCommand: Command {
    /**
     * Программа полностью исполняется на клиентском приложении, поэтому ее выполнения никогда не будет.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    override fun execute(tokens: List<String>, state: ClientState) {
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
