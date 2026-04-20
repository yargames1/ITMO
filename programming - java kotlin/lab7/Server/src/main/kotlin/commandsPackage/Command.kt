package commandsPackage

import ClientState

interface Command {
    /**
     * Выполняет команду с переданными аргументами.
     *
     * @param tokens Список, содержащий команду и её аргументы.
     */
    fun execute(tokens: List<String>, state: ClientState)
    /**
     * Возвращает описание команды.
     *
     * @return Текстовое описание команды.
     */
    fun describe(): String
}