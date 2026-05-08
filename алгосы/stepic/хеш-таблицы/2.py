p = 1_000_000_007
x = [1, 263]
m = int(input())
store = [[] for _ in range(m)]
def h(s):
    summ = 0
    for i in range(len(s)):
        if len(x) <= i:
            x.append(x[-1]*263 % p)
        summ = (summ + ord(s[i])*x[i]) % p
    return summ % m

n = int(input())
for line in range(n):
    task, param = input().split()
    match task:
        case "add":
            hassFunc = h(param)
            if param in store[hassFunc]:
                pass
            else:
                store[hassFunc].insert(0, param)
        case "del":
            hassFunc = h(param)
            if param in store[hassFunc]:
                store[hassFunc].remove(param)
        case "find":
            hassFunc = h(param)
            if param in store[hassFunc]:
                print("yes")
            else:
                print("no")
        case "check":
            if store[int(param)] != []:
                print(" ".join(store[int(param)]))
            else:
                print("")
        case _:
            pass