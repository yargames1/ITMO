name, surname = input().split()

name_prefix = name[0]

for i in range(1, len(name)):
    if name[i] >= surname[0]:
        break
    name_prefix += name[i]

print(name_prefix + surname[0])