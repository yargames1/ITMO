package managersPackage
import Generator
import Log.logger
import labWorkClass.*
import java.security.MessageDigest
import java.sql.Connection
import java.sql.DriverManager
import java.time.ZoneId
import java.time.ZonedDateTime

object DbManager {

    private var connection: Connection? = null
    fun connect() {
        val url = "jdbc:postgresql://localhost:5432/studs"
        val user = "s405271"
        val password = "QJqSuMmJPoLxoecU"

        try {
            connection = DriverManager.getConnection(url, user, password)
            logger.info("Успешное подключение к базе данных!")
            createTablesIfNotExists()

        } catch (e: Exception) {
            logger.info(e.printStackTrace().toString())
        }
    }

    private fun createTablesIfNotExists(){
        val sql1 = """
        CREATE TABLE IF NOT EXISTS labworkcollection (
            key TEXT PRIMARY KEY,
            id SERIAL,
            name TEXT NOT NULL,
            coord_x INT NOT NULL,
            coord_y INT NOT NULL,
            creation_date DATE NOT NULL,
            minimal_point BIGINT NOT NULL,
            personal_qualities_minimum BIGINT NOT NULL,
            difficulty VARCHAR(50) NOT NULL,
            author_name TEXT NOT NULL,
            birthday TIMESTAMPTZ NOT NULL,
            height DOUBLE PRECISION NOT NULL CHECK (height > 0),
            weight DOUBLE PRECISION CHECK (weight > 0),
            loc_x DOUBLE PRECISION NOT NULL,
            loc_y REAL NOT NULL,
            loc_z DOUBLE PRECISION NOT NULL,
            creator TEXT NOT NULL
        );
        """.trimIndent()
        connection?.let { conn -> conn.createStatement().use { it.execute(sql1) }
        }

        val sql2 = """
        CREATE TABLE IF NOT EXISTS users (
            id SERIAL PRIMARY KEY,
            login TEXT NOT NULL,
            passwd TEXT NOT NULL
        );
        """.trimIndent()
        connection?.let { conn -> conn.createStatement().use { it.execute(sql2) }
        }
    }
    fun load(): HashMap<String, LabWork>{
        val newList = HashMap<String, LabWork>()
        val sql = "SELECT * FROM labworkcollection"

        connection?.let {
            it.createStatement().use { statement ->
                val resultSet = statement.executeQuery(sql)
                while (resultSet.next()) {
                    try {
                        // Считываем поля из ResultSet
                        val key = resultSet.getString("key")
                        val id = resultSet.getInt("id")
                        val name = resultSet.getString("name")
                        val coordX = resultSet.getInt("coord_x")
                        val coordY = resultSet.getInt("coord_y")
                        val creationDate = resultSet.getDate("creation_date").toLocalDate()
                        val minimalPoint = resultSet.getLong("minimal_point")
                        val personalQualitiesMinimum = resultSet.getLong("personal_qualities_minimum")
                        val difficulty = Difficulty.valueOf(resultSet.getString("difficulty"))
                        val authorName = resultSet.getString("author_name")
                        val birthday = resultSet.getTimestamp("birthday").toInstant().atZone(ZoneId.systemDefault())
                        val height = resultSet.getDouble("height")
                        val weight = resultSet.getDouble("weight")
                        val locX = resultSet.getDouble("loc_x")
                        val locY = resultSet.getFloat("loc_y")
                        val locZ = resultSet.getDouble("loc_z")

                        val location = Location(locX, locY, locZ)
                        val author = Person(authorName, birthday, height, weight, location)
                        val coordinates = Coordinates(coordX, coordY)
                        val creator = resultSet.getString("creator")
                        // Собираем в элементы коллекции
                        val labWork = LabWork(
                            id = id,
                            name = name,
                            coordinates = coordinates,
                            creationDate = creationDate,
                            minimalPoint = minimalPoint,
                            personalQualitiesMinimum = personalQualitiesMinimum,
                            difficulty = difficulty,
                            author = author,
                            creator = creator
                        )
                        newList[key] = labWork
                    } catch (e: Exception) {
                        logger.info("Ошибка при разборе записи из БД: ${e.message}")
                    }
                }
            }
        }
        logger.info("Данные загружены из базы данных $newList")
        return newList
    }
    fun insert(key: String, info: List<String>, login: String): Int{
        println("$key $info $login")
        val sql = """
            INSERT INTO labworkcollection (
                key, name, coord_x, coord_y, creation_date,
                minimal_point, personal_qualities_minimum, difficulty,
                author_name, birthday, height, weight, loc_x, loc_y, loc_z, creator
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()

        connection?.let {it.prepareStatement(sql).use { stmt ->

                stmt.setString(1, key)
                stmt.setString(2, info[0])
                stmt.setInt(3, info[1].toInt())
                stmt.setInt(4, info[2].toInt())
                stmt.setDate(5, java.sql.Date.valueOf(Generator.newDate()))
                stmt.setLong(6, info[3].toLong())
                stmt.setLong(7, info[4].toLong())
                stmt.setString(8, info[5])
                stmt.setString(9, info[6])
                stmt.setTimestamp(10, java.sql.Timestamp.from(ZonedDateTime.parse(info[7]).toInstant()))
                stmt.setDouble(11, info[8].toDouble())
                stmt.setDouble(12, info[9].toDouble())
                stmt.setDouble(13, info[10].toDouble())
                stmt.setFloat(14, info[11].toFloat())
                stmt.setDouble(15, info[12].toDouble())
                stmt.setString(16, login)

                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val newId = rs.getInt("id")
                    println("Inserted id = $newId")
                    return newId
                }
            }
        }
        return -1
    }
    fun updateId(id: Int, key: String, info: List<String>, login: String ): Int {
        val sql = """
            UPDATE labworkcollection
                SET key = ?,
                name = ?,
                coord_x = ?,
                coord_y = ?,
                creation_date = ?,
                minimal_point = ?,
                personal_qualities_minimum = ?,
                difficulty = ?,
                author_name = ?,
                birthday = ?,
                height = ?,
                weight = ?,
                loc_x = ?,
                loc_y = ?,
                loc_z = ?
             WHERE id = ? AND creator = ?
        """.trimIndent()
        connection?.let {
            it.prepareStatement(sql).use { stmt ->

                stmt.setString(1, key)
                stmt.setString(2, info[0])
                stmt.setInt(3, info[1].toInt())
                stmt.setInt(4, info[2].toInt())
                stmt.setDate(5, java.sql.Date.valueOf(Generator.newDate()))
                stmt.setLong(6, info[3].toLong())
                stmt.setLong(7, info[4].toLong())
                stmt.setString(8, info[5])
                stmt.setString(9, info[6])
                stmt.setTimestamp(10, java.sql.Timestamp.from(ZonedDateTime.parse(info[7]).toInstant()))
                stmt.setDouble(11, info[8].toDouble())
                stmt.setDouble(12, info[9].toDouble())
                stmt.setDouble(13, info[10].toDouble())
                stmt.setFloat(14, info[11].toFloat())
                stmt.setDouble(15, info[12].toDouble())
                stmt.setInt(16, id)
                stmt.setString(17, login)
                return stmt.executeUpdate()
            }
        }
        return -1
    }

    fun getNewId(): Int {
        val sql = "SELECT next('labworkcollection_id_seq');"
        connection?.let {
            val resultSet = it.createStatement().executeQuery(sql)
            return resultSet.getInt(1)
        }
        return -1
    }

    fun clear(login: String): Int{
        val sql = "DELETE FROM labworkcollection WHERE creator = ?"
        connection?.let {
            it.prepareStatement(sql).use { stmt ->
                stmt.setString(1, login)
                return stmt.executeUpdate()
            }
        }
        return -1
    }
    fun remove(K: String, login: String): Int{
        val sql = "DELETE FROM labworkcollection WHERE key = ? AND creator = ?"
        connection?.let {
            it.prepareStatement(sql).use { stmt ->
                stmt.setString(1, K)
                stmt.setString(2, login)
                return stmt.executeUpdate()
            }
        }
        return -1
    }
    fun checkLoginExist(login: String): Int{
        val sql = "SELECT COUNT(*) AS counts FROM users WHERE login = ?"
        connection?.let {
            it.prepareStatement(sql).use { stmt ->
                stmt.setString(1, login)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getInt("counts")
                }
            }
        }
        return -1
    }

    fun logIn(tokens: List<String>): Int {
        val sql = "SELECT COUNT(*) AS counts FROM users WHERE login = ? AND passwd = ?"
        connection?.let {
            it.prepareStatement(sql).use { stmt ->
                stmt.setString(1, tokens[0])
                val passwd = MessageDigest.getInstance("SHA-256").
                digest(tokens[1].toByteArray()).
                joinToString("") { "%02x".format(it) }
                stmt.setString(2, passwd)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getInt("counts")
                }
            }
        }
        return -1
    }

    fun reg(tokens: List<String>): Int {
        val sql = """
            INSERT INTO users (login, passwd)
            VALUES (?, ?);
        """.trimIndent()
        connection?.let {
            it.prepareStatement(sql).use {stmt ->
                stmt.setString(1, tokens[0])
                val passwd = MessageDigest.getInstance("SHA-256").
                digest(tokens[1].toByteArray()).
                joinToString("") { "%02x".format(it) }
                stmt.setString(2, passwd)
                return stmt.executeUpdate()
            }
        }
        return -1
    }
}