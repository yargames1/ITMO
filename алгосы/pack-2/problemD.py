n = int(input())
a = list(map(int, input().split()))
b = list(map(int, input().split()))

ops = []

for i in range(n):
    # ищем нужный элемент справа
    pos = -1
    for j in range(i, n):
        if b[j] == a[i]:
            pos = j
            break

    # двигаем его влево до позиции i
    for j in range(pos, i, -1):
        # swap b[j] и b[j-1]
        b[j], b[j - 1] = b[j - 1], b[j]
        ops.append((j, j + 1))  # индексация с 1

print(len(ops))
for x, y in ops:
    print(x, y)