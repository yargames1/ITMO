from collections import deque
size, n = map(int, input().split())
results = []
buffer = deque()

for i in range(n):
    arrival, duration = list(map(int, input().split()))
    while buffer and buffer[0] <= arrival:
        buffer.popleft()
    c_size = len(buffer)
    if c_size < size:
        if c_size > 0:
            start_time = buffer[-1]
        else:
            start_time = arrival
        results.append(start_time)
        buffer.append(start_time+duration)
    else:
        results.append(-1)

print("\n".join(map(str, results)))