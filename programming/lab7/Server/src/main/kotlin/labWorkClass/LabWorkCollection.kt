package labWorkClass

import Generator
import managersPackage.DbManager
import java.time.LocalDate
import java.util.concurrent.locks.ReentrantLock

/**
 * Объект управляет коллекцией лабораторных работ.
 * Хранит данные в виде HashMap, где ключ — строка, а значение — объект LabWork.
 */
object LabWorkCollection {
    private var collection = HashMap<String, LabWork>()
    private val inicializationDate = Generator.newDate()
    private val lock = ReentrantLock()

    /**
     * Возвращает максимальный id, которые используются
     *
     * @return id, число
     */
    fun getMaxId(): Int {
        lock.lock()
        try {
            return getCollection()
                .values
                .maxOfOrNull { it.getId() } ?: -1
        }finally {
            lock.unlock()
        }
    }

    /**
     * Выдает значение ключа по id
     *
     * @param id id элемента
     *
     * @return Ключ элемента
     */
    fun getKeyById(id: Int): String {
        lock.lock()
        try {
        return getCollection()
            .asSequence()
            .firstOrNull { it.value.getId() == id }
            ?.key ?: ""
        }finally {
            lock.unlock()
        }
    }
    /**
     * Выдает значение ключа по personalQualitiesMinimum
     *
     * @param pqm personalQualitiesMinimum элемента
     *
     * @return Ключ элемента
     */
    fun getKeyByPQM(pqm: Long): String{
        lock.lock()
        try {
        return collection.entries
            .stream()
            .filter { it.value.getPersonalQualitiesMinimum() == pqm }
            .findAny()
            .map { it.key }
            .orElse("")
        }finally {
            lock.unlock()
        }
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
        lock.lock()
        try {
        return HashMap(collection)
        }finally {
            lock.unlock()
        }
    }
    /**
     * Устанавоивает коллекцию.
     *
     * @param coll коллекция
     */
    fun setCollection(coll: HashMap<String, LabWork>){
        lock.lock()
        try {
        collection = coll
        }finally {
            lock.unlock()
        }
    }
    /**
     * Очищает коллекцию.
     */
    fun clearConnection(login: String): String {
        lock.lock()
        try {
        val rez = DbManager.clear(login)
        if (rez > 0) {
            collection.clear()
            return "Коллекция очищена"
        }
        else if (rez == 0){
            return "В коллекции нет элементов, которые вы можете удалить"
        }
        else{
            return "Не удалось подключиться к базе данных, попробуйте еще раз"
        }
        }finally {
            lock.unlock()
        }
    }
    /**
     * Добавляет в коллекцию элемент.
     *
     * @param K ключ
     * @param V новый элемент
     */
    fun putInCollection(K: String, V: List<String>, login: String){
        lock.lock()
        try {
        val newID = DbManager.insert(K, V, login)
        val newLab = Generator.newLab(newID, V, login)
        collection[K] = newLab
        }finally {
            lock.unlock()
        }
    }
    /**
     * Удаляет элемент по ключу.
     *
     * @param K ключ
     */
    fun removeFromCollection(K: String, login: String): String {
        lock.lock()
        try {
        val rez = DbManager.remove(K, login)
        if (rez > 0) {
            collection.remove(K)
            return "Элемент удален"
        }
        else if (rez == 0){
            return "Этот элемент удалить нельзя, у вас нет прав"
        }
        else{
            return "Не удалось подключиться к базе данных, попробуйте еще раз"
        }
        }finally {
            lock.unlock()
        }
    }
    /**
     * Заменяет в коллекции элемент.
     *
     * @param K ключ
     * @param V новый элемент
     */
    fun replaceInCollection(K: String, V: List<String>, mode: Int = 0, login: String): String {
        lock.lock()
        try {
        when (mode) {
            0 -> {
                val id = V[0].toInt()
                val rez = DbManager.updateId(id, K, V.drop(1), login)
                if (rez > 0) {
                    collection.replace(K, Generator.newLab(id, V.drop(1), login))
                    return "Элемент успешно обновлен"
                } else if (rez == 0) {
                    return "Элемент не будет обновлен, у вас нет прав"
                }
                return "Не удалось подключиться к базе данных, попробуйте еще раз"

            }

            1 -> {
                val id = DbManager.getNewId()
                val key: String = getKeyById(id)

                val newLab = Generator.newLab(id, V, login)
                if (newLab > getCollection().getValue(key)) {
                    val rez = DbManager.updateId(id, key, V, login)
                    if (rez > 0) {
                        collection.replace(K, newLab)
                        return "Данные заменены"
                    } else if (rez == 0) {
                        return "Данные не будут заменены, у вас нет прав"
                    }
                    return "Не удалось подключиться к базе данных, попробуйте еще раз"
                }
                return "Данные не были заменены, значение оказалось меньше или равно"
            }

            else -> {
                val id = DbManager.getNewId()
                val key: String = getKeyById(id)

                val newLab = Generator.newLab(id, V, login)
                if (newLab < getCollection().getValue(key)) {
                    val rez = DbManager.updateId(id, key, V, login)
                    if (rez > 0) {
                        collection.replace(K, newLab)
                        return "Данные заменены"
                    } else if (rez == 0) {
                        return "Данные не будут заменены, у вас нет прав"
                    }
                    return "Не удалось подключиться к базе данных, попробуйте еще раз"
                }
                return "Данные не были заменены, значение оказалось больше или равно"
            }
        }
    }finally {
        lock.unlock()
    }
    }
}