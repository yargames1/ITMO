# По данным наблюдениям построить простую линейную регрессию, 
# найти доверительные интервалы для коэффициентов регрессии и 
# доверительный интервал для средней функции регрессии.

import pandas as pd
import numpy as np
from scipy import stats

# =========================
# Загрузка данных
# =========================
df = pd.read_csv('матстат/ргр3/variant_10_data.csv', sep=',')

X = df['x'].values
Y = df['y'].values

n = len(X)

# =========================
# Оценка коэффициентов по МНК (из лекции, стр. 2-3)
# =========================
mean_x = np.mean(X)
mean_y = np.mean(Y)

# Сумма квадратов отклонений X и кросс-произведение
Sxx = np.sum((X - mean_x) ** 2)
Sxy = np.sum((X - mean_x) * (Y - mean_y))

# Оценки коэффициентов (theta_1 и theta_0 в обозначениях лекции)
beta1 = Sxy / Sxx
beta0 = mean_y - beta1 * mean_x

print("\n========== Оценки параметров модели Y = θ₀ + θ₁·X ==========")
print(f"θ₀ = {beta0:.4f},  θ₁ = {beta1:.4f}")

# =========================
# Качество подгонки: R^2 и остаточная дисперсия
# =========================
Y_pred = beta0 + beta1 * X       # предсказанные моделью значения
residuals = Y - Y_pred
SSE = np.sum(residuals ** 2)     # сумма квадратов остатков
SST = np.sum((Y - mean_y) ** 2)  # общая сумма квадратов
SSR = SST - SSE                  # объяснённая сумма квадратов
R2 = 1 - SSE / SST

# Несмещённая оценка дисперсии ошибок σ²
sigma2_hat = SSE / (n - 2)
sigma_hat = np.sqrt(sigma2_hat)

print(f"\nSSE = {SSE:.4f}, SST = {SST:.4f}, SSR = {SSR:.4f}")
print(f"Коэффициент детерминации R² = {R2:.4f}")
print(f"Несмещённая оценка дисперсии ошибок σ² = {sigma2_hat:.4f}, σ = {sigma_hat:.4f}")

# =========================
# Доверительные интервалы для коэффициентов
# =========================
# Стандартные ошибки оценок
se_beta1 = sigma_hat / np.sqrt(Sxx)
se_beta0 = sigma_hat * np.sqrt(1/n + mean_x**2 / Sxx)

# Уровень доверия 95%, квантиль t-распределения с n-2 степенями свободы
alpha = 0.05
t_crit = stats.t.ppf(1 - alpha/2, df=n-2)

# Границы доверительных интервалов
ci_beta0_low = beta0 - t_crit * se_beta0
ci_beta0_up = beta0 + t_crit * se_beta0
ci_beta1_low = beta1 - t_crit * se_beta1
ci_beta1_up = beta1 + t_crit * se_beta1

print("\n====== 95%-доверительные интервалы для коэффициентов ======")
print(f"Для θ₀: [{ci_beta0_low:.4f}, {ci_beta0_up:.4f}]")
print(f"Для θ₁: [{ci_beta1_low:.4f}, {ci_beta1_up:.4f}]")

# =========================
# Доверительный интервал для средней функции регрессии E(Y|X=x0)
# =========================
# Выберем несколько характерных точек: минимум, среднее, максимум
x_points = np.array([np.min(X), np.mean(X), np.max(X)])
x_points = np.unique(np.round(x_points, 2))   # уникальные значения

print("\n===== Доверительные интервалы для средней функции регрессии (95%) =====")
print(f"{'x0':>8}  {'ŷ(x0)':>8}  {'Нижняя':>10}  {'Верхняя':>10}")
for x0 in x_points:
    y_hat = beta0 + beta1 * x0
    # Дисперсия предсказания среднего
    se_mean = sigma_hat * np.sqrt(1/n + (x0 - mean_x)**2 / Sxx)
    ci_low = y_hat - t_crit * se_mean
    ci_up = y_hat + t_crit * se_mean
    print(f"{x0:8.2f}  {y_hat:8.4f}  {ci_low:10.4f}  {ci_up:10.4f}")

# =========================
# Визуализация
# =========================

import matplotlib.pyplot as plt
X_grid = np.linspace(np.min(X), np.max(X), 100)
Y_grid = beta0 + beta1 * X_grid
se_grid = sigma_hat * np.sqrt(1/n + (X_grid - mean_x)**2 / Sxx)
ci_low_grid = Y_grid - t_crit * se_grid
ci_up_grid = Y_grid + t_crit * se_grid

plt.figure(figsize=(9,5))
plt.scatter(X, Y, alpha=0.6, label='Наблюдения')
plt.plot(X_grid, Y_grid, 'r-', label='Линия регрессии')
plt.fill_between(X_grid, ci_low_grid, ci_up_grid, color='red', alpha=0.15,
                    label='95% ДИ для среднего')
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Простая линейная регрессия с доверительной полосой для среднего')
plt.legend()
plt.grid(True, alpha=0.3)
plt.show()
