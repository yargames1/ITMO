enum class Stage {
    READ,           // Чтение данных
    PROCESSING,     // Обработка команды (в ForkJoinPool)
    WRITE_RESULT    // Отправка результата
}