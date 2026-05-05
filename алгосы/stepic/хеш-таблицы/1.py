n = int(input())
tel = {}

for i in range(n):
    task = list(input().split())
    match task[0]:
        case "add":
            tel[task[1]] = task[2]
        case "find":
            if task[1] not in tel:
                tel[task[1]] = "not found"
            print(tel[task[1]])
        case "del":
            tel[task[1]] = "not found"
        case _:
            pass
