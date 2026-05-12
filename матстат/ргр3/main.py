import pandas as pd
import numpy as np
from scipy import stats
import matplotlib.pyplot as plt

# =========================================================
# Лабораторная работа:
# Простая линейная регрессия
# =========================================================

# =========================
# Загрузка данных
# =========================

df = pd.read_csv('матстат/ргр3/variant_10_data.csv', sep=',')

print("===== Исходные данные =====")
print(df)

X = df['x'].values
Y = df['y'].values

n = len(X)

# =========================
# Диаграмма рассеяния
# =========================

plt.figure(figsize=(7, 5))
plt.scatter(X, Y)
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Диаграмма рассеяния')
plt.grid(True, alpha=0.3)
plt.show()

# =========================
# Средние значения
# =========================

mean_x = np.mean(X)
mean_y = np.mean(Y)

print("\n===== Средние значения =====")
print(f"x̄ = {mean_x:.4f}")
print(f"ȳ = {mean_y:.4f}")

# =========================
# Вспомогательные суммы
# =========================

Sxx = np.sum((X - mean_x) ** 2)
Sxy = np.sum((X - mean_x) * (Y - mean_y))

print("\n===== Вспомогательные суммы =====")
print(f"Sxx = {Sxx:.4f}")
print(f"Sxy = {Sxy:.4f}")

# =========================
# Оценивание коэффициентов регрессии
# =========================

beta1 = Sxy / Sxx
beta0 = mean_y - beta1 * mean_x

print("\n========== Оценки параметров модели Y = β₀ + β₁·X ==========")
print(f"β₀ = {beta0:.4f}")
print(f"β₁ = {beta1:.4f}")

print("\n===== Уравнение регрессии =====")
print(f"ŷ = {beta0:.4f} + {beta1:.4f}·x")

# =========================
# Прогнозные значения и остатки
# =========================

Y_pred = beta0 + beta1 * X
residuals = Y - Y_pred

results_df = pd.DataFrame({
    'i': np.arange(1, n + 1),
    'x_i': X,
    'y_i': Y,
    'y_hat_i': Y_pred,
    'e_i': residuals,
    'e_i^2': residuals ** 2
})

print("\n===== Таблица прогнозных значений и остатков =====")
print(results_df)

# =========================
# Остаточная сумма квадратов
# =========================

RSS = np.sum(residuals ** 2)

# Общая сумма квадратов
TSS = np.sum((Y - mean_y) ** 2)

# Объяснённая сумма квадратов
SSR = TSS - RSS

# Коэффициент детерминации
R2 = 1 - RSS / TSS

# =========================
# Оценка дисперсии ошибки
# =========================

sigma2_hat = RSS / (n - 2)
sigma_hat = np.sqrt(sigma2_hat)

print("\n===== Оценка качества модели =====")
print(f"RSS = {RSS:.4f}")
print(f"TSS = {TSS:.4f}")
print(f"SSR = {SSR:.4f}")
print(f"R² = {R2:.4f}")

print("\n===== Оценка дисперсии ошибки =====")
print(f"s² = {sigma2_hat:.4f}")
print(f"s = {sigma_hat:.4f}")

# =========================
# Стандартные ошибки коэффициентов
# =========================

se_beta1 = sigma_hat / np.sqrt(Sxx)
se_beta0 = sigma_hat * np.sqrt(1 / n + mean_x ** 2 / Sxx)

print("\n===== Стандартные ошибки коэффициентов =====")
print(f"SE(β₀) = {se_beta0:.4f}")
print(f"SE(β₁) = {se_beta1:.4f}")

# =========================
# Доверительные интервалы
# =========================

alpha = 0.05
t_crit = stats.t.ppf(1 - alpha / 2, df=n - 2)

print("\n===== Критическое значение t =====")
print(f"t_crit = {t_crit:.4f}")

# Интервал для beta0
ci_beta0_low = beta0 - t_crit * se_beta0
ci_beta0_up = beta0 + t_crit * se_beta0

# Интервал для beta1
ci_beta1_low = beta1 - t_crit * se_beta1
ci_beta1_up = beta1 + t_crit * se_beta1

