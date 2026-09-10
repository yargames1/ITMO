.text
_start:

addi t0, zero, 0x80
addi t1, zero, 0x84

lw  t4, t0
ble t4, zero, error

lw t3, t0
addi t4, t4, -1

loop:
beqz t4, end
lw  a0, t0
bgt a0, t3, change

after_compare:
addi t4, t4, -1
j loop

change:
mv t3, a0
j after_compare

error:
addi t3, zero, -1

end:
sw t3, t1
halt
