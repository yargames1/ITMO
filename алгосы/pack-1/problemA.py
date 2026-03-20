t = int(input())
for i in range(t):
    n = int(input())
    s = input()
    not_closed_count = 0
    open_count = 0
    for ii in range(n):
        if s[ii] == "(":
            open_count += 1
        if s[ii] == ")":
            if open_count>0:
                open_count-=1
            else:
                not_closed_count += 1
    print(not_closed_count)