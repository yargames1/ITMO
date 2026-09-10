.data
.org 0x00
const_n:      .word 10
input_port:   .word 0x80
output_port:  .word 0x84
buffer:       .word 0
buf_size:     .word 0x20

.text
.org 0x32
_start:
read_loop:
    ; Чтение с input_port (0x80)
    load_imm input_port
    load_acc              ; acc = 0x80
    load_acc              ; Читаем символ с 0x80
    store_addr buffer     ; buffer <- символ
    
    ; Проверка: символ == 10?
    load_imm const_n
    load_acc              ; acc = 10
    sub buffer            ; 10 - символ  
    beqz end              ; Если == \n, выход
    
    ; Эхо: символ -> output_port (0x84)
    load_addr buffer      ; acc = символ
    store_addr 0x84       ; ПРЯМАЯ запись в 0x84 (НЕ store_ind!)
    
    jmp read_loop

end:
    halt