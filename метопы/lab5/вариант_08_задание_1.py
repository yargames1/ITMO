import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import minimize
from mpl_toolkits.mplot3d import Axes3D

# ==============================================================================
# ВАРИАНТ 8
# ==============================================================================
data = np.array([
    [0.95, 1.00, 0.44],
    [1.80, 1.86, 1.88],
    [3.04, 2.97, 4.29],
    [3.96, 4.14, 2.60],
    [4.99, 4.91, 0.76]
])

# ==============================================================================
# ЗАДАНИЕ 1: Извлеките значения из data
# ==============================================================================
X = data[:, 0]
Y = data[:, 1]
Z = data[:, 2]

print("Мои данные:")
for i in range(len(X)):
    print(f"  Точка {i}: x={X[i]:.2f}, y={Y[i]:.2f}, z={Z[i]:.2f}")

# ==============================================================================
# ЗАДАНИЕ 2: Функция Гаусса
# ==============================================================================
def gauss_2d(x, y, A, x0, y0, sigma_x, sigma_y, theta=0, offset=0):
    """
    2D функция Гаусса
    """
    if theta != 0:
        x_new = (x - x0) * np.cos(theta) + (y - y0) * np.sin(theta)
        y_new = -(x - x0) * np.sin(theta) + (y - y0) * np.cos(theta)
    else:
        x_new = x - x0
        y_new = y - y0
    exp_part = np.exp(-((x_new**2)/(2*sigma_x**2) + (y_new**2)/(2*sigma_y**2)))
    resultGaus = A * exp_part + offset
    return resultGaus

# ==============================================================================
# ЗАДАНИЕ 3: Функция потерь (MSE)
# ==============================================================================
def loss_function(model_params):
    """
    Вычисляем MSE для реальных и модельных значений.
    """
    A, x0, y0, sigma_x, sigma_y, theta, offset = model_params
    
    if sigma_x <= 0 or sigma_y <= 0 or A <= 0:
        return 1e10
    
    predictions = []
    for i in range(len(X)):
        pred = gauss_2d(X[i], Y[i], A, x0, y0, sigma_x, sigma_y, theta, offset)
        predictions.append(pred)
    
    errors = (np.array(predictions) - Z) ** 2
    mse = 0.5 * np.mean(errors)
    return mse

# ==============================================================================
# ЗАДАНИЕ 4: Начальное приближение
# ==============================================================================
max_idx = np.argmax(Z)
A_start = Z[max_idx] + 0.2          # небольшая корректировка
x0_start = X[max_idx]
y0_start = Y[max_idx]
sigma_x_start = np.std(X) * 0.5
sigma_y_start = np.std(Y) * 0.5
offset_start = 0.0
theta_start = 0.0

params_startGaus = [A_start, x0_start, y0_start, sigma_x_start, sigma_y_start, theta_start, offset_start]

print(f"\nЗначения для инициализации модельных параметров (начальное приближение):")
print(f"  Amplitude (A):     {A_start:.2f}")
print(f"  Center (x0, y0):   ({x0_start:.2f}, {y0_start:.2f})")
print(f"  Sigma_x:           {sigma_x_start:.2f}")
print(f"  Sigma_y:           {sigma_y_start:.2f}")
print(f"  Theta (rotation):  0.00 рад")
print(f"  Offset:            0.00")

# ==============================================================================
# ДОПОЛНЕНИЕ: сбор истории потерь для кривой обучения
# ==============================================================================
loss_history = []

def callback(params):
    """Callback, сохраняющий значение loss после каждой итерации."""
    current_loss = loss_function(params)
    loss_history.append(current_loss)

# ==============================================================================
# ЗАДАНИЕ 5: Границы и оптимизация
# ==============================================================================
bounds = [
    (0.1, 10.0), (0.0, 6.0), (0.0, 6.0),
    (0.1, 5.0), (0.1, 5.0), (-np.pi/4, np.pi/4), (-1.0, 1.0)
]

print("\nЗапускаем оптимизацию...")
resultGaus = minimize(loss_function, params_startGaus, method='L-BFGS-B', bounds=bounds, callback=callback)


