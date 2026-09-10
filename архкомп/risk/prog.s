    .data

stack_top:       .word  0x90
input_port:      .word  0x80
output_port:     .word  0x84
stack_slot_size: .word  4
one:             .word  1

    .text
    .org     0x100

_start:
    ; загрузим константы в регистры
    ; адрес вывода
    lui      t0, %hi(output_port)
    addi     t0, t0, %lo(output_port)
    lw       t0, t0
    ; адрес ввода в t3
    lui      t3, %hi(input_port)
    addi     t3, t3, %lo(input_port)
    lw       t3, t3
    ; конст 1 в t6
    lui      t6, %hi(one)
    addi     t6, t6, %lo(one)
    lw       t6, t6
    ; размер ячейки стека в t5
    lui      t5, %hi(stack_slot_size)
    addi     t5, t5, %lo(stack_slot_size)
    lw       t5, t5
    ; значение вершины стека в sp
    lui      sp, %hi(stack_top)
    addi     sp, sp, %lo(stack_top)
    lw       sp, sp

    ; запускаем сумматор
    jal      ra, sum_stream
    ; должны получить a0 - старшие, a1 - младшие биты

    ; пишем ответ
    sw       a0, t0
    sw       a1, t0

    halt

sum_stream:

    ; выделим в стеке под него место, сохраним адрес возврата, в t5 размер ячейки стека
    sub      sp, sp, t5
    sw       ra, sp

    ; вход в t3

    ; запишем 1 слово, это количество слов
    lw       t4, t3

    ; в а0 и а1 закинем нули
    mv       a0, zero
    mv       a1, zero

    ; если количество слов меньше или равно 0, то не суммируем
    ble      t4, zero, sum_stream_end

sum_stream_loop:
    ; пишем слово
    lw       a2, t3
    ; добавляем его к сумме
    jal      ra, add_word_to_acc

    ; уменьшим размер оставшихся слов для суммирования
    sub      t4, t4, t6
    ; пока не 0, продолжаем
    bnez     t4, sum_stream_loop

sum_stream_end:
    ; закончили писать, надо вернуться

    ; восстанавливаем адрес возврата
    lw       ra, sp
    ; освобождаем место на стеке, в t5 размер ячейки стека
    add      sp, sp, t5
    ; возвращаемся обратно
    jr       ra

add_word_to_acc:
    ; доп слово на a2

    ; сохраним старое значение младшего слова в t1
    mv       t1, a1
    ; добавим к младшей части новое слово
    add      a1, a1, a2
    ; если отрицательно, надо учесть знаковое расширение в старшей части
    bgt      zero, a2, add_word_negative

add_word_carry:
    ; сумма готова, надо перенос проверить
    ; если новое меньше старого, то перенос есть
    ; в t1 старое лежит, в a1 новое
    bgtu     t1, a1, add_word_with_carry
    ; вернемся обратно, на выход из add_word_to_acc
    jr       ra

add_word_negative:
    ; число отрицательное
    ; в 64 битном представлении старшие биты с 1, надо уменьшить старшую сумму на 1
    ; в t6 лежит 1
    sub      a0, a0, t6
    ; выполняем проверку переноса
    j        add_word_carry

add_word_with_carry:
    ; переполнение есть, к старшем битам добавим 1, в t6 лежит
    add      a0, a0, t6
    ; возврат из add_word_to_acc
    jr       ra