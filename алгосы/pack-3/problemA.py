n = int(input())
names = {}

for _ in range(n):
    name = input()
    
    if name not in names:
        names[name] = 0
        print("OK")
    else:
        names[name] += 1
        new_name = name + str(names[name])
        print(new_name)
        names[new_name] = 0