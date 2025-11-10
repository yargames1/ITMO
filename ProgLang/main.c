#include <stdio.h>
/* Возвращает 1 если a делится на b
  (остаток от деления a на b -- ноль)
   0 если не делится.
*/
int divides(int a, int b) { 
    return a % b == 0; 
}

/* Переводит вывод на новую строку. 
*/
void print_newline() { 
    printf("\n"); 
}

/* Выводит одну строчку: число n, двоеточие и все его делители, большие единицы, до самого n включительно.
Например, для числа 8 это:
"8: 2 4 8 "
В конце этой строчки должен быть пробел.
*/
void divisors(int n) {
    printf("%d: ", n);
    int i;
    for (i = 2; i<=n; i++){
        if (divides(n, i)){printf("%d ", i);}

    }
}

/* Выводит делители для всех чисел от 1 до limit включительно
Каждая строчка -- в формате, заданном функцией divisors.
*/
void all_divisors(int limit) {
    int i;
    for (i = 1; i < limit; i++){
        divisors(i);
        print_newline();
    }
    divisors(i);
}

void array_fib(int* array, int* limit) {
    array[0] = 1;
    array[1] = 1;
    for (int i = 2; i < limit-array; i++){
        array[i] = array[i-2]+array[i-1];
    }
}



int main() {
    int arr[10];
    array_fib(arr, arr + 10);
    for (int i = 0; i < 10; i++) {
    printf("%d ", arr[i]);
}
}
