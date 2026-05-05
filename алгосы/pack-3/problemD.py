import sys
from collections import Counter


# Быстрый ввод всех данных разом
data = sys.stdin.read().split()
it = iter(data)

t = int(next(it))
out = []

for _ in range(t):
    n = int(next(it))
    a = [int(next(it)) for _ in range(n)]
    b = [int(next(it)) for _ in range(n)]
    
    # Считаем частоты каждого числа
    cnt_a = Counter(a)
    cnt_b = Counter(b)
    ops = 0
    
    # Частоты для однозначных чисел (индексы 1..9)
    freq_a = [0] * 10
    freq_b = [0] * 10
    
    # Переносим исходные 1..9 в частотные массивы
    for v in range(1, 10):
        freq_a[v] = cnt_a.get(v, 0)
        freq_b[v] = cnt_b.get(v, 0)
        
    # Обрабатываем большие числа (>= 10)
    # Берём объединение ключей, чтобы не пропустить ни одно число
    large_nums = set(k for k in cnt_a.keys() | cnt_b.keys() if k >= 10)
    
    for v in large_nums:
        ca = cnt_a.get(v, 0)
        cb = cnt_b.get(v, 0)
        match = min(ca, cb)
        
        # То, что не совпало, обязательно нужно трансформировать
        rem_a = ca - match
        rem_b = cb - match
        
        if rem_a > 0:
            ops += rem_a          # 1 операция за каждое число
            freq_a[len(str(v))] += rem_a
        if rem_b > 0:
            ops += rem_b
            freq_b[len(str(v))] += rem_b
            
    # Ищем совпадения среди однозначных чисел (1..9)
    for v in range(1, 10):
        match = min(freq_a[v], freq_b[v])
        freq_a[v] -= match
        freq_b[v] -= match
        
    # Всё, что осталось > 1, превратится в 1 за 1 операцию.
    # Единицы трансформировать не нужно.
    for v in range(2, 10):
        ops += freq_a[v] + freq_b[v]
        
    out.append(str(ops))
    
sys.stdout.write('\n'.join(out) + '\n')

