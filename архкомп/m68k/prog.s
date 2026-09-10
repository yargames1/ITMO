    .data

io_in_ptr:      .word  0x80
io_out_ptr:     .word  0x84
scan_buf:       .byte  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

    .text
    .org     0x200

_start:
    movea.l  io_in_ptr, A0
    movea.l  (A0), A0
    movea.l  io_out_ptr, A2
    movea.l  (A2), A2
    movea.l  0x1000, A7
    movea.l  0x0c00, A5
    clr.l    D6
    clr.l    D5

    jsr      load_line
    cmp.l    0, D5
    bne      emit_status

    jsr      calculate

emit_status:
    cmp.l    0, D5
    beq      emit_value
    cmp.l    2, D5
    beq      emit_overflow

emit_error:
    move.l   -1, D0
    jmp      emit_value

emit_overflow:
    move.l   0xcccccccc, D0

emit_value:
    move.l   D0, (A2)
    halt

load_line:
    link     A6, 0
    movea.l  scan_buf, A3
    clr.l    D5
    clr.l    D7

load_line_next:
    cmp.l    64, D7
    bge      load_line_too_long
    move.l   (A0), D0
    cmp.l    10, D0
    beq      load_line_stop
    move.b   D0, (A3)+
    add.l    1, D7
    jmp      load_line_next

load_line_stop:
    clr.l    D0
    move.b   D0, (A3)
    unlk     A6
    rts

load_line_too_long:
    move.l   2, D5
    unlk     A6
    rts

calculate:
    link     A6, 0
    movea.l  scan_buf, A1
    movea.l  0x0c00, A5
    clr.l    D5
    clr.l    D6

calc_next:
    jsr      pass_spaces
    clr.l    D0
    move.b   (A1), D0
    cmp.l    0, D0
    beq      calc_finish
    jsr      consume_token
    cmp.l    0, D5
    bne      calc_return
    jmp      calc_next

calc_finish:
    cmp.l    1, D6
    bne      calc_bad
    move.l   -(A5), D0
    sub.l    1, D6
    clr.l    D5
    jmp      calc_return

calc_bad:
    move.l   1, D5

calc_return:
    unlk     A6
    rts

pass_spaces:
    link     A6, 0

pass_spaces_loop:
    clr.l    D0
    move.b   (A1), D0
    cmp.l    ' ', D0
    bne      pass_spaces_done
    clr.l    D0
    move.b   (A1)+, D0
    jmp      pass_spaces_loop

pass_spaces_done:
    unlk     A6
    rts

consume_token:
    link     A6, 0
    clr.l    D5
    clr.l    D0
    move.b   (A1), D0

    cmp.l    '+', D0
    beq      maybe_plus
    cmp.l    '-', D0
    beq      maybe_minus
    cmp.l    '*', D0
    beq      only_operator
    cmp.l    '/', D0
    beq      only_operator

    cmp.l    '0', D0
    blt      token_bad
    cmp.l    '9', D0
    bgt      token_bad
    jmp      token_number

maybe_plus:
    jsr      next_is_digit
    cmp.l    1, D5
    beq      token_number
    jsr      next_is_end
    cmp.l    0, D5
    bne      token_done
    jmp      token_operator

maybe_minus:
    jsr      next_is_digit
    cmp.l    1, D5
    beq      token_number
    jsr      next_is_end
    cmp.l    0, D5
    bne      token_done
    jmp      token_operator

only_operator:
    jsr      next_is_end
    cmp.l    0, D5
    bne      token_done
    jmp      token_operator

token_operator:
    clr.l    D4
    move.b   (A1)+, D4
    jsr      run_operator
    jmp      token_done

token_number:
    jsr      read_integer
    cmp.l    0, D5
    bne      token_done
    move.l   D0, (A5)+
    add.l    1, D6
    jmp      token_done

token_bad:
    move.l   1, D5

token_done:
    unlk     A6
    rts

next_is_digit:
    link     A6, 0
    clr.l    D5
    clr.l    D3
    move.l   1, D3
    clr.l    D2
    move.b   0(A1,D3), D2
    cmp.l    '0', D2
    blt      next_is_digit_done
    cmp.l    '9', D2
    bgt      next_is_digit_done
    move.l   1, D5

next_is_digit_done:
    unlk     A6
    rts

next_is_end:
    link     A6, 0
    clr.l    D5
    clr.l    D3
    move.l   1, D3
    clr.l    D2
    move.b   0(A1,D3), D2
    cmp.l    0, D2
    beq      next_is_end_done
    cmp.l    ' ', D2
    beq      next_is_end_done
    move.l   1, D5

next_is_end_done:
    unlk     A6
    rts

read_integer:
    link     A6, 0
    clr.l    D0
    clr.l    D3
    clr.l    D4
    clr.l    D5

    clr.l    D1
    move.b   (A1), D1
    cmp.l    '+', D1
    beq      int_skip_plus
    cmp.l    '-', D1
    beq      int_skip_minus
    jmp      int_digits

int_skip_plus:
    clr.l    D1
    move.b   (A1)+, D1
    jmp      int_digits

int_skip_minus:
    move.l   1, D4
    clr.l    D1
    move.b   (A1)+, D1

int_digits:
    clr.l    D1
    move.b   (A1), D1
    cmp.l    0, D1
    beq      int_finish
    cmp.l    ' ', D1
    beq      int_finish
    cmp.l    '0', D1
    blt      int_bad
    cmp.l    '9', D1
    bgt      int_bad

    clr.l    D1
    move.b   (A1)+, D1
    sub.l    '0', D1
    add.l    1, D3

    cmp.l    0, D4
    bne      int_negative

int_positive:
    mul.l    10, D0
    bvs      int_overflow
    add.l    D1, D0
    bvs      int_overflow
    jmp      int_digits

int_negative:
    mul.l    10, D0
    bvs      int_overflow
    sub.l    D1, D0
    bvs      int_overflow
    jmp      int_digits

int_finish:
    cmp.l    0, D3
    beq      int_bad
    clr.l    D5
    jmp      int_return

int_bad:
    move.l   1, D5
    jmp      int_return

int_overflow:
    move.l   2, D5

int_return:
    unlk     A6
    rts

run_operator:
    link     A6, 0
    clr.l    D5
    cmp.l    2, D6
    blt      op_bad

    move.l   -(A5), D1
    sub.l    1, D6
    move.l   -(A5), D0
    sub.l    1, D6

    cmp.l    '+', D4
    beq      op_add
    cmp.l    '-', D4
    beq      op_sub
    cmp.l    '*', D4
    beq      op_mul
    cmp.l    '/', D4
    beq      op_div
    jmp      op_bad

op_add:
    add.l    D1, D0
    bvs      op_overflow
    jmp      op_push

op_sub:
    sub.l    D1, D0
    bvs      op_overflow
    jmp      op_push

op_mul:
    mul.l    D1, D0
    bvs      op_overflow
    jmp      op_push

op_div:
    cmp.l    0, D1
    beq      op_bad
    cmp.l    0x80000000, D0
    bne      op_div_exec
    cmp.l    -1, D1
    beq      op_overflow

op_div_exec:
    div.l    D1, D0
    jmp      op_push

op_push:
    move.l   D0, (A5)+
    add.l    1, D6
    jmp      op_return

op_bad:
    move.l   1, D5
    jmp      op_return

op_overflow:
    move.l   2, D5

op_return:
    unlk     A6
    rts
