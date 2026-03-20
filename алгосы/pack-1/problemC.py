line = input().split()
n = int(line[-1])
pos = 1
neg = 0
order = [1]
for i in line:
    if i == "=": break
    if i == "+": 
        pos += 1
        order.append(1)
    elif i == "-": 
        neg += 1
        order.append(-1)

# Проверка на реальность
if not ((pos-n*neg) <= n <= (n*pos-neg)):
    print("Impossible")
    exit(0)
print("Possible")

# Магия
s = 0
k = neg+pos
for i in range(k):
    sign = order[i]
    if sign == 1: pos -= 1
    if sign == -1: neg -= 1

    for x in range(1, n+1):
        if ((s+x*sign+pos-n*neg) <= n <= (s+x*sign+n*pos-neg)):
            s += x*sign
            line[line.index("?")] = x
            break

print(" ".join(map(str, line)))