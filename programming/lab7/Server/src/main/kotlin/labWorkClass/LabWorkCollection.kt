package labWorkClass

import Generator
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
    fun getMaxId(): Int {
        return getCollection()
            .values
            .maxOfOrNull { it.getId() } ?: -1
    }

    /**
     * Выдает значение ключа по id
     *
     * @param id id элемента
     *
     * @return Ключ элемента
     */
    fun getKeyById(id: Int): String {
        return getCollection()
            .asSequence()
            .firstOrNull { it.value.getId() == id }
            ?.key ?: ""
    }
    /**
     * Выдает значение ключа по personalQualitiesMinimum
     *
     * @param pqm personalQualitiesMinimum элемента
     *
     * @return Ключ элемента
     */
    fun getKeyByPQM(pqm: Long): String{
        return collection.entries
            .stream()
            .filter { it.value.getPersonalQualitiesMinimum() == pqm }
            .findAny()
            .map { it.key }
            .orElse("")
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