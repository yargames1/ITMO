package labWorkClass

import managersPackage.IOManager
import java.time.ZonedDateTime
/**
 * Объект управляет коллекцией лабораторных работ.
 * Хранит данные в виде HashMap, где ключ — строка, а значение — объект LabWork.
 */
object LabWorkCollection {
    var collection = HashMap<String, LabWork>()
    val inicializationDate = Generator.newDate()

    /**
     * Возвращает максимальный id, которые используются
     *
     * @return id, число
     */
    fun getMaxId(): Int{
        val list = mutableListOf<Int>()
        for (values in collection.values){
            list.add(values.getId())
        }
        return list.maxOrNull() ?: -1
    }

    /**
     * Выдает значение ключа по id
     *
     * @param id id элемента
     *
     * @return Ключ элемента
     */
    fun getKeyById(id: Int): String{
        for (key in collection.keys){
            if (collection.getValue(key).getId() == id){
                return key
            }
        }
        return ""
    }
    /**
     * Выдает значение ключа по personalQualitiesMinimum
     *
     * @param personalQualitiesMinimum personalQualitiesMinimum элемента
     *
     * @return Ключ элемента
     */
    fun getKeyByPQM(personalQualitiesMinimum: Long): String{
        for (key in collection.keys){
            if (collection.getValue(key).getPersonalQualitiesMinimum() == personalQualitiesMinimum){
                return key
            }
        }
        return ""
    }
    /**
     * Выдает значение ключа по имени, берет минимальное
     *
     * @return Ключ элемента
     */
    fun getMinByNameKey(): String{
        val names = mutableListOf<String>()
        for (key in collection.keys){
            names.add(collection.getValue(key).getName())
        }
        val minName = names.minOrNull()
        for(key in collection.keys){
            if (minName == collection.getValue(key).getName()){
                return key
            }
        }
        return ""
    }
    /**
     * Выдает список авторов, отсортированный по убыванию
     *
     * @return Список авторов
     */
    fun getDescendingAuthors(): List<Person>{
        return collection.entries.sortedByDescending { it.value.getAuthor() }.map { it.value.getAuthor() }
    }
}