import heapq

n, m = map(int, input().split())
tasks = list(map(int, input().split()))

# (время освобождения, номер процессора)
heap = [(0, i) for i in range(n)]
heapq.heapify(heap)

for task in tasks:
    time, proc_id = heapq.heappop(heap)
    print(proc_id, time)
    heapq.heappush(heap, (time + task, proc_id))