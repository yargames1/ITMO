import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy import stats

plt.style.use('seaborn-v0_8-whitegrid')
plt.rcParams['font.size'] = 9
plt.rcParams['axes.titlesize'] = 10
plt.rcParams['axes.labelsize'] = 9

df = pd.read_csv('RGR2_A-10_X1-X4.csv', sep=';')
X1, X2, X3, X4 = df['X1'], df['X2'], df['X3'], df['X4']
n = len(X1)

# === Fig 1: Boxplot X1 vs X2 ===
plt.figure(figsize=(4, 3.5))
plt.boxplot([X1, X2], labels=['X1', 'X2'], patch_artist=True, 
            boxprops=dict(facecolor='lightblue', color='navy'),
            medianprops=dict(color='red', linewidth=1.5))
plt.ylabel('Время отклика, мс')
plt.title('Сравнение конфигураций (Задание 1, 3)')
plt.tight_layout()
plt.savefig('fig1_boxplot.png', dpi=300, bbox_inches='tight')
plt.close()

# === Fig 2: Histogram X3 + normal curve ===
plt.figure(figsize=(4, 3.5))
plt.hist(X3, bins=12, density=True, alpha=0.7, edgecolor='black', label='Выборка')
x = np.linspace(X3.min(), X3.max(), 100)
plt.plot(x, stats.norm.pdf(x, X3.mean(), X3.std()), 'r-', linewidth=1.5, label=f'N(μ={X3.mean():.1f}, σ={X3.std():.1f})')
plt.xlabel('Время отклика, мс')
plt.ylabel('Плотность')
plt.title('Распределение X3 (Задание 2)')
plt.legend(fontsize=8)
plt.tight_layout()
plt.savefig('fig2_X3_hist.png', dpi=300, bbox_inches='tight')
plt.close()

# === Fig 3: Histogram X4 + exponential curve ===
plt.figure(figsize=(4, 3.5))
lam = 0.109
plt.hist(X4, bins=15, density=True, alpha=0.7, edgecolor='black', label='Выборка')
x = np.linspace(0, X4.max(), 200)
plt.plot(x, stats.expon.pdf(x, scale=1/lam), 'r-', linewidth=1.5, label=f'Exp(λ={lam})')
plt.xlabel('Интервал, мс')
plt.ylabel('Плотность')
plt.title('Интервалы между событиями (Задание 4)')
plt.legend(fontsize=8)
plt.tight_layout()
plt.savefig('fig3_X4_exp.png', dpi=300, bbox_inches='tight')
plt.close()

# === Fig 4: QQ-plot X4 vs Exp ===
plt.figure(figsize=(4, 3.5))
stats.probplot(X4, dist=stats.expon(scale=1/lam), plot=plt)
plt.title('Q-Q plot: X4 vs Exp(0.109)')
plt.tight_layout()
plt.savefig('fig4_QQplot.png', dpi=300, bbox_inches='tight')
plt.close()

print("✅ Графики сохранены: fig1_*.png ... fig4_*.png")