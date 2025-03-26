/**
 * Объект, хранящий и изменяющий флаг выхода.
 */
object FlagController {
    private var exitFlag = false
    private var autoFlag = false
    fun turnOff(){
        exitFlag = true
    }
    fun getExitFlag(): Boolean {
        return exitFlag
    }
    fun startAuto(){
        autoFlag = true
    }
    fun stopAuto(){
        autoFlag = false
    }
    fun getAutoFlag(): Boolean{
        return autoFlag
    }
}