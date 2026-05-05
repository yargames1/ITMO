import sys
import bisect

input = sys.stdin.readline

n = int(input())
a = list(map(int, input().split()))

sorted_vals = []
depth = {}

sorted_vals.append(a[0])
depth[a[0]] = 0

result = []

for i in range(1, n):
    x = a[i]

    pos = bisect.bisect_left(sorted_vals, x)

    lower = sorted_vals[pos - 1] if pos > 0 else None
    higher = sorted_vals[pos] if pos < len(sorted_vals) else None

    if lower is None:
        parent = higher
    elif higher is None:
        parent = lower
    else:
        if depth[lower] > depth[higher]:
            parent = lower
        else:
            parent = higher

    result.append(str(parent))

    depth[x] = depth[parent] + 1
    bisect.insort(sorted_vals, x)

print(" ".join(result))