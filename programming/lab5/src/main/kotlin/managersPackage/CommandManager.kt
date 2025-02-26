package managersPackage

import commandsPackage.CommandRegistry
/**
 * Объект для управления выполнением команд.
 *
 * Получает строку команды, парсит её и передаёт выполнение соответствующему объекту команды.
 */
object CommandManager{
    /**
     * Выполняет команду, полученную из строки ввода.
     *
     * @param line строка, содержащая команду и её аргументы.
     */
    fun getCommand(line: String){
        val tokens = line.split(" ")
        val command = tokens[0]
        CommandRegistry.commands[command]?.execute(tokens.drop((1)))
    }
    /**
     * Выполняет команду в автоматическом режиме.
     *
     * @param line строка, содержащая команду и её аргументы.
     */
    fun autoGetCommand(line: String){
        val tokens = line.split(" ")
        val command = tokens[0]
        CommandRegistry.commands[command]?.execute(tokens.drop((1)), auto = true)
    }
}