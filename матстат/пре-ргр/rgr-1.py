import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy import stats

data = pd.read_csv("RGR1_A-11_X1-X4.csv")

values = data.values.flatten()

# Кол-во данных
n = len(values)

# границы
xmin = min(values)
xmax = max(values)

# стандартное отклонение
sigma = np.std(values, ddof=1)

# правило Скотта
h = 3.5 * sigma / (n ** (1/3))

# число интервалов
k = int((xmax - xmin) / h)

# интервалы
bins = np.arange(xmin, xmax + h, h)

# частоты по интервалам
freq, edges = np.histogram(values, bins=bins)

# середины интервалов
midpoints = (edges[:-1] + edges[1:]) / 2

# среднее по группированной выборке
mean_grouped = np.sum(freq * midpoints) / n

# дисперсия по группированной выборке
variance_grouped = np.sum(freq * (midpoints - mean_grouped)**2) / (n - 1)

# среднеквадратичное отклонение
std_grouped = np.sqrt(variance_grouped)

# медиана
median = np.median(values)

# Асиметоия
skew = stats.skew(values)


print("Количество наблюдений:", n)
print("Стандартное отклонение:", sigma)
print("Среднее (группированное):", mean_grouped)
print("Дисперсия:", variance_grouped)
print("Стандартное отклонение (группированное):", std_grouped)
print("Медиана:", median)
print(f"Асимметрия:   {skew}")

# гистограмма
plt.figure(figsize=(8,5))
plt.hist(values, bins, edgecolor='black')

plt.title("Гистограмма")
plt.xlabel("Знач")
plt.ylabel("Частота")

# линия среднего
plt.axvline(mean_grouped, color='red', linestyle='--', label='среднее')
# интервал [x̄ − σ , x̄ + σ]
plt.axvline(mean_grouped - std_grouped, color='green', linestyle='--', label='ср - откл')
plt.axvline(mean_grouped + std_grouped, color='green', linestyle='--', label='ср + откл')
# линия медианы
plt.axvline(median, color='purple', linestyle='--', label='медиана')

plt.legend()

plt.grid(True)
plt.show()