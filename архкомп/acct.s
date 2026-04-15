.data
.org 0x00
buffer:       .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0

tmp:          .word 0
ptr:          .word 0

const_n:      .word 10
const_FF:     .word  0xFF
const_0:      .word  0
const_1:      .word  1
const_5f:     .word 0x5F
buf_size:     .word 0x20

input_port:   .word 0x80
output_port:  .word 0x84

.text
.org 0x88
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
    load_imm const_n
    load_acc              ; acc = 10
    sub tmp               ; 10 - символ  
    beqz n              ; Если == \n, выход
    
    ; Эхо: символ -> output_port (0x84)
    load_addr tmp         ; acc = символ
    store_ind output_port ; запись в 0x84
    store_ind ptr   ; записть в буфер(1 ячейку)

    load         ptr
    add          const_1
    store        ptr                           ;     ptr <- ptr + const_1

    jmp read_loop
    
n:
    load_imm const_0
    load_acc
    store_ind ptr

    load         ptr
    add          const_1
    store        ptr                           ;     ptr <- ptr + const_1

fill_5f:
    sub     buf_size
    beqz         end  

    load_imm const_5f
    load_acc
    store_ind ptr

    load         ptr
    add          const_1
    store        ptr                           ;     ptr <- ptr + const_1

    jmp fill_5f


end:
    halt