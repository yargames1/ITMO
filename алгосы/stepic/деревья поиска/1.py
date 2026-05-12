n = int(input())

tree = []
for i in range(n):
    key, l_i, r_i = map(int, input().split())
    tree.append([key, l_i, r_i])

ans_in_order = []
ans_pre_order = []
ans_post_order = []

# in-order: left -> key -> right
def in_order(v):
    if v == -1:
        return 
    in_order(tree[v][1])
    ans_in_order.append(tree[v][0])
    in_order(tree[v][2])
# pre-order: key -> left -> right
def pre_order(v):
    if v == -1:
        return 
    ans_pre_order.append(tree[v][0])
    pre_order(tree[v][1])
    pre_order(tree[v][2])
    # post-order: left -> right -> key
def post_order(v):
    if v == -1:
        return 
    post_order(tree[v][1])
    post_order(tree[v][2])
    ans_post_order.append(tree[v][0])

in_order(0)
pre_order(0)
post_order(0)

print(*ans_in_order)
print(*ans_pre_order)
print(*ans_post_order)