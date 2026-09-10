.data
.text

_start:
addi t0, zero, 0x80
addi t1, zero,  0x84
addi s1, zero, 2

lw t2, t0
bgt t2, zero, continue
error:
addi t3, t3, -1
j end

continue:
beqz t2, nulinfo
mv t3, zero

loop:
beqz t2, end
lw a0, t0
rem a1, a0, s1
beqz a1, update_sum
after_update:
addi t2, t2, -1
j loop

update_sum:
add t3, t3, a0
j after_update

nulinfo:
mv t3, zero

end:
sw t3, t1
halt