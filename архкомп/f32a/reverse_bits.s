    .data

input_addr:      .word  0x80
output_addr:     .word  0x84

n_value:         .word  0
result_value:    .word  0

    .text
    .org 0x100


_start:
    read_input

    reverse_bits

    write_result

    halt

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

read_input:
    @p input_addr            \ dataStack.push(mem[input_addr])  [0x80] []
    a!                       \ A = 0x80 [] []
    @                        \ dataStack.push(mem[A])  [n] []

    !p n_value               \ n_value = n [] []
    ;

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

reverse_bits:
    0                        \ dataStack.push(0)  [0] []
    !p result_value          \ result = 0  [] []

    31                       \ [31] []
    >r                       \ returnStack.push(dataStack.pop())  [] [31]
    
reverse_loop:
                             \ while iteration_number >= 0
    @p result_value          \ [result_value] [iteration_number]
    2*                       \ dataStack.push(dataStack.pop() << 1)  [result_value << 1] [31]

    @p n_value               \ [n, result_value << 1] [31]
    1                        \ [1, n, result_value << 1] [31]
    and                      \ [1 & n, result_value << 1] [31]

    +                        \ [1 & n + result_value << 1] [31]  - аналог result |= n&1
    !p result_value          \ result = 1 & n + result_value << 1  [] [31]

    @p n_value               \ [n] [31]
    2/                       \ dataStack.push(dataStack.pop() >> 1)  [n>>1] [31]
    !p n_value               \ n_value = n>>1  [][31]

    next reverse_loop        \ if R != 0 => R < R - 1; p = reverse_loop else returnStack.pop()
                             \ [] []
    ;

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

write_result:
    @p result_value          \ [result_value] []
    @p output_addr           \ [0x84, result_value] []
    a!                       \ A = 0x84 [result_value] []
    !                        \ mem[0x84] <- result_value [] []
    ;
