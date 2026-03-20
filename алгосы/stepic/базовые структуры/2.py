n = int(input())
tree = list(map(int,input().split()))
h = 1
new_tree = [[] for e in range(n)]
for i in range(n):
    x = tree[i]
    if x != -1:
        new_tree[x].append(i)

next_layer = [tree.index(-1)]
while True:
    layer = []
    for x in next_layer:
        layer += new_tree[x]
    if layer == []:
        break
    next_layer = layer
    h+=1
print(h)