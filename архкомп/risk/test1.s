
.data

.text
_start:
addi t0, zero, 0x80
addi t1, zero, 0x84
loop:
lw  a1, t0
beqz a1, print_last
lw  a2, t0
beqz a2, print_pre_last
sb a2, t1
sb a1, t1
j loop

print_pre_last:
sb a1, t1

print_last:
sb zero, t1
halt