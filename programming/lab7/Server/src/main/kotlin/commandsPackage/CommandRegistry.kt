package commandsPackage


/**
 * Объект, хранящий список доступных команд
 */
object CommandRegistry {
    /**
     * Хеш-таблица доступных команд.
     * Ключ — строковое название команды, значение — объект, реализующий `Command`.
     */

    val commands = mutableMapOf(
        "help" to HelpCommand(),
        "info" to InfoCommand(),
        "show" to ShowCommand(),
        "insert" to InsertCommand(),
        "update" to UpdateIdCommand(),
        "remove_key" to RemoveKeyCommand(),
        "clear" to ClearCommand(),
        "exit" to ExitCommand(),
        "replace_if_greater" to ReplaceIfGreaterCommand(),
        "replace_if_lower" to ReplaceIfLowerCommand(),
        "remove_lower_key" to RemoveLowerKeyCommand(),
        "remove_any_by_personal_qualities_minimum" to RemoveAnyByPersonalQualitiesMinimumCommand(),
        "min_by_name" to MinByNameCommand(),
        "print_field_descending_author" to PrintFieldDescendingAuthorCommand(),
        "execute_script" to ExecuteScriptCommand()
    )
    val specialCommands = mutableMapOf(
        "get_list_of_client_cmds" to GetClientCommands(),
        "get_list_of_server_cmds" to GetServerCommand()
    )
    val clientCommands = mutableListOf(
        "insert",
        "update",
        "replace_if_greater",
        "replace_if_lower"
    )
    val serverCommands = mutableListOf(
        "help",
        "info",
        "show",
        "remove_key",
        "clear",
        "exit",
        "remove_lower_key",
        "remove_any_by_personal_qualities_minimum",
        "min_by_name",
        "print_field_descending_author"
        )
}