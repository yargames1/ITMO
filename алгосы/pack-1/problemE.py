import itertools

n, k = map(int, input().split())
nums = [input().strip() for line in range(n)]

s = 10**18
for p in itertools.permutations(range(k)):
    l = []
    for num in nums:
        new = int("".join(num[i] for i in p))
        l.append(new)
    s = (max(l)-min(l)) if (max(l)-min(l)) < s else s

print(s)
