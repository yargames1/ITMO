import numpy as np

# Общее
def f(param):
    x1, x2 = param
    return 2*x1**2+2*x1*x2+3*x2**2+10*x1-10*x2+35


def numerical_gradient(f, x, h=1e-8):
    grad = np.zeros_like(x, dtype=float)
    for i in range(len(x)):
        x_forward = x.copy()
        x_backward = x.copy()
        x_forward[i] += h
        x_backward[i] -= h
        grad[i] = (f(x_forward) - f(x_backward)) / (2 * h)
    return grad

#===================
# Покоординатный спуск
def coordinate_descent(f, x0, step=0.1, tol=1e-4, max_iter=1000):
    x = np.array(x0, dtype=float)
    n = len(x)

    for iteration in range(max_iter):
        x_old = x.copy()

        for i in range(n):
            # пробуем шаг вперёд и назад по координате i
            x_forward = x.copy()
            x_backward = x.copy()

            x_forward[i] += step
            x_backward[i] -= step

            # выбираем лучшее направление
            if f(x_forward) < f(x):
                x = x_forward
            elif f(x_backward) < f(x):
                x = x_backward

        # проверка сходимости
        if np.linalg.norm(x - x_old) < tol:
            print(f"Покоординатный спуск сошелся за {iteration} итераций")
            return x

    print("Достигнут предел итераций")
    return x

#=============================
# Градиентный спуск


def gradient_descent(f, x0, lr=0.1, tol=1e-4, max_iter=1000):
    x = np.array(x0, dtype=float)

    for i in range(max_iter):
        grad = numerical_gradient(f, x)
        x_new = x - lr * grad

        if np.linalg.norm(x_new - x) < tol:
            print(f"Градиентный спуск сошелся за {i} итераций")
            return x_new

        x = x_new

    print("Достигнут предел итераций")
    return x

#==========================
# Метод наискорейшего спуска

def line_search(f, x, direction, alpha=1.0, beta=0.5, c=1e-4):
    """
    Бэктрекинг (уменьшаем шаг, пока не станет лучше)
    """
    fx = f(x)
    grad = numerical_gradient(f, x)

    while f(x + alpha * direction) > fx + c * alpha * np.dot(grad, direction):
        alpha *= beta

    return alpha


def faster_descent(f, x0, tol=1e-4, max_iter=1000):
    x = np.array(x0, dtype=float)

    for i in range(max_iter):
        grad = numerical_gradient(f, x)

        # направление наискорейшего спуска
        direction = -grad

        # подбор шага
        alpha = line_search(f, x, direction)

        x_new = x + alpha * direction

        if np.linalg.norm(x_new - x) < tol:
            print(f"Метод наискорейшего спуска сошелся за {i} итераций")
            return x_new

        x = x_new

    print("Достигнут предел итераций")
    return x

#===========================================

start = [2, -3]

methods = [["Покоординатный спуск", coordinate_descent(f, start)], ["Градиентный спуск", gradient_descent(f, start)], ["Наискорейший спуск", faster_descent(f, start)]]

print("\n")
for method in methods:
    print(method[0])
    result = method[1]
    print("Найденный минимум :", result)
    print("Значение функции:", f(result), "\n")
print(f([2,-3]))