n, k = map(int, input().split())
rank = list(map(int, input().split()))
parent = [i for i in range(n)]
m = max(rank)

def find(i):
    while i != parent[i]:
        i = parent[i]
    return i

for _ in range(k):
    i, j = map(int, input().split())
    i-=1
    j-=1
    i_id = find(i)
    j_id = find(j)
    if i_id == j_id:
        print(m)
        continue
    if rank[i_id] > rank[j_id]:
        parent[j_id] = i_id
        rank[i_id] += rank[j_id]
        m = max(m, rank[i_id])
    else:
        parent[i_id] = j_id
        rank[j_id] += rank[i_id]
        m = max(m, rank[j_id])
    print(m)
    