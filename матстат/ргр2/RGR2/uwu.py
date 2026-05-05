import pandas as pd
import numpy as np
from scipy import stats

# 1. Загрузка данных
df = pd.read_csv('RGR2_A-10_X1-X4.csv', sep=';')
X1 = df['X1'].values
X2 = df['X2'].values
X3 = df['X3'].values
X4 = df['X4'].values

alpha = 0.05
n = len(X1)
print(f"Данные загружены. Объём выборки: n = {n}")
print("="*65)

# ==========================================
# ЗАДАНИЕ 1: Проверка равенства матожиданий (X1, X2)
# ==========================================
print("ЗАДАНИЕ 1: Проверка гипотезы о равенстве математических ожиданий (X1, X2)")
t_stat, p_val_1 = stats.ttest_ind(X1, X2, equal_var=True)
df_t = n + n - 2
t_crit = stats.t.ppf(1 - alpha/2, df_t)

print(f"• Статистика t = {t_stat:.4f}")
print(f"• p-value = {p_val_1:.6f}")
print(f"• Критическое значение (двустороннее, α=0.05, df={df_t}): ±{t_crit:.4f}")
decision_1 = "ОТВЕРГАЕМ H₀" if abs(t_stat) > t_crit else "НЕТ ОСНОВАНИЙ ОТВЕРГНУТЬ H₀"
print(f"👉 Вывод: {decision_1}\n")

# ==========================================
# ЗАДАНИЕ 2: Проверка гипотезы о дисперсии (X3)
# ==========================================
print("ЗАДАНИЕ 2: Проверка гипотезы о дисперсии (X3)")
s2 = np.var(X3, ddof=1)
sigma0_2 = 63.53
chi2_stat_2 = (n - 1) * s2 / sigma0_2
df_chi2_2 = n - 1
chi2_crit_lower = stats.chi2.ppf(alpha/2, df_chi2_2)
chi2_crit_upper = stats.chi2.ppf(1 - alpha/2, df_chi2_2)
p_val_2 = 2 * min(stats.chi2.cdf(chi2_stat_2, df_chi2_2), 1 - stats.chi2.cdf(chi2_stat_2, df_chi2_2))

print(f"• Выборочная дисперсия s² = {s2:.4f}")
print(f"• Статистика χ² = {chi2_stat_2:.4f}")
print(f"• p-value = {p_val_2:.6f}")
print(f"• Критическая область: (0; {chi2_crit_lower:.2f}] ∪ [{chi2_crit_upper:.2f}; +∞)")
decision_2 = "ОТВЕРГАЕМ H₀" if chi2_stat_2 < chi2_crit_lower or chi2_stat_2 > chi2_crit_upper else "НЕТ ОСНОВАНИЙ ОТВЕРГНУТЬ H₀"
print(f"👉 Вывод: {decision_2}\n")

# ==========================================
# ЗАДАНИЕ 3: Непараметрический критерий Манна-Уитни (X1, X2)
# ==========================================
print(" ЗАДАНИЕ 3: Непараметрический критерий Манна-Уитни (X1, X2)")
u_stat, p_val_3 = stats.mannwhitneyu(X1, X2, alternative='two-sided')

print(f"• Статистика U = {u_stat:.2f}")
print(f"• p-value = {p_val_3:.6f}")
decision_3 = "ОТВЕРГАЕМ H₀" if p_val_3 < alpha else "НЕТ ОСНОВАНИЙ ОТВЕРГНУТЬ H₀"
print(f"👉 Вывод: {decision_3}")
match = (decision_1 == decision_3)
print(f"🔍 Сравнение с Заданием 1: {'Выводы совпали' if match else 'Выводы различаются'}\n")

# ==========================================
# ЗАДАНИЕ 4: Критерий согласия Пирсона (X4 ~ Exp(λ=0.109))
# ==========================================
print("ЗАДАНИЕ 4: Критерий согласия Пирсона (X4 ~ Exp(λ=0.109))")
lam = 0.109
F = lambda x: 1 - np.exp(-lam * x)

# Начальное разбиение на 9 интервалов
edges_init = np.linspace(X4.min(), X4.max(), 10)
probs_init = np.diff(F(edges_init))
exp_init = n * probs_init
obs_init, _ = np.histogram(X4, bins=edges_init)

# Автоматическое объединение интервалов с ожидаемой частотой < 5
merged_obs, merged_exp, merged_edges = [], [], [edges_init[0]]
i = 0
while i < len(exp_init):
    curr_o, curr_e = obs_init[i], exp_init[i]
    j = i + 1
    while curr_e < 5 and j < len(exp_init):
        curr_o += obs_init[j]
        curr_e += exp_init[j]
        j += 1
    merged_obs.append(curr_o)
    merged_exp.append(curr_e)
    merged_edges.append(edges_init[j])
    i = j

# Страховка для последнего интервала
if merged_exp[-1] < 5 and len(merged_exp) > 1:
    merged_obs[-2] += merged_obs.pop()
    merged_exp[-2] += merged_exp.pop()
    merged_edges.pop()

k = len(merged_obs)
chi2_stat_4 = sum((o - e)**2 / e for o, e in zip(merged_obs, merged_exp))
df_4 = k - 1  # параметр λ задан заранее, s=0
p_val_4 = 1 - stats.chi2.cdf(chi2_stat_4, df_4)
crit_4 = stats.chi2.ppf(1 - alpha, df_4)

print(f"• Количество интервалов после объединения: k = {k}")
for idx in range(k):
    print(f"  [{merged_edges[idx]:.2f}; {merged_edges[idx+1]:.2f}): набл.={merged_obs[idx]}, ожид.={merged_exp[idx]:.2f}")
print(f"• Статистика χ² = {chi2_stat_4:.4f}")
print(f"• Степени свободы: df = {df_4}")
print(f"• p-value = {p_val_4:.6f}")
print(f"• Критическое значение (α=0.05): χ²_крит = {crit_4:.4f}")
decision_4 = "ОТВЕРГАЕМ H₀" if chi2_stat_4 > crit_4 else "НЕТ ОСНОВАНИЙ ОТВЕРГНУТЬ H₀"
print(f"👉 Вывод: {decision_4}\n")

# ==========================================
# ИТОГ
# ==========================================
print("="*65)
print("📋 ИТОГОВАЯ СВОДКА ПО ВАРИАНТУ A-10")
print(f"Задание 1 (t-критерий Стьюдента):     {decision_1}")
print(f"Задание 2 (χ² для дисперсии):          {decision_2}")
print(f"Задание 3 (Манна-Уитни):               {decision_3}")
print(f"Задание 4 (χ² Пирсона):                {decision_4}")
print("="*65)