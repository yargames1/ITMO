import re

test1 = '20 + 22 = 42'
test2 = '35 + 12 = 47'
test3 = '20 * 38 = 760'
test4 = '14 - 5 = 9'
test5 = '351 / 9 = 39'


def cypher(message):
    old_integers = re.findall(r'\d+', message)
    new_integers = [3 * int(x) ** 2 + 5 for x in old_integers]
    for i in range(len(old_integers)):
        message = re.sub(old_integers[i], str(new_integers[i]), message)
    return message


print(cypher(test1))
print(cypher(test2))
print(cypher(test3))
print(cypher(test4))
print(cypher(test5))


