package labWorkClass

import java.time.LocalDate

/**
 * Класс LabWork представляет лабораторную работу с уникальными параметрами.
 *
 * @property id Уникальный идентификатор лабораторной работы (автоматически генерируется, не может быть null, должен быть > 0).
 * @property name Название лабораторной работы (не может быть null или пустым).
 * @property coordinates Координаты, связанные с лабораторной работой (не могут быть null, генерируются автоматически).
 * @property creationDate Дата создания лабораторной работы (не может быть null, должна быть больше 0).
 * @property minimalPoint Минимальный балл для выполнения (не может быть null, должен быть > 0).
 * @property personalQualitiesMinimum Минимальный уровень личных качеств (не может быть null).
 * @property difficulty Уровень сложности лабораторной работы (может быть null).
 * @property author Автор лабораторной работы (не может быть null).
 */
class LabWork(
    private val id: Int,
    private val name: String,
    private val coordinates: Coordinates,
    private val creationDate: LocalDate,
    private val minimalPoint: Long,
    private val personalQualitiesMinimum: Long,
    private val difficulty: Difficulty,
    private val author: Person
) : Comparable<LabWork> {

    /**
     * Сравнивает лабораторные работы по названию в лексикографическом порядке.
     *
     * @param other Другая лабораторная работа для сравнения.
     * @return Отрицательное число, ноль или положительное число, если текущий объект меньше, равен или больше другого объекта соответственно.
     */
    override fun compareTo(other: LabWork): Int {
        return this.name.compareTo(other.name)
    }

    /**
     * Возвращает строковое представление лабораторной работы в формате CSV.
     *
     * @return Строка с данными лабораторной работы.
     */
    override fun toString(): String {
        return "$id,$name,$coordinates,$creationDate,$minimalPoint,$personalQualitiesMinimum,$difficulty,$author"
    }

    /**
     * Получает идентификатор лабораторной работы.
     *
     * @return Уникальный ID.
     */
    fun getId(): Int {
        return this.id
    }

    /**
     * Получает минимальный уровень личных качеств.
     *
     * @return Минимальное значение личных качеств.
     */
    fun getPersonalQualitiesMinimum(): Long {
        return this.personalQualitiesMinimum
    }

    /**
     * Получает название лабораторной работы.
     *
     * @return Название лабораторной работы.
     */
    fun getName(): String {
        return this.name
    }

    /**
     * Получает автора лабораторной работы.
     *
     * @return Объект типа Person, представляющий автора.
     */
    fun getAuthor(): Person {
        return this.author
    }
}
