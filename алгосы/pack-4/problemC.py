from collections import deque

with open('input.txt', 'r') as f:
    data = f.read().split()

it = iter(data)
n = int(next(it))
m = int(next(it))
k = int(next(it))

# Одномерный bytearray вместо списка списков. 
# Занимает ровно N*M байт (~4 МБ при 2000x2000), что критически экономит память.
grid = bytearray(n * m)
q = deque()

for _ in range(k):
    r = int(next(it)) - 1
    c = int(next(it)) - 1
    grid[r * m + c] = 1
    q.append((r, c))
    
# Если в очереди есть элементы, запоминаем начальные координаты
last_r, last_c = q[0][0], q[0][1]

directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

while q:
    r, c = q.popleft()
    last_r, last_c = r, c  # Последний извлечённый элемент загорится позже всех
    
    for dr, dc in directions:
        nr, nc = r + dr, c + dc
        if 0 <= nr < n and 0 <= nc < m:
            idx = nr * m + nc
            if grid[idx] == 0:
                grid[idx] = 1
                q.append((nr, nc))
                
with open('output.txt', 'w') as f:
    f.write(f"{last_r + 1} {last_c + 1}\n")