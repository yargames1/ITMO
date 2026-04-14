t = int(input())

for _ in range(t):
    sp = input()
    n, m = map(int, input().split())
    line = []
    for i in range(m):
        x, w = map(int, input().split())
        line.append([x, w, i+1])
    line.sort(key=lambda x: x[1])
    segment = line[0:2*n]
    segment.sort(key=lambda x: x[0])
    total_weight = sum(x[1] for x in segment)
    print(total_weight)
    for j in range(n):
        print(segment[j][2], segment[2*n-1-j][2])
    print("")
    