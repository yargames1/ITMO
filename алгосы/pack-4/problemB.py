from collections import deque

def bfs_lake_volume(pool, start_i, start_j, n, m):
    if pool[start_i][start_j] == 0:
        return 0

    queue = deque([(start_i, start_j)])
    
    volume = pool[start_i][start_j]
    pool[start_i][start_j] = 0

    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

    while queue:
        r, c = queue.popleft()

        for dr, dc in directions:
            nr, nc = r + dr, c + dc

            if 0 <= nr < n and 0 <= nc < m and pool[nr][nc] > 0:
                volume += pool[nr][nc]
                pool[nr][nc] = 0
                queue.append((nr, nc))

    return volume

t = int(input())

for test in range(t):
    n, m = map(int, input().split())
    pool = []
    for i in range(n):
        pool.append([int(depth) for depth in input().split()])
    max_volume = 0
    for i in range(n):
            for j in range(m):
                current_vol = bfs_lake_volume(pool, i, j, n, m)
                if current_vol > max_volume:
                    max_volume = current_vol
    print(max_volume)