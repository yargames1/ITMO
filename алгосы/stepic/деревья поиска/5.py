class Node:

    def __init__(self, val):
        self.value = val
        self.left = None
        self.right = None
        self.height = 1
        self.sum = 1


def height(node):
    return node.height if node else 0


def get_sum(node):
    return node.sum if node else 0


def update(node):
    if node:
        node.height = 1 + max(height(node.left), height(node.right))
        node.sum = 1 + get_sum(node.left) + get_sum(node.right)


def get_balance(node):
    return height(node.left) - height(node.right) if node else 0


def right_rotate(y):
    x = y.left
    T2 = x.right

    x.right = y
    y.left = T2

    update(y)
    update(x)

    return x


def left_rotate(x):
    y = x.right
    T2 = y.left

    y.left = x
    x.right = T2

    update(x)
    update(y)

    return y


def insert(node, val):
    if not node:
        return Node(val)

    if node.right == None:
        node.right = Node(val)
    else:
        node.right = insert(node.right, val)

    update(node)

    balance = get_balance(node)

    if balance > 1:
        if key < node.left.key:
            return right_rotate(node)
        else:
            node.left = left_rotate(node.left)
            return right_rotate(node)

    if balance < -1:
        if key > node.right.key:
            return left_rotate(node)
        else:
            node.right = right_rotate(node.right)
            return left_rotate(node)

    return node


def get_max(node):
    while node.right:
        node = node.right
    return node


def delete(node, key):
    if not node:
        return None

    if key < node.key:
        node.left = delete(node.left, key)

    elif key > node.key:
        node.right = delete(node.right, key)

    else:
        if not node.left:
            return node.right

        if not node.right:
            return node.left

        temp = get_max(node.left)

        node.key = temp.key
        node.left = delete(node.left, temp.key)

    update(node)

    balance = get_balance(node)

    if balance > 1:
        if get_balance(node.left) >= 0:
            return right_rotate(node)

        node.left = left_rotate(node.left)
        return right_rotate(node)

    if balance < -1:
        if get_balance(node.right) <= 0:
            return left_rotate(node)

        node.right = right_rotate(node.right)
        return left_rotate(node)

    return node


def find(node, key):
    while node:
        if key == node.key:
            return "Found"

        if key < node.key:
            node = node.left
        else:
            node = node.right

    return "Not found"


def prefix_sum(node, x):
    if not node:
        return 0

    if node.key > x:
        return prefix_sum(node.left, x)

    return (
        get_sum(node.left)
        + node.key
        + prefix_sum(node.right, x)
    )


def range_sum(root, l, r):
    return prefix_sum(root, r) - prefix_sum(root, l - 1)


MOD = 1000000001

s = 0
root = None

n = int(input())

for _ in range(n):
    cmd = input().split()

    if cmd[0] == "+":
        x = (int(cmd[1]) + s) % MOD
        root = insert(root, x)

    elif cmd[0] == "-":
        x = (int(cmd[1]) + s) % MOD
        root = delete(root, x)

    elif cmd[0] == "?":
        x = (int(cmd[1]) + s) % MOD
        print(find(root, x))

    else:
        l = (int(cmd[1]) + s) % MOD
        r = (int(cmd[2]) + s) % MOD

        s = range_sum(root, l, r)
        print(s)