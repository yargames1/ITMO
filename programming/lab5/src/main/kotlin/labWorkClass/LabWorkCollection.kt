package labWorkClass

import java.time.LocalDate
/**
 * Объект управляет коллекцией лабораторных работ.
 * Хранит данные в виде HashMap, где ключ — строка, а значение — объект LabWork.
 */
object LabWorkCollection {
    private var collection = HashMap<String, LabWork>()
    private val inicializationDate = Generator.newDate()
    private val opendFiles = mutableListOf<String>()

    /**
     * Возвращает максимальный id, которые используются
     *
     * @return id, число
     */
    fun getMaxId(): Int{
        val list = mutableListOf<Int>()
        for (values in getCollection().values){
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
        for (key in getCollection().keys){
            if (getCollection().getValue(key).getId() == id){
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
        for (key in getCollection().keys){
            if (getCollection().getValue(key).getPersonalQualitiesMinimum() == personalQualitiesMinimum){
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
        for (key in getCollection().keys){
            names.add(getCollection().getValue(key).getName())
        }
        val minName = names.minOrNull()
        for(key in getCollection().keys){
            if (minName == getCollection().getValue(key).getName()){
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
    /**
     * Добавляет в список открытых файлов новый файл
     *
     * @param name имя файла
     */
    fun addFile(name: String){
        opendFiles.add(name)
    }
    /**
     * Очищает список открытых файлов
     */
    fun deleteFiles(){
        opendFiles.clear()
    }
    /**
     * Выдает список открытых файлов
     *
     * @return Список файлов
     */
    fun getOpendFiles(): MutableList<String> {
        return opendFiles
    }
    /**
     * Выдает дату инициализации
     *
     * @return Дата инициализации
     */
    fun getInicializationDate(): LocalDate {
        return inicializationDate
    }
    /**
     * Выдает коллекцию
     *
     * @return Коллекция
     */
    fun getCollection(): HashMap<String, LabWork> {
        return collection
    }
    /**
     * Устанавоивает коллекцию.
     *
     * @param coll коллекция
     */
    fun setCollection(coll: HashMap<String, LabWork>){
        collection = coll
    }
    /**
     * Очищает коллекцию.
     */
    fun clearConnection(){
        collection.clear()
    }
    /**
     * Добавляет в коллекцию элемент.
     *
     * @param K ключ
     * @param V новый элемент
     */
    fun putInCollection(K: String, V: LabWork){
        collection.put(K, V)
    }
    /**
     * Удаляет элемент по ключу.
     *
     * @param K ключ
     */
    fun removeFromCollection(K: String){
        collection.remove(K)
    }
    /**
     * Заменяет в коллекции элемент.
     *
     * @param K ключ
     * @param V новый элемент
     */
    fun replaceInCollection(K: String, V: LabWork){
        collection.remove(K, V)
    }
}