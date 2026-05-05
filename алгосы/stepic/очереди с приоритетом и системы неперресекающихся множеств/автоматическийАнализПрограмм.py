n, e, d = map(int, input().split())

flag = True
parent = [_ for _ in range(n)]
rank = [0 for _ in range(n)]

def find(x):
    if parent[x] != x:
        parent[x] = find(parent[x])  # сжатие пути
    return parent[x]

def union(a, b):
    a = find(a)
    b = find(b)

    if a != b:
        if rank[a] < rank[b]:
            parent[a] = b
        else:
            parent[b] = a
            if rank[a] == rank[b]:
                rank[a] += 1

# Запись i, j чисел в 1 группу
for _ in range(e):
    i, j = map(int, input().split())
    union(i - 1, j - 1)


# Проверка p, q чисел на разные группы
for _ in range(d):
    p, q = map(int, input().split())
    if find(p-1) == find(q-1):
        flag = False

print(1 if flag else 0)