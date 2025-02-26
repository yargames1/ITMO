package labWorkClass

/**
 * Класс, реализующий представляющий местоположение в трехмерном пространстве.
 *
 * @property x Поле не может быть null
 * @property y Поле не может быть null
 * @property z
 */
class Location(
    private val x: Double,
    private val y: Float,
    private val z: Double
){
    /**
     * Возвращает строковое представление координат локации в формате CSV.
     *
     * @return Строка с координатами
     */
    override fun toString(): String {
        return "$x,$y,$z"
    }
}