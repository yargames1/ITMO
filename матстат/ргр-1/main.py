from first_task import *
from second_task import *
from fourth_task import *
from plots import *


# =========================
# Загрузка данных
# =========================
col1, col2, col3, col4 = load_csv_columns("C:\ITMO\матстат\ргр-1\RGR1_A-15_X1-X4.csv")


# =========================
# Выбор столбца
# =========================
data = col3 


# =========================
# 1. Вариационный ряд
# =========================
var_series = variation_series(data)
#print("Вариационный ряд:")
#print(var_series)


# =========================
# 2. Эмпирическая функция
# =========================
x, y = empirical_cdf(data)
#plot_ecdf(x, y)


# =========================
# 3. Гистограммы
# =========================
n = len(data)

bins_sturges = sturges_rule(n)
bins_scott = scott_rule(data)
bins_fd = fd_rule(data)

#plot_histogram(data, bins_sturges, "Гистограмма (Стерджес)")
#plot_histogram(data, bins_scott, "Гистограмма (Скотт)")
#plot_histogram(data, bins_fd, "Гистограмма (Фридман-Дьяконис)")


# =========================
# 4. Числовые характеристики
# =========================
stats = statistics(data)

'''print("\nЧисловые характеристики:")
for key, value in stats.items():
    print(f"{key}: {value}")'''


# =========================
# Оценивание параметров
# =========================

'''
print("X1 (Exp)")
print("MM:", exp_method_of_moments(col1))
print("MLE:", exp_mle(col1))

print("\nX2 (Normal)")
print("MM:", normal_method_of_moments(col2))
print("MLE:", normal_mle(col2))

print("\nX3 (Uniform)")
print("MM:", uniform_method_of_moments(col3))
print("MLE:", uniform_mle(col3))
'''

# =========================
# Оценка параметрической вероятности
# =========================
'''emp = emperical_prob(col1)
param = exp_param_prob(119.1033, 0.0195, 14.08)
err = emp-param
print("X1 (Exp)")
print("Эмперический метод", emp)
print("Параметрический:", param)
print("Ошибка", err)

emp = emperical_prob(col2)
param = normal_param_prob(54.6669, 43.9104, 115.7031)
err = emp-param
print("\nX2 (Normal)")
print("Эмперический метод", emp)
print("Параметрический:", param)
print("Ошибка", err)

emp = emperical_prob(col3)
param = uniform_param_prob(75.5665, min(col3), max(col3))
err = emp-param
print("\nX3 (Uniform)")
print("Эмперический метод", emp)
print("Параметрический:", param)
print("Ошибка", err)'''


# =========================
# Оценка параметрической вероятности
# =========================
'''
for col in col1, col2, col3:
    # Определяем количество интервалов
    n_bins = sturges_rule(len(col))

    # Строим гистограмму с автоматически подобранными интервалами
    freq, edges = np.histogram(col, bins=n_bins)

    # Середины интервалов
    midpoints = (edges[:-1] + edges[1:]) / 2

    # EX и DX по сгруппированной выборке
    n = np.sum(freq)
    EX_grouped = np.sum(freq * midpoints) / n
    DX_grouped = np.sum(freq * (midpoints - EX_grouped)**2) / (n - 1)

    # EX и DX по исходным данным
    EX_raw = np.mean(col)
    DX_raw = 1/(n-1)*(sum(((x-EX_raw)**2 for x in col)))

    # Вывод результатов
    print("============================")
    print(f"Количество интервалов (Стерджес): {n_bins}")
    print(f"Количество интервалов: {n}")
    print("Частоты:", freq)
    print("Середины интервалов:", midpoints)
    print("E[X] по сгруппированной выборке:", EX_grouped)
    print("D[X] по сгруппированной выборке:", DX_grouped)
    print("E[X] по исходным данным:", EX_raw)
    print("D[X] по исходным данным:", DX_raw)
    print("============================\n")

'''



# =========================
# Оценка параметрической вероятности
# =========================

# ЭФР
sorted_data = np.sort(col4)
n = len(col4)
ecdf = np.arange(1, n + 1) / n

plt.figure(figsize=(12, 5))

plt.subplot(1, 2, 1)
bins_sturges = sturges_rule(n)
plt.hist(col4, bins_sturges, density=True)
plt.title("Гистограмма")

plt.subplot(1, 2, 2)
plt.step(sorted_data, ecdf, where='post')
plt.title("ЭФР")

plt.show()


X = col4.reshape(-1, 1)

# 1. случайная инициализация центров
np.random.seed(42)
centroids = X[np.random.choice(len(X), 2, replace=False)]

for _ in range(100):  # максимум 100 итераций
    # 2. считаем расстояния до центров
    distances = np.abs(X - centroids.T)
    
    # 3. назначаем кластеры
    labels = np.argmin(distances, axis=1)
    
    # 4. пересчитываем центры
    new_centroids = np.array([
        X[labels == i].mean() for i in range(2)
    ]).reshape(-1, 1)
    
    # 5. проверка сходимости
    if np.allclose(centroids, new_centroids):
        break
        
    centroids = new_centroids

# разбиение
cluster1 = col4[labels == 0]
cluster2 = col4[labels == 1]



def describe(x):
    return {
        "mean": np.mean(x),
        "var": np.var(x),
        "size": len(x)
    }

print("Cluster 1:", describe(cluster1))
print("Cluster 2:", describe(cluster2))


plt.hist(cluster1, bins=10, alpha=0.5, label="Cluster 1")
plt.hist(cluster2, bins=10, alpha=0.5, label="Cluster 2")
plt.legend()
plt.title("Разделение на кластеры")
plt.show()

print(np.mean(col4))