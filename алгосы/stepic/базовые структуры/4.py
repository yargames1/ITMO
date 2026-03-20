q = int(input())
stack = []
stack_max = [0]
for i in range(q):
    command = input().split()
    match command:
        case ["max"]:
            print(stack_max[-1])
        case ["pop"]:
            stack.pop(-1)
            stack_max.pop(-1)
        case ["push", i]:
            stack.append(int(i))
            stack_max.append(max(stack_max[-1], int(i)))