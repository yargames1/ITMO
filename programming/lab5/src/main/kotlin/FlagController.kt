/**
 * Объект, хранящий и изменяющий флаг выхода.
 */
object FlagController {
    private var exitFlag = false
    fun turnOff(){
        exitFlag = true
    }
    fun getFlag(): Boolean {
        return exitFlag
    }
}