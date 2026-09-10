y = 1
x = 0
h = 0.1


for i in range(10):
    f = y-(2*x/y)
    y = y + h*f
    x += h
    print(f,y)