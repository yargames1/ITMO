pattern = input()
text = input()
t = len(text)
p = len(pattern)
P = 1_000_000_007
p_hash = 0
x = [1]
h = [-1 for _ in range(t-p+1)]
h_last = 0
for i in range(p):
    x.append(x[-1]*263 % P)
    h_last = (h_last + ord(text[t-p+i])*x[i] % P) % P
    p_hash = (p_hash + ord(pattern[i]) *x[i] % P) % P
h[-1] = h_last
for i in range(t-p-1, -1, -1):
    h[i] = ((h[i+1] - ord(text[i+p]) * x[p-1] % P ) * x[1] % P + ord(text[i])) % P

for i in range(t-p+1):
    if h[i] == p_hash:
        if text[i:i+p] == pattern:
            print(i, end=" ")
