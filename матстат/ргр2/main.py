"""
Вариант: A-10
Группа: A
Единицы измерения: мс
Объём выборки: n = 96
Уровень значимости: α = 0,05

Карточка варианта
1) По столбцам X1 и X2 проверить гипотезу о равенстве математических ожиданий двух независимых нормальных совокупностей.
2) По столбцу X3 проверить гипотезу:
   H₀: σ² = 63,53.
3) По столбцу X4 проверить гипотезу:
   H₀: X4 имеет показательное распределение с параметром λ = 0,109.
   Использовать критерий согласия Пирсона χ².

Если в карточке варианта не указано иное, использовать двустороннюю альтернативу.
"""
import pandas as pd
import numpy as np
from scipy.stats import t, chi2  # только для p-value

# =========================
# Загрузка данных
# =========================
df = pd.read_csv('матстат/ргр2/RGR2_A-10_X1-X4.csv', sep=';')

X1 = df['X1'].values
X2 = df['X2'].values
X3 = df['X3'].values
X4 = df['X4'].values

n = len(X1)
alpha = 0.05

# =========================
# 1. X1 vs X2
# =========================

# Средние
mean1 = sum(X1) / n
mean2 = sum(X2) / n

# Дисперсии (несмещённые)
S1_sq = sum((x - mean1)**2 for x in X1) / (n - 1)
S2_sq = sum((x - mean2)**2 for x in X2) / (n - 1)

# Объединённая дисперсия
S_sq = ((n - 1)*S1_sq + (n - 1)*S2_sq) / (2*n - 2)

# t-статистика
t_stat = (mean1 - mean2) / np.sqrt(S_sq * (1/n + 1/n))

df_t = 2*n - 2

# p-value
p_value_t = 2 * (1 - t.cdf(abs(t_stat), df=df_t))

print("=== t-критерий ===")
print("mean1 =", mean1)
print("mean2 =", mean2)
print("S1^2 =", S1_sq)
print("S2^2 =", S2_sq)
print("S_pooled^2 =", S_sq)
print("t =", t_stat)
print("p-value =", p_value_t)


# =========================
# 1.2 Манна–Уитни вручную
# =========================

# объединяем
combined = [(x, 'X1') for x in X1] + [(x, 'X2') for x in X2]

# сортировка
combined.sort(key=lambda x: x[0])

# ранги
ranks = []
i = 0
while i < len(combined):
    j = i
    while j < len(combined) and combined[j][0] == combined[i][0]:
        j += 1

    avg_rank = (i + j + 1) / 2  # средний ранг
    for k in range(i, j):
        ranks.append((combined[k][1], avg_rank))

    i = j

# сумма рангов X1
R1 = sum(rank for group, rank in ranks if group == 'X1')

# U-статистика
U1 = R1 - n*(n+1)/2
U2 = n*n - U1
U = min(U1, U2)

# аппроксимация нормальным
mu_U = n*n / 2
sigma_U = np.sqrt(n*n*(2*n+1)/12)

z = (U - mu_U) / sigma_U

from scipy.stats import norm
p_value_u = 2 * (1 - norm.cdf(abs(z)))

print("\n=== Манна-Уитни ===")
print("R1 =", R1)
print("U =", U)
print("z =", z)
print("p-value =", p_value_u)


# =========================
# 2. Проверка дисперсии X3
# =========================

mean3 = sum(X3) / n

S3_sq = sum((x - mean3)**2 for x in X3) / (n - 1)

sigma0_sq = 63.53

chi_stat = (n - 1) * S3_sq / sigma0_sq

df_chi = n - 1

p_value_chi = 2 * min(
    chi2.cdf(chi_stat, df_chi),
    1 - chi2.cdf(chi_stat, df_chi)
)

print("\n=== Дисперсия X3 ===")
print("mean3 =", mean3)
print("S3^2 =", S3_sq)
print("chi^2 =", chi_stat)
print("p-value =", p_value_chi)


# =========================
# 3. Пирсон (X4) вручную
# =========================

lambda_ = 0.109

# число интервалов (Стерджес)
k = int(1 + 3.322 * np.log10(n))

# границы
xmin, xmax = min(X4), max(X4)
bins = np.linspace(xmin, xmax, k+1)

# наблюдаемые частоты
observed = [0]*k
for x in X4:
    for i in range(k):
        if bins[i] <= x < bins[i+1]:
            observed[i] += 1
            break
    else:
        if x == xmax:
            observed[-1] += 1

# теоретические вероятности
expected = []
for i in range(k):
    a, b = bins[i], bins[i+1]
    p = np.exp(-lambda_*a) - np.exp(-lambda_*b)
    expected.append(n * p)

# объединение интервалов (если <5)
obs_new = []
exp_new = []

temp_o, temp_e = 0, 0

for o, e in zip(observed, expected):
    temp_o += o
    temp_e += e
    if temp_e >= 5:
        obs_new.append(temp_o)
        exp_new.append(temp_e)
        temp_o, temp_e = 0, 0

# χ²
chi2_stat = sum((o - e)**2 / e for o, e in zip(obs_new, exp_new))

df_pearson = len(obs_new) - 1  # параметр задан

p_value_pearson = 1 - chi2.cdf(chi2_stat, df_pearson)

print("\n=== Пирсон ===")
print("chi^2 =", chi2_stat)
print("df =", df_pearson)
print("p-value =", p_value_pearson)