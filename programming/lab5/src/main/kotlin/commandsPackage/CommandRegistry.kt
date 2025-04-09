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
        "save" to SaveCommand(),
        "exit" to ExitCommand(),
        "replace_if_greater" to ReplaceIfGreaterCommand(),
        "replace_if_lower" to ReplaceIfLowerCommand(),
        "remove_lower_key" to RemoveLowerKeyCommand(),
        "remove_any_by_personal_qualities_minimum" to RemoveAnyByPersonalQualitiesMinimumCommand(),
        "min_by_name" to MinByNameCommand(),
        "print_field_descending_author" to PrintFieldDescendingAuthorCommand(),
        "execute_script" to ExecuteScriptCommand()
    )
}