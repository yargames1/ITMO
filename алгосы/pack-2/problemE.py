n, I = map(int, input().split())
a = list(map(int, input().split()))

# максимум бит на элемент
bits = (8 * I) // n

# если можем хранить всё
if bits > 20:
    print(0)
    exit()

# максимум различных значений
max_k = 1 << bits

# считаем частоты
from collections import Counter
cnt = Counter(a)

# сортируем уникальные значения
vals = sorted(cnt.keys())
freq = [cnt[v] for v in vals]

m = len(vals)

# если можем оставить все
if max_k >= m:
    print(0)
    exit()

# sliding window
l = 0
cur_sum = 0
max_keep = 0

for r in range(m):
    cur_sum += freq[r]

    # окно не больше max_k
    while r - l + 1 > max_k:
        cur_sum -= freq[l]
        l += 1

    max_keep = max(max_keep, cur_sum)

# сколько удалим
print(n - max_keep)