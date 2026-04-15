
.data
.org 0x20
aux_wptr:   .word 0x100
len:        .word 0x00
tmp:        .word 0x00
lane:       .word 0x00
waddr:      .word 0x00
shift_v:    .word 0x00
buf_wptr:   .word 0x00
aux_rptr:   .word 0x00
byte_val:   .word 0x00
cstr_end:   .word 0xFFFF
aux_fptr:   .word 0x00
const_4:    .word 4
const_8:    .word 8
const_2:    .word 2
const_1:    .word 1
const_0A:   .word 0x0A
const_1F:   .word 0x1F
const_FF:   .word 0xFF
const_5F5F: .word 0x5F5F5F5F
const_CC:   .word 0xCCCCCCCC
const_FFFF: .word 0xFFFF

.text
.org 0x200

_start:
    load_addr const_5F5F
    store_addr 0x00
    store_addr 0x04
    store_addr 0x08
    store_addr 0x0C
    store_addr 0x10
    store_addr 0x14
    store_addr 0x18
    store_addr 0x1C
    load_imm 0x100
    store_addr aux_wptr
    load_imm 0x00
    store_addr len
    load_imm 0xFFFF
    store_addr cstr_end

; read chars, detect first \0 
read_loop:
    load_addr len
    sub const_1F
    bgt overflow

    load_imm 0x80
    load_acc
    sub const_0A
    beqz pass1_done
    add const_0A
    store_addr byte_val

    store_ind aux_wptr
    load_addr aux_wptr
    add const_4
    store_addr aux_wptr

    ; if byte_val == 0 and cstr_end == 0xFFFF: cstr_end = len
    load_addr byte_val
    bnez no_zero
    load_addr cstr_end
    sub const_FFFF
    bnez no_zero
    load_addr len
    store_addr cstr_end
no_zero:
    load_addr len
    add const_1
    store_addr len
    jmp read_loop

pass1_done:
    ; if cstr_end == 0xFFFF (no \0 found): cstr_end = len
    load_addr cstr_end
    sub const_FFFF
    bnez pass2a_setup
    load_addr len
    store_addr cstr_end

; etup 
pass2a_setup:
    load_imm 0x00
    store_addr buf_wptr

    load_addr cstr_end
    beqz write_null_cstr

    ; aux_rptr = aux_base + (cstr_end-1)*4
    ; = 0x100 + cstr_end*4 - 4
    load_addr cstr_end
    shiftl const_2              ; acc = cstr_end * 4
    store_addr aux_rptr
    load_imm 0x100
    add aux_rptr                ; acc = 0x100 + cstr_end*4
    sub const_4                 ; acc = 0x100 + (cstr_end-1)*4
    store_addr aux_rptr

    ; aux_fptr = 0x100 + cstr_end*4 (points to the \0 word)
    load_addr cstr_end
    shiftl const_2
    store_addr aux_fptr
    load_imm 0x100
    add aux_fptr
    store_addr aux_fptr

;reverse cstr, output 
pass2a_loop:
    load_addr aux_rptr
    load_acc
    and const_FF
    store_addr byte_val

    store_addr 0x84

    ; set_byte(buf_wptr, byte_val)
    load_addr buf_wptr
    rem const_4
    store_addr lane
    mul const_8
    store_addr shift_v
    load_addr buf_wptr
    sub lane
    store_addr waddr
    load_addr waddr
    load_acc
    store_addr tmp
    load_imm 0xFF
    shiftl shift_v
    not
    and tmp
    store_addr tmp
    load_addr byte_val
    shiftl shift_v
    or tmp
    store_ind waddr

    load_addr buf_wptr
    add const_1
    store_addr buf_wptr
    load_addr aux_rptr
    sub const_4
    store_addr aux_rptr

    load_addr buf_wptr
    sub cstr_end
    beqz write_null_cstr
    jmp pass2a_loop

; Write \0 at buf[cstr_end] 
write_null_cstr:
    load_addr buf_wptr
    rem const_4
    store_addr lane
    mul const_8
    store_addr shift_v
    load_addr buf_wptr
    sub lane
    store_addr waddr
    load_addr waddr
    load_acc
    store_addr tmp
    load_imm 0xFF
    shiftl shift_v
    not
    and tmp
    store_ind waddr

    ; remaining = len - cstr_end - 1
    load_addr len
    sub cstr_end
    sub const_1
    ble finish

    load_addr buf_wptr
    add const_1
    store_addr buf_wptr

; copy remainder 

    load_addr aux_fptr
    add const_4
    store_addr aux_fptr

pass2b_loop:
    load_addr aux_fptr
    load_acc
    and const_FF
    store_addr byte_val

    ; set_byte(buf_wptr, byte_val)
    load_addr buf_wptr
    rem const_4
    store_addr lane
    mul const_8
    store_addr shift_v
    load_addr buf_wptr
    sub lane
    store_addr waddr
    load_addr waddr
    load_acc
    store_addr tmp
    load_imm 0xFF
    shiftl shift_v
    not
    and tmp
    store_addr tmp
    load_addr byte_val
    shiftl shift_v
    or tmp
    store_ind waddr

    load_addr buf_wptr
    add const_1
    store_addr buf_wptr
    load_addr aux_fptr
    add const_4
    store_addr aux_fptr

    load_addr buf_wptr
    sub len
    beqz write_null_end
    jmp pass2b_loop

write_null_end:
    load_addr buf_wptr
    rem const_4
    store_addr lane
    mul const_8
    store_addr shift_v
    load_addr buf_wptr
    sub lane
    store_addr waddr
    load_addr waddr
    load_acc
    store_addr tmp
    load_imm 0xFF
    shiftl shift_v
    not
    and tmp
    store_ind waddr
    jmp finish
overflow:
    load_addr const_CC
    store_addr 0x84
finish:
    halt
