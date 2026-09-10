    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
max_len:         .word  32
buffer:          .byte  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

    .text
    .org 0x100

_start:
    lui      t0, %hi(input_addr)
    addi     t0, t0, %lo(input_addr)
    lw       t0, 0(t0)

    lui      t1, %hi(output_addr)
    addi     t1, t1, %lo(output_addr)
    lw       t1, 0(t1)

    lw       t2, 0(t0)
    bgt      zero, t2, reverse_bytes__domain_error

    lui      t6, %hi(max_len)
    addi     t6, t6, %lo(max_len)
    lw       t6, 0(t6)
    bgt      t2, t6, reverse_bytes__domain_error

    lui      t3, %hi(buffer)
    addi     t3, t3, %lo(buffer)
    mv       t4, t2


reverse_bytes__read_loop:
    beqz     t4, reverse_bytes__write_loop

    lw       t5, 0(t0)
    sb       t5, 0(t3)
    addi     t3, t3, 1
    addi     t4, t4, -1
    j        reverse_bytes__read_loop

reverse_bytes__write_loop:
    beqz     t2, reverse_bytes__done

    addi     t3, t3, -1
    lb       t5, 0(t3)
    sb       t5, 0(t1)
    addi     t2, t2, -1
    j        reverse_bytes__write_loop

reverse_bytes__domain_error:
    addi     t5, zero, -1
    sw       t5, 0(t1)

reverse_bytes__done:
    halt