# ==============================================================================
# РЕЗУЛЬТАТЫ
# ==============================================================================
A_opt, x0_opt, y0_opt, sigma_x_opt, sigma_y_opt, theta_opt, offset_opt = resultGaus.x

print("\n" + "="*50)
print("РЕЗУЛЬТАТЫ")
print("="*50)
print(f"\nA = {A_opt:.4f}")
print(f"x0 = {x0_opt:.4f}, y0 = {y0_opt:.4f}")
print(f"sigma_x = {sigma_x_opt:.4f}, sigma_y = {sigma_y_opt:.4f}")
print(f"theta = {theta_opt:.2f} рад = {np.degrees(theta_opt):.2f}°, offset = {offset_opt:.4f}")
print(f"\nФинальная ошибка (MSE): {resultGaus.fun:.8f}")

# ==============================================================================
# ЗАДАНИЕ 6: Проверка и вычисление невязок
# ==============================================================================
predictionsGaus = []
residualsGaus = []
for i in range(len(X)):
    pred = gauss_2d(X[i], Y[i], A_opt, x0_opt, y0_opt, sigma_x_opt, sigma_y_opt, theta_opt, offset_opt)
    predictionsGaus.append(pred)
    residualsGaus.append(Z[i] - pred)

print("\nПроверка с невязками:")
print(" № |   z факт   |   модель   |  невязка (z - модель)")
print("--------------------------------------------------")
for i in range(len(X)):
    print(f"{i:2} | {Z[i]:9.4f} | {predictionsGaus[i]:9.4f} | {residualsGaus[i]:11.4f}")

# ==============================================================================
# КРИВАЯ ОБУЧЕНИЯ (loss от итерации)
# ==============================================================================
plt.figure(figsize=(8, 5))
plt.plot(range(1, len(loss_history)+1), loss_history, 'o-', color='blue', markersize=4, linewidth=1.5)
plt.xlabel('Номер итерации', fontsize=12)
plt.ylabel('Loss (MSE)', fontsize=12)
plt.title('Кривая обучения: зависимость ошибки от итерации', fontsize=14)
plt.grid(True, alpha=0.3)
plt.yscale('log')   # логарифмическая шкала для лучшей наглядности
plt.tight_layout()
plt.savefig('learning_curve.png', dpi=300)
plt.show()

# Гистограмма невязок
plt.figure(figsize=(6, 4))
plt.hist(residualsGaus, bins=5, edgecolor='black', alpha=0.7, color='green')
plt.xlabel('Невязка (z факт - модель)', fontsize=12)
plt.ylabel('Частота', fontsize=12)
plt.title('Распределение невязок', fontsize=14)
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig('residuals_hist.png', dpi=300)
plt.show()

# ==============================================================================
# ЗАДАНИЕ 7: Визуализация поверхности и контуров (3D + 2D)
# ==============================================================================
x_grid = np.linspace(0, 6, 50)
y_grid = np.linspace(0, 6, 50)
X_grid, Y_grid = np.meshgrid(x_grid, y_grid)

Z_grid = np.zeros((50, 50))
for i in range(50):
    for j in range(50):
        Z_grid[i, j] = gauss_2d(X_grid[i, j], Y_grid[i, j], A_opt, x0_opt, y0_opt,
                                sigma_x_opt, sigma_y_opt, theta_opt, offset_opt)

fig = plt.figure(figsize=(14, 6))

ax1 = fig.add_subplot(121, projection='3d')
ax1.plot_surface(X_grid, Y_grid, Z_grid, cmap='viridis', alpha=0.8)
ax1.scatter(X, Y, Z, c='red', s=50)
ax1.set_title('3D-визуализация Гауссианы')

ax2 = fig.add_subplot(122)
contour = ax2.contourf(X_grid, Y_grid, Z_grid, levels=25, cmap='viridis', alpha=0.9)
scatter = ax2.scatter(X, Y, c=Z, s=120, edgecolors='black', cmap='viridis')
ax2.contour(X_grid, Y_grid, Z_grid, levels=10, colors='white', alpha=0.3, linewidths=0.5)
ax2.set_xlabel('X', fontsize=10)
ax2.set_ylabel('Y', fontsize=10)
ax2.set_title('Линии уровня модельной функции и точки области задания исходной', fontsize=12, fontweight='bold')
ax2.grid(True, alpha=0.3)
cbar = plt.colorbar(contour, ax=ax2, label='Z')
cbar.ax.tick_params(labelsize=9)

