import heapq

n, d = map(int, input().split())

a = [0] + list(map(int, input().split())) + [0]

x = []
y = []
for i in range(n):
    statoin = list(map(int, input().split()))
    x.append(statoin[0])
    y.append(statoin[1])
    
# Предвычисляем стоимости перемещения
cost = [[0] * n for _ in range(n)]
for i in range(n):
    for j in range(n):
        if i != j:
            dist = abs(x[i] - x[j]) + abs(y[i] - y[j])
            cost[i][j] = dist * d
            
def can_reach(T: int) -> bool:
    # max_time[i] - максимальное время, с которым можно оказаться на станции i
    max_time = [-1] * n
    max_time[0] = T
    # Куча хранит (-time, vertex) для имитации max-heap
    pq = [(-T, 0)]
    
    while pq:
        t_neg, u = heapq.heappop(pq)
        t = -t_neg
        
        if t < max_time[u]:
            continue
        if u == n - 1:
            return True
            
        for v in range(n):
            if u == v:
                continue
            c = cost[u][v]
            if t >= c:
                new_t = t - c + a[v]
                if new_t > max_time[v]:
                    max_time[v] = new_t
                    heapq.heappush(pq, (-new_t, v))
    return False

# Бинарный поиск по ответу
lo, hi = 0, 400_000_000
ans = hi

while lo <= hi:
    mid = (lo + hi) // 2
    if can_reach(mid):
        ans = mid
        hi = mid - 1
    else:
        lo = mid + 1
        
print(ans)
