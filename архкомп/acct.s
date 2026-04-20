.data
.org 0x00
buffer:       .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0

tmp:          .word 0
ptr:          .word 0
flagis_upper_flag: .word 1 ; По умолчанию включен

const_n:      .word 10
const_0:      .word  0
const_1:      .word  1
const_5f:     .word 0x5F
buf_size:     .word 0x20

;Заглавные A-Z: 0x41-0x5A (65-90)
;Строчные  a-z: 0x61-0x7A (97-122)
;Разница:    32 (0x20)
const_A:      .word 0x41
const_Z:      .word 0x5A
const_a:      .word 0x61
const_z:      .word 0x7A
const_32:     .word 0x20
const_space:  .word 0x20

input_port:   .word 0x80
output_port:  .word 0x84

.text
.org 0x200
_start:

    load_imm    buffer
    store       ptr       ; ptr <- buffer

read_loop:
    sub     buf_size
    beqz         end  
    
    ; Чтение с input_port (0x80)
    load input_port
    load_acc              
    store_addr tmp        ; buffer <- символ
    
    ; Проверка: символ == 10?
    load_addr  const_n     ; acc = 10
    sub tmp               ; 10 - символ
    beqz n              ; Если == \n, выход

    ; Проверка: символ == пробел?
    load_addr  const_space     ; acc = пробле
    sub tmp               ; 10 - символ
    bnez first_symb             ; Если != пробел, пропуск
    load_addr   const_1
    store      flagis_upper_flag ; обновляем флаг

    
first_symb:
    ;Проверка на первый символ слова
    load_addr flagis_upper_flag ; acc = flag
    beqz another_symb ; Если не флаг поднят то прыгаем - это не начало слова

    ;Проверка регистра
    load_addr tmp ; acc = символ
    sub const_a ; если больше 0, то после (или он и есть) строчной а
    ble flag_off ; меньше строчной а, все хорошо, не правим (либо заглавный, либо не буква)
    add const_a
    sub const_z ; если меньше 0, то точно строчная (между a и z)
    bgt flag_off ; больше, видимо другой какой-то (не буква)

    ; если мы оказались здесь, значит символ - строчная бкува, принимаемся за исправление
    load_addr tmp ;acc = tmp = символ
    sub const_32 ; символ -> СИМВОЛ
    store_addr tmp ; tmp = СИМВОЛ

    jmp flag_off

another_symb:
    ; Аналогично, но теперь проверка на строчной

    ;Проверка регистра
    load_addr tmp ; acc = символ
    sub const_A ; если больше 0, то после (или он и есть) загловной A
    ble echo ; меньше загловной A, все хорошо, не правим (либо стьрочной, либо не буква)
    add const_A
    sub const_Z ; если меньше 0, то точно строчная (между A и Z)
    bgt echo ; больше, видимо другой какой-то (не буква)
    
    ; если мы оказались здесь, значит символ - загловной бкува, принимаемся за исправление
    load_addr tmp ; acc = tmp = символ
    add const_32 ; СИМВОЛ -> символ
    store_addr tmp ; tmp = символ

    jmp echo

flag_off:
    load_addr const_0 
    store_addr flagis_upper_flag

echo:
    ; Эхо: символ -> output_port (0x84)
    load_addr tmp         ; acc = символ
    store_ind output_port ; запись в 0x84
    store_ind ptr   ; записть в буфер(1 ячейку)

    load         ptr
    add          const_1
    store        ptr                           ;     ptr <- ptr + const_1

    jmp read_loop

n:
    load_addr const_0
    store_ind ptr

    load         ptr
    add          const_1
    store        ptr                           ;     ptr <- ptr + const_1

fill_5f:
    sub     buf_size
    beqz         end  

    load_addr const_5f
    store_ind ptr

    load         ptr
    add          const_1
    store        ptr                           ;     ptr <- ptr + const_1

    jmp fill_5f


end:
    halt
