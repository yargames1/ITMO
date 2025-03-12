package labWorkClass

object Validator {
    fun coords(coord: String, auto: Boolean = false): List<Int> {
            try {
                val coordinates = coord.split(" ").map { it.toInt() }
                if ((coordinates.size == 2) and (coordinates[1] <= 858)) {
                    return coordinates
                } else {
                    println("Произошла ошибка при вводе данных, у должен быть не больше 858")
                }
            } catch (e: Exception) {
                println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y")
            }
        if (!auto) {
            while (true) {
                try {
                    print("> ")
                    val newCoord = readln().split(" ").map { it.toInt() }
                    if ((newCoord.size == 2) and (newCoord[1] <= 858)) {
                        return newCoord
                    } else {
                        println("Произошла ошибка при вводе данных, у должен быть не больше 858")
                    }
                } catch (e: Exception) {
                    println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y")
                }
            }
        }
        else{
            throw Exception()
        }
    }
    fun longMoreThanZero(long: String, auto: Boolean = false):Long{
        try {
            if (long.toLong() > 0){
                return long.toLong()
            }
            else{println("Произошла ошибка при вводе данных, число должно быть больше 0")}
        }
        catch (e: Exception){
            println("Произошла ошибка при вводе данных, это должно быть число")
        }
        if (!auto) {
            while (true) {
                try {
                    print("> ")
                    val newLong = readln().toLong()
                    if (newLong > 0) {
                        return newLong
                    } else {
                        println("Произошла ошибка при вводе данных, у должен быть не больше 858")
                    }
                } catch (e: Exception) {
                    println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y")
                }
            }
        }
        else{
            throw Exception()
        }
    }
    fun doubleMoreThanZero(double: String, auto: Boolean = false):Double{
        try {
            if (double.toDouble() > 0){
                return double.toDouble()
            }
            else{println("Произошла ошибка при вводе данных, число должно быть больше 0")}
        }
        catch (e: Exception){
            println("Произошла ошибка при вводе данных, это должно быть число")
        }
        if (!auto) {
            while (true) {
                try {
                    print("> ")
                    val newDouble = readln().toDouble()
                    if (newDouble > 0) {
                        return newDouble
                    } else {
                        println("Произошла ошибка при вводе данных, у должен быть не больше 858")
                    }
                } catch (e: Exception) {
                    println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y")
                }
            }
        }
        else{
            throw Exception()
        }
    }
    fun enum(diff: String, auto: Boolean = false): Difficulty {
        try {
            val difficulty = Difficulty.valueOf(diff.uppercase())
            return difficulty
        }
        catch (e: Exception){
            println("Произошла ошибка при вводе данных, попробуйте снова")
        }
        if (!auto) {
            while (true) {
                try {
                    print("> ")
                    val newDifficulty = Difficulty.valueOf(readln().uppercase())
                    return newDifficulty
                } catch (e: Exception) {
                    println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y")
                }
            }
        }
        else{
            throw Exception()
        }
    }
    fun str(string: String, auto: Boolean = false):String{
        if (string.trim() != ""){
            return string
        }
        if (!auto) {
            while (true) {
                print("> ")
                val newString = readln().trim()
                if (newString != "") {
                    return newString
                }
            }
        }
        else{
            throw Exception()
        }
    }
    private fun timePatternCheck(time: String): Boolean {
        val timePattern = """^\d{2}:\d{2}$""".toRegex()

        if (time.matches(timePattern)) {
            val (hours, minutes) = time.split(":").map { it.toInt() }
            if (hours in 0..24) {
                if (minutes in 0..59) {
                    return true
                } else {
                    println("Неверное количество минут. Попробуйте снова.")
                }
            } else {
                println("Неверное количество часов. Попробуйте снова.")
            }
        } else {
            println("Неверный формат времени. Попробуйте снова.")
        }
        return false
    }
    private fun zonePatternCheck(time: String): Boolean {
        val timePattern = """^[+-]\d{2}:\d{2}$""".toRegex()

        if (time.matches(timePattern)) {
            val (hours, minutes) = time.split(":").map { it.toInt() }
            if (hours in -18..18) {
                if (minutes in 0..59) {
                    return true
                } else {
                    println("Неверное количество минут. Попробуйте снова.")
                }
            } else {
                println("Неверное количество часов. Попробуйте снова.")
            }
        } else {
            println("Неверный формат времени. Попробуйте снова.")
        }
        return false
    }

    private fun datePatternCheck(date:String):Boolean{
        val datePattern = """^\d{4}\.\d{2}\.\d{2}$""".toRegex()
        val daysInMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        if (date.matches(datePattern)) {
            val (year, month, day) = date.split(".").map { it.toInt() }
            if (month in 1..12) {
                if (day in 1..daysInMonth[month - 1]) {
                    return true
                } else {
                    println("Неверное количество дней в месяце. Попробуйте снова.")
                }
            } else {
                println("Месяц должен быть в пределах от 01 до 12. Попробуйте снова.")
            }
        } else {
            println("Неверный формат даты. Попробуйте снова.")
        }
        return false
    }
    fun date(date: String, auto: Boolean = false): String {
        if (datePatternCheck(date)){
            return date
        }
        if (!auto) {
            while (true) {
                print("> ")
                val newDate = readln()
                if (datePatternCheck(newDate)) {
                    return newDate
                }
            }
        }
        else{
            throw Exception()
        }
    }
    fun time(time: String, auto: Boolean = false): String {
        if (timePatternCheck(time)){
            return time
        }
        if (!auto) {
            while (true) {
                print("> ")
                val newTime = readln()
                if (timePatternCheck(newTime)) {
                    return newTime
                }
            }
        }
        else{
            throw Exception()
        }
    }
    fun zone(zone: String, auto: Boolean = false): String {
        if (zonePatternCheck(zone)){
            return zone
        }
        if (!auto) {
            while (true) {
                print("> ")
                val newZone = readln()
                if (zonePatternCheck(newZone)) {
                    return newZone
                }
            }
        }
        else{
            throw Exception()
        }
    }
    fun loc(loc: String, auto: Boolean = false): Location{
        try {
            val loc = loc.split(" ")
            if (loc.size == 3){
                val coord_X = loc[0].toDouble()
                val coord_Y = loc[1].toFloat()
                val coord_Z = loc[2].toDouble()
                return Location(coord_X, coord_Y, coord_Z)
            }
            else{println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y z")}
        }
        catch (e: Exception){
            println("Произошла ошибка при вводе данных, x - Double, y - Float, z - Double")
        }
        if(!auto){
            while (true) {
                print("> ")
                val newLoc = readln().split(" ")
                if (newLoc.size == 3){
                    val coord_X = newLoc[0].toDouble()
                    val coord_Y = newLoc[1].toFloat()
                    val coord_Z = newLoc[2].toDouble()
                    return Location(coord_X, coord_Y, coord_Z)
                }
                else{println("Произошла ошибка при вводе данных, напишите координаты еще раз в формате x y z")}
            }
        }
        else{
            throw Exception()
        }
    }
}