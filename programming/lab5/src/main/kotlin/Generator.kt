import labWorkClass.LabWorkCollection
import java.time.LocalDate
/**
 * Объект для генерации уникальных идентификаторов и дат.
 */
object Generator {
    /**
     * Генерирует новый уникальный ID для LabWork.
     *
     * @return новый ID, который является максимальным существующим ID + 1.
     */
    fun newId(): Int{
        return LabWorkCollection.getMaxId()+1
    }
    /**
     * Возвращает текущую дату.
     *
     * @return текущая дата в формате LocalDate.
     */
    fun newDate(): LocalDate {
        return LocalDate.now()
    }
}