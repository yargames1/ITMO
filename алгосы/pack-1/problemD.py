l = int(input())
for i in range(l):
    n = int(input())
    line = map(int, input().split())
    multipliers = {}
    for x in line:
        i = 2
        while i*i <= x:
            if x%i==0:
                key = multipliers.get(i)
                if key!=None:
                    multipliers[i] += 1
                else:
                    multipliers[i] = 1
                x /= i
            else:
                i += 1
        if x > 1:
            key = multipliers.get(x)
            if key!=None:
                multipliers[x] += 1
            else:
                multipliers[x] = 1

    last = -1
    flag = "yes"
    for val in multipliers.values():
        if val%n!=0:
            flag = "no"
            break

    print(f"{flag}")
            