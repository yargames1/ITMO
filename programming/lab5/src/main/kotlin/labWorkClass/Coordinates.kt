package labWorkClass
/**
 * Класс, реализующий создание координат
 */
class Coordinates(private val x: Int, coordY: Int) {
    private val y: Int = coordY //Максимальное значение поля: 858, Поле не может быть null

    override fun toString(): String {
        return "$x,$y"
    }
}