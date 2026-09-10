.data
.text
_start:

addi t0, zero, 0x80
addi t1, zero, 0x84

mv t2, zero

loop:
lw a0, t0

beqz a0, end

addi t2, t2, 1

j loop

end:
sw t2, t1
halt