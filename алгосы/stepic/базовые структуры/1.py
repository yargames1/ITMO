line = input()
stack = []
index = []
for i in range(len(line)):
    if line[i] not in "([{}])": continue
    if line[i] in "([{":
        stack.append(line[i])
        index.append(i+1)
        continue
    if len(stack) == 0: 
        print(i+1)
        exit(0)
    if (line[i] == ")" and "(" == stack[-1]) or (line[i] == "}" and "{" == stack[-1]) or (line[i] == "]" and "[" == stack[-1]):
        stack.pop()
        index.pop()
    else:
        print(i+1)
        exit(0)
if len(stack) == 0:
    print("Success")
else:
    print(index[0])