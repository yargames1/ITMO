n, k = map(int, input().split())
a = list(map(int, input().split()))


def can(x):
    # префиксные суммы
    pref = [0] * (n + 1)

    for i in range(1, n + 1):
        if a[i - 1] >= x:
            pref[i] = pref[i - 1] + 1
        else:
            pref[i] = pref[i - 1] - 1

    min_pref = 0

    for r in range(k, n + 1):
        min_pref = min(min_pref, pref[r - k])
        if pref[r] > min_pref:
            return True

    return False


left, right = 1, n
answer = 1

while left <= right:
    mid = (left + right) // 2

    if can(mid):
        answer = mid
        left = mid + 1
    else:
        right = mid - 1

print(answer)