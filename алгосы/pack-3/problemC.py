line = input()
stack = []
problems = 0
for i in range(len(line)):
    if line[i] in "([{<":
        stack.append(line[i])
        continue
    if stack == []:
        print("Impossible")
        exit(0)
    if (line[i] == ")" and "(" == stack[-1]) or (line[i] == "}" and "{" == stack[-1]) or (line[i] == "]" and "[" == stack[-1]) or (line[i] == ">" and "<" == stack[-1]):
        stack.pop()
    else:
        problems += 1
        stack.pop()

if len(stack) != 0:
    print("Impossible")
else:
    print(problems)