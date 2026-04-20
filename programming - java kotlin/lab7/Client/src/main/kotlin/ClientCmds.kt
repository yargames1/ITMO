import java.nio.file.Paths
import java.time.ZonedDateTime
import kotlin.io.path.readLines

object ClientCmds {
    fun prepare(msg: String): String{
        val tokens = msg.split(" ")

        if (tokens.size == 2)
        {
            return collectData()
        }
        else{
            IOManager.send("Введите команду верно - [название команды] [аргумент]")
            return ""
        }

    }

    private fun readValidatedInput(prompt: String, validator: (String) -> Boolean, errorMessage: String, retryMessage: String): String {
        IOManager.send(prompt)
        IOManager.newString()
        var input = IOManager.read() ?: ""
        while (!validator(input)) {
            IOManager.send(retryMessage)
            IOManager.newString()
            input = IOManager.read() ?: ""
        }
        return input
    }
    private fun collectData() : String{
        val name = readValidatedInput(
            "Введите имя работы", Validator::str,
            "Неверно указано имя работы, создание объекта прервано",
            "Некорректное имя, попробуйте снова."
        )

        val coord = readValidatedInput(
            "Введите координаты работы в формате x y", Validator::coords,
            "Неверно указаны координаты работы, создание объекта прервано",
            "Ошибка в формате координат. Введите снова (пример: 10 20)."
        )
        val coords = coord.split(" ").map { it.toInt() }



        val minimalPoint = readValidatedInput(
            "Введите минимальную оценку за работу", Validator::longMoreThanZero,
            "Неверно указана минимальная оценка за работу, создание объекта прервано",
            "Введите число больше 0."
        ).toLong()

        val personalQualitiesMinimum = readValidatedInput(
            "Введите минимальную оценку за работу, приемлемую для ученика", Validator::longMoreThanZero,
            "Неверно указана минимальная оценка за работу, приемлемая для ученика, создание объекта прервано",
            "Число должно быть больше 0. Попробуйте снова."
        ).toLong()

        val difficulty = Difficulty.valueOf(
            readValidatedInput(
                "Введите уровень сложности работы из указанных: EASY, NORMAL, VERY_HARD, HOPELESS", Validator::enum,
                "Неверно указан уровень сложности работы, создание объекта прервано",
                "Ошибка. Введите один из предложенных уровней сложности: EASY, NORMAL, VERY_HARD, HOPELESS."
            ).uppercase()
        )

        val authorName = readValidatedInput(
            "Введите имя автора работы", Validator::str,
            "Неверно указано имя автора работы, создание объекта прервано",
            "Имя не должно быть пустым. Попробуйте снова."
        )

        val birthDate = readValidatedInput(
            "Введите дату рождения автора (формат: 2006.04.13)", Validator::date,
            "Неверно указана дата рождения автора, создание объекта прервано",
            "Ошибка в формате даты. Введите снова (пример: 2006.04.13)."
        )

        val birthTime = readValidatedInput(
            "Введите время рождения автора (формат: 10:24)", Validator::time,
            "Неверно указано время рождения автора, создание объекта прервано",
            "Ошибка в формате времени. Введите снова (пример: 10:24)."
        )

        val zone = readValidatedInput(
            "Введите часовой пояс места рождения автора (формат: +01:00)", Validator::zone,
            "Неверно указан часовой пояс места рождения автора, создание объекта прервано",
            "Ошибка в формате часового пояса. Введите снова (пример: +01:00)."
        )

        val birthday = ZonedDateTime.parse("${birthDate.replace(".", "-")}T${birthTime}$zone")

        val height = readValidatedInput(
            "Введите высоту автора", Validator::doubleMoreThanZero,
            "Неверно указана высота автора, создание объекта прервано",
            "Введите число больше 0."
        ).toDouble()

        val weight = readValidatedInput(
            "Введите вес автора", Validator::doubleMoreThanZero,
            "Неверно указан вес автора, создание объекта прервано",
            "Введите число больше 0."
        ).toDouble()

        val locationInput = readValidatedInput(
            "Введите координаты автора в формате x y z", Validator::loc,
            "Неверно указаны координаты автора, создание объекта прервано",
            "Ошибка в формате координат. Введите снова (пример: 10.5 20.3 30.7)."
        )
        val loc = locationInput.split(" ")
        val data = listOf(name, coords.joinToString(" "), minimalPoint.toString(), personalQualitiesMinimum.toString(),
            difficulty.toString(), authorName, birthday.toString(), height.toString(), weight.toString(),
            loc.joinToString(" ")).joinToString(" ")
        return data
    }
    fun perpareText(msg: String):String{
        val tokens = msg.split(" ")
        if (tokens.size == 2) {
            return collectText(tokens[1])
        }
        IOManager.send("Введите команду верно - execute_script filename, где filename - имя файла")
        return ""
    }
    private fun collectText(file: String):String{
        try {
            IOManager.opendFiles.add(file)
            val path = Paths.get("files", file)
            val text = mutableListOf("")

            for (line in path.readLines()) {
                val trimmed = line.trim()
                val parts = trimmed.split(" ")
                if (parts.firstOrNull() == "execute_script" && parts.size > 1) {
                    val cmdFile = parts[1]
                    if (IOManager.opendFiles.contains(cmdFile)) {
                        continue // пропустить повтор
                    } else {
                        IOManager.opendFiles.add(cmdFile)
                    }
                }
                text.add(line)
            }
            IOManager.addText(text.drop(1))
            return text[0]
        }
        catch (e: Exception){
            IOManager.send("Не могу найти файл по пути ${e.message.toString()}")
            return ""
        }
    }
}