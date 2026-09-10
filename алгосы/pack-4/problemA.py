import heapq

n, m = map(int, input().split())

graph = [[] for _ in range(n+1)]

for i in range(m):
    a, b, w = map(int, input().split())
    if a == b:
        continue
    graph[a].append([b, w])
    graph[b].append([a, w])

# Инициализация Дейкстры
INF = 10**18
dist = [INF] * (n + 1)
dist[1] = 0
parent = [0] * (n + 1)  # для восстановления пути
pq = [(0, 1)]           # (расстояние, вершина)

while pq:
    d, u = heapq.heappop(pq)
    if d > dist[u]:     # удаление дубликатов
        continue
    for v, w in graph[u]:
        nd = d + w
        if nd < dist[v]:
            dist[v] = nd
            parent[v] = u
            heapq.heappush(pq, (nd, v))

if dist[n] == INF:
    print(-1)
    exit()

# Восстановление пути
path = []
cur = n
while cur != 0:
    path.append(cur)
    cur = parent[cur]

print(" ".join(map(str, path[::-1])) + "\n")