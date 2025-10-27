package managersPackage

import ClientState
import ServerOutput
import Stage
import commandsPackage.CommandRegistry
import commandsPackage.InformationCommand
import commandsPackage.UpdateCommand

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
        val tokens = list.flatMap { it.split(" ") }
        when (val command = tokens[0]) {
            in CommandRegistry.specialCommands -> {
                CommandRegistry.specialCommands[command]?.execute(tokens.drop(1))
            }
            in CommandRegistry.commands -> {
                val passwdIndex = tokens.size
                val login = tokens[passwdIndex-2]
                val passwd = tokens[passwdIndex-1]
                // Сделать проверку на доступ к базе
                val permission = DbManager.logIn(listOf(login, passwd))
                if (permission > 0) {
                    when (CommandRegistry.commands[command]) {
                        is InformationCommand -> CommandRegistry.commands[command]?.execute(
                            tokens.drop(1).take(passwdIndex - 3), state
                        )

                        is UpdateCommand -> CommandRegistry.commands[command]?.execute(
                            tokens.drop(1).take(passwdIndex - 2), state
                        )
                    }
                }
                else if (permission == -1){
                    ServerOutput.send("Не удалось подключиться к базе данных")
                }
            }
            else -> {
                state.stage = Stage.READ
            }
        }
    }
}