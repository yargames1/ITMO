import sys

n = int(input())
sys.setrecursionlimit(max(2*n, 100))
tree = []
for i in range(n):
    key, l_i, r_i = map(int, input().split())
    tree.append([key, l_i, r_i])


def check(v, mn, mx):
    if v == -1:
        return True

    key, l, r = tree[v]

    if not (mn < key < mx):
        return False

    return check(l, mn, key) and check(r, key, mx)

if n > 0:
    if check(0, float('-inf'), float('inf')):
        print("CORRECT")
    else:
        print("INCORRECT")
else:
    print("CORRECT")