from collections import deque

n = int(input())
a = list(map(int, input().split()))
m = int(input())

in_stack = []
out_stack = []

def push(x):
    if not in_stack:
        in_stack.append((x, x))
    else:
        in_stack.append((x, max(x, in_stack[-1][1])))

def move():
    while in_stack:
        x = in_stack.pop()[0]
        if not out_stack:
            out_stack.append((x, x))
        else:
            out_stack.append((x, max(x, out_stack[-1][1])))

def pop():
    if not out_stack:
        move()
    out_stack.pop()

def get_max():
    if not in_stack:
        return out_stack[-1][1]
    if not out_stack:
        return in_stack[-1][1]
    return max(in_stack[-1][1], out_stack[-1][1])

for i in range(m):
    push(a[i])

print(get_max(), end=" ")

for i in range(m, n):
    push(a[i])
    pop()
    print(get_max(), end=" ")