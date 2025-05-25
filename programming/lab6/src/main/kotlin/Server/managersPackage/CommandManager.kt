package Server.managersPackage

import Server.ClientState
import Server.Stage
import Server.commandsPackage.CommandRegistry
/**
 * Объект для управления выполнением команд.
 *
 * Получает строку команды, парсит её и передаёт выполнение соответствующему объекту команды.
 */
object CommandManager{
    /**
     * Выполняет команду, полученную из строки ввода.
     *
     * @param list строка, содержащая команду и её аргументы.
     */
    fun getCommand(list: List<String>, state: ClientState){
        val tokens = list[0].split(" ").toMutableList()
        if (list.drop(1).isNotEmpty()){
            tokens += list[1].split(" ")
        }
        val command = tokens[0]
        if (command in CommandRegistry.commands) {
            CommandRegistry.commands[command]?.execute(tokens.drop(1), state)
        }
        else{
            state.stage = Stage.READ
        }
    }
}