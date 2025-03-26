package labWorkClass

import managersPackage.IOManager

object Validator {
    fun coords(coord: String): Boolean {
        val coordinates = coord.split(" ").mapNotNull { it.toIntOrNull() }
        return coordinates.size == 2 && coordinates[1] <= 858
    }

    fun longMoreThanZero(long: String):Boolean{
        return long.toLongOrNull()?.let { it > 0 } ?: false
    }

    fun doubleMoreThanZero(double: String):Boolean{
        return double.toDoubleOrNull()?.let { it > 0 } ?: false
    }

    fun enum(diff: String): Boolean {
        return  Difficulty.values().any { it.name == diff.uppercase() }
    }

    fun str(string: String):Boolean {
        return string.trim() != ""
    }

    private fun timePatternCheck(time: String): Boolean {
        val timePattern = """^\d{2}:\d{2}$""".toRegex()

        if (time.matches(timePattern)) {
            val (hours, minutes) = time.split(":").map { it.toInt() }
            if (hours in 0..24) {
                if (minutes in 0..59) {
                    return true
                } else {
                     IOManager.send("Неверное количество минут.")
                }
            } else {
                IOManager.send("Неверное количество часов.")
            }
        } else {
            IOManager.send("Неверный формат времени.")
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
                    IOManager.send("Неверное количество минут.")
                }
            } else {
                IOManager.send("Неверное количество часов.")
            }
        } else {
            IOManager.send("Неверный формат времени.")
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
                    IOManager.send("Неверное количество дней в месяце.")
                }
            } else {
                IOManager.send("Месяц должен быть в пределах от 01 до 12.")
            }
        } else {
            IOManager.send("Неверный формат даты.")
        }
        return false
    }

    fun date(date: String): Boolean {
        return datePatternCheck(date)
    }

    fun time(time: String): Boolean{
        return timePatternCheck(time)
    }

    fun zone(zone: String): Boolean {
        return zonePatternCheck(zone)
    }

    fun loc(loc: String): Boolean{
        return ((loc.split(" ")[0].toDoubleOrNull()!=null) &&
                (loc.split(" ")[1].toFloatOrNull()!=null)&&
                (loc.split(" ")[2].toDoubleOrNull()!=null))
    }
}