plt.tight_layout()
plt.savefig('model_plot.png', dpi=300)
plt.show()

# ====================================================
# ЗАДАНИЕ 2 ПО МЕТОДИЧКЕ (Выполните аппроксимацию функции двух переменных, заданную
#таблично, с помощью эллиптического параболоида (лосс-функция MSE). Используйте данные
#по варианту (см. Задание 1), предыдущий модуль и при необходимости генеративные сервисы
#для написания программного кода (язык Python). Постройте кривую обучения (лосс как
#функция от номера итерации), вычислите невязки на каждом объекте для модельной функции,
#отрисуйте модельную поверхность и её линии уровня вместе с точками данных по варианту.
#Запишите аналитический вид полученной модели
# ====================================================
# ==============================================================================
# НОВОЕ ЗАДАНИЕ: АППРОКСИМАЦИЯ ЭЛЛИПТИЧЕСКИМ ПАРАБОЛОИДОМ
# ==============================================================================

print("\n" + "="*60)
print("АППРОКСИМАЦИЯ ЭЛЛИПТИЧЕСКИМ ПАРАБОЛОИДОМ")
print("="*60)

# ==============================================================================
# МОДЕЛЬ: параболоид
# ==============================================================================
def paraboloid(x, y, a, b, c, d, e, f):
    return a*x**2 + b*y**2 + c*x*y + d*x + e*y + f

# ==============================================================================
# LOSS (MSE)
# ==============================================================================
loss_history_paraboloid = []

def loss_function_paraboloid(params):
    a, b, c, d, e, f = params

    predictions = []
    for i in range(len(X)):
        pred = paraboloid(X[i], Y[i], a, b, c, d, e, f)
        predictions.append(pred)

    errors = (np.array(predictions) - Z) ** 2
    return 0.5 * np.mean(errors)

def callback_paraboloid(params):
    loss_history_paraboloid.append(loss_function_paraboloid(params))

# ==============================================================================
# НАЧАЛЬНОЕ ПРИБЛИЖЕНИЕ
# ==============================================================================
params_start_paraboloid = [0.1, 0.1, 0.0, 0.1, 0.1, np.mean(Z)]

# ==============================================================================
# ГРАНИЦЫ
# ==============================================================================
bounds_paraboloid = [
    (-5, 5), (-5, 5), (-5, 5),
    (-10, 10), (-10, 10), (-10, 10)
]

# ==============================================================================
# ОПТИМИЗАЦИЯ
# ==============================================================================
print("\nЗапускаем оптимизацию для параболоида...")
result_paraboloid = minimize(
    loss_function_paraboloid,
    params_start_paraboloid,
    method='L-BFGS-B',
    bounds=bounds_paraboloid,
    callback=callback_paraboloid
)

a_opt, b_opt, c_opt, d_opt, e_opt, f_opt = result_paraboloid.x

# ==============================================================================
# РЕЗУЛЬТАТЫ
# ==============================================================================
print("\nПАРАБОЛОИД — параметры:")
print(f"a = {a_opt:.4f}, b = {b_opt:.4f}, c = {c_opt:.4f}")
print(f"d = {d_opt:.4f}, e = {e_opt:.4f}, f = {f_opt:.4f}")
print(f"Финальная ошибка (MSE): {result_paraboloid.fun:.8f}")

# ==============================================================================
# НЕВЯЗКИ
# ==============================================================================
predictions_paraboloid = []
residuals_paraboloid = []

for i in range(len(X)):
    pred = paraboloid(X[i], Y[i], a_opt, b_opt, c_opt, d_opt, e_opt, f_opt)
    predictions_paraboloid.append(pred)
    residuals_paraboloid.append(Z[i] - pred)

print("\nНевязки (параболоид):")
print(" № |   z факт   |   модель   |  невязка")
print("----------------------------------------")
for i in range(len(X)):
    print(f"{i:2} | {Z[i]:9.4f} | {predictions_paraboloid[i]:9.4f} | {residuals_paraboloid[i]:11.4f}")

# ==============================================================================
# КРИВАЯ ОБУЧЕНИЯ
# ==============================================================================
plt.figure(figsize=(8, 5))
plt.plot(range(1, len(loss_history_paraboloid)+1),
         loss_history_paraboloid,
         'o-', markersize=4)
plt.xlabel('Номер итерации')
plt.ylabel('Loss (MSE)')
plt.title('Кривая обучения (параболоид)')
plt.grid(True)
plt.yscale('log')
plt.tight_layout()
plt.savefig('learning_curve_paraboloid.png', dpi=300)
plt.show()

# ==============================================================================
# ГИСТОГРАММА НЕВЯЗОК (ПАРАБОЛОИД)
# ==============================================================================
plt.figure(figsize=(6, 4))

plt.hist(residuals_paraboloid,
         bins=5,
         edgecolor='black',
         alpha=0.7)

plt.xlabel('Невязка (z факт - модель)', fontsize=12)
plt.ylabel('Частота', fontsize=12)
plt.title('Распределение невязок (параболоид)', fontsize=14)

plt.grid(True, alpha=0.3)
plt.tight_layout()

plt.savefig('residuals_hist_paraboloid.png', dpi=300)
plt.show()

# ==============================================================================
# ПОВЕРХНОСТЬ
# ==============================================================================
Z_grid_paraboloid = np.zeros((50, 50))

for i in range(50):
    for j in range(50):
        Z_grid_paraboloid[i, j] = paraboloid(
            X_grid[i, j], Y_grid[i, j],
            a_opt, b_opt, c_opt, d_opt, e_opt, f_opt
        )

# ==============================================================================
# ВИЗУАЛИЗАЦИЯ
# ==============================================================================
fig = plt.figure(figsize=(14, 6))

# 3D
ax1 = fig.add_subplot(121, projection='3d')
ax1.plot_surface(X_grid, Y_grid, Z_grid_paraboloid, cmap='plasma', alpha=0.8)
ax1.scatter(X, Y, Z, c='red', s=50)
ax1.set_title('3D-параболоид')

# Контуры
ax2 = fig.add_subplot(122)
contour = ax2.contourf(X_grid, Y_grid, Z_grid_paraboloid,
                       levels=25, cmap='plasma', alpha=0.9)
ax2.scatter(X, Y, c=Z, s=120, edgecolors='black')
ax2.contour(X_grid, Y_grid, Z_grid_paraboloid,
            levels=10, colors='white', linewidths=0.5)

ax2.set_title('Линии уровня (параболоид)')
ax2.set_xlabel('X')
ax2.set_ylabel('Y')
ax2.grid(True)

plt.colorbar(contour, ax=ax2)
plt.tight_layout()
plt.savefig('paraboloid_plot.png', dpi=300)
plt.show()

# ==============================================================================
# АНАЛИТИЧЕСКИЙ ВИД
# ==============================================================================
print("\nАналитический вид модели (параболоид):")
print(f"z = {a_opt:.4f}x^2 + {b_opt:.4f}y^2 + {c_opt:.4f}xy + "
      f"{d_opt:.4f}x + {e_opt:.4f}y + {f_opt:.4f}")




# ====================================================
# ЗАДАНИЕ 3 ПО МЕТОДИЧКЕ (Найдите лучшую аппроксимацию функции константной моделью: (1) в
# смысле минимизации MSE, (2) минимизации MAE)
# ====================================================
# MSE optimum
c_mse = np.mean(Z)
mse = np.mean((Z - c_mse)**2)

# MAE optimum
c_mae = np.median(Z)
mae = np.mean(np.abs(Z - c_mae))

# Вывод задания 3
print("===================\nЗадание 3.")
print(f"Лучшая константная модель в смысле мин. MSE: среднее выборочное выборки {{zᵢ}},")
print(f"z = ({' + '.join(f'{x:.2f}' for x in Z)})/{len(Z)} = {np.sum(Z):.2f}/{len(Z)} = {c_mse:.3f}.\n")
print(f"Лучшая константная модель в смысле мин. MAE: медиана выборки {{zᵢ}},")
print(f"z = median({{{', '.join(f'{x:.2f}' for x in sorted(Z))}}}) = {c_mae:.2f}.")