print("\n===== 95%-доверительные интервалы =====")
print(f"Для β₀: [{ci_beta0_low:.4f}; {ci_beta0_up:.4f}]")
print(f"Для β₁: [{ci_beta1_low:.4f}; {ci_beta1_up:.4f}]")

# =========================
# Проверка значимости β₁
# =========================

print("\n===== Проверка значимости коэффициента β₁ =====")

if ci_beta1_low <= 0 <= ci_beta1_up:
    print("0 попадает в доверительный интервал β₁.")
    print("Коэффициент β₁ статистически НЕ значим на уровне 5%.")
else:
    print("0 НЕ попадает в доверительный интервал β₁.")
    print("Коэффициент β₁ статистически значим на уровне 5%.")

# =========================
# Доверительный интервал
# для средней функции регрессии
# =========================

x_points = np.array([
    np.min(X),
    np.mean(X),
    np.max(X)
])

x_points = np.unique(np.round(x_points, 2))

print("\n===== Доверительные интервалы для E(Y|x) =====")
print(f"{'x0':>8} {'ŷ(x0)':>12} {'Нижняя':>12} {'Верхняя':>12}")

for x0 in x_points:

    y_hat = beta0 + beta1 * x0

    se_mean = sigma_hat * np.sqrt(
        1 / n + (x0 - mean_x) ** 2 / Sxx
    )

    ci_low = y_hat - t_crit * se_mean
    ci_up = y_hat + t_crit * se_mean

    print(f"{x0:8.2f} {y_hat:12.4f} {ci_low:12.4f} {ci_up:12.4f}")

# =========================
# Построение доверительной полосы
# =========================

X_grid = np.linspace(np.min(X), np.max(X), 200)

Y_grid = beta0 + beta1 * X_grid

se_grid = sigma_hat * np.sqrt(
    1 / n + (X_grid - mean_x) ** 2 / Sxx
)

ci_low_grid = Y_grid - t_crit * se_grid
ci_up_grid = Y_grid + t_crit * se_grid

# =========================
# Итоговый график
# =========================

plt.figure(figsize=(10, 6))

# Исходные точки
plt.scatter(X, Y, alpha=0.7, label='Наблюдения')

# Линия регрессии
plt.plot(X_grid, Y_grid, 'r-', linewidth=2,
         label='Линия регрессии')

# Нижняя граница
plt.plot(X_grid, ci_low_grid, 'g--',
         label='Нижняя граница ДИ')

# Верхняя граница
plt.plot(X_grid, ci_up_grid, 'b--',
         label='Верхняя граница ДИ')

# Доверительная полоса
plt.fill_between(
    X_grid,
    ci_low_grid,
    ci_up_grid,
    alpha=0.15
)

plt.xlabel('X')
plt.ylabel('Y')

plt.title(
    'Простая линейная регрессия\n'
    'с доверительным интервалом для функции регрессии'
)

plt.legend()
plt.grid(True, alpha=0.3)

plt.show()

# =========================
# Итоговые выводы
# =========================

print("\n================ ИТОГОВЫЕ ВЫВОДЫ ================")

print(f"1. Уравнение регрессии:")
print(f"   ŷ = {beta0:.4f} + {beta1:.4f}·x")

if beta1 > 0:
    print("2. Зависимость между x и y положительная.")
else:
    print("2. Зависимость между x и y отрицательная.")

if ci_beta1_low <= 0 <= ci_beta1_up:
    print("3. Ноль попадает в доверительный интервал β₁.")
    print("4. Линейную зависимость нельзя считать статистически значимой.")
else:
    print("3. Ноль НЕ попадает в доверительный интервал β₁.")
    print("4. Линейная зависимость статистически значима.")

print("5. Доверительный интервал наиболее узкий")
print("   вблизи среднего значения x.")

print(f"6. Коэффициент детерминации R² = {R2:.4f}")

if R2 > 0.8:
    print("   Качество модели высокое.")
elif R2 > 0.5:
    print("   Качество модели среднее.")
else:
    print("   Качество модели низкое.")