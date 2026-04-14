import numpy as np
import pandas as pd


# =========================
# Загрузка данных из CSV
# =========================
def load_csv_columns(filepath):
    """
    Загружает CSV и возвращает первые 3 столбца как numpy массивы
    """
    df = pd.read_csv(filepath)

    col1 = df.iloc[:, 0].to_numpy()
    col2 = df.iloc[:, 1].to_numpy()
    col3 = df.iloc[:, 2].to_numpy()
    col4 = df.iloc[:, 3].to_numpy()

    return col1, col2, col3, col4


# =========================
# Вариационный ряд
# =========================
def variation_series(data):
    return np.sort(data)


# =========================
# Эмпирическая функция распределения
# =========================
def empirical_cdf(data):
    sorted_data = np.sort(data)
    n = len(data)
    y = np.arange(1, n + 1) / n
    return sorted_data, y


# =========================
# Правила выбора интервалов
# =========================
def sturges_rule(n):
    return int(np.ceil(1 + np.log2(n)))


def scott_rule(data):
    n = len(data)
    mean = np.mean(data)
    var_biased = 1/n*(sum(((x-mean)**2 for x in data)))
    std = np.sqrt(var_biased)
    h = 3.5 * std / (n ** (1 / 3))
    return int(np.ceil((np.max(data) - np.min(data)) / h))


def fd_rule(data):
    q75, q25 = np.percentile(data, [75, 25])
    iqr = q75 - q25
    n = len(data)
    h = 2 * iqr / (n ** (1 / 3))
    return int(np.ceil((np.max(data) - np.min(data)) / h))


# =========================
# Числовые характеристики
# =========================
def statistics(data):
    n = len(data)

    mean = np.mean(data)

    var_biased = 1/n*(sum(((x-mean)**2 for x in data)))
    var_unbiased = 1/(n-1)*(sum(((x-mean)**2 for x in data)))

    std_biased = np.sqrt(var_biased)
    std_unbiased = np.sqrt(var_unbiased)

    median = np.median(data)

    quartiles = np.percentile(data, [25, 50, 75])

    return {
        "mean": mean,
        "var_biased (S^2)": var_biased,
        "var_unbiased (σ^2)": var_unbiased,
        "std_biased (S)": std_biased,
        "std_unbiased (σ)": std_unbiased,
        "median": median,
        "quartiles (Q1, Q2, Q3)": quartiles
    }