package Server.labWorkClass

import java.time.ZonedDateTime
/**
 * Класс, реализующий представляющий местоположение в трехмерном пространстве.
 *
 * @property name Поле не может быть null, Строка не может быть пустой
 * @property birthday Поле не может быть null
 * @property height Поле не может быть null, Значение поля должно быть больше 0
 * @property weight Значение поля должно быть больше 0
 * @property location Поле может быть null
 */
class Person(
    private val name: String,
    private val birthday: ZonedDateTime,
    private val height: Double,
    private val weight: Double,
    private val location: Location
): Comparable<Person>{
    /**
     * Возвращает строковое представление данных автора в формате CSV.
     *
     * @return Строка с данными автора.
     */
    override fun toString(): String {
        return "$name,$birthday,$height,$weight,$location"
    }
    /**
     * Сравнивает авторов по названию в лексикографическом порядке.
     *
     * @param other Другой автор для сравнения.
     * @return Отрицательное число, ноль или положительное число, если текущий объект меньше, равен или больше другого объекта соответственно.
     */
    override fun compareTo(other: Person): Int {
        return this.name.compareTo(other.name)
    }
}
