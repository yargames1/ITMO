import numpy as np

# =========================
# ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ
# =========================

def sample_mean(data):
    return np.mean(data)

def sample_variance(data):
    # дисперсия с делением на n (как нужно для ММП)
    return np.mean((data - np.mean(data))**2)


# =========================
# 1. ЭКСПОНЕНЦИАЛЬНОЕ Exp(λ, c)
# =========================

def exp_method_of_moments(data):
    mean = sample_mean(data)
    var = sample_variance(data)

    lam = 1 / np.sqrt(var)
    c = mean - 1 / lam

    return lam, c


def exp_mle(data):
    c = np.min(data)
    mean = sample_mean(data)

    lam = 1 / (mean - c)

    return lam, c


# =========================
# 2. НОРМАЛЬНОЕ N(a, σ)
# =========================

def normal_method_of_moments(data):
    mean = sample_mean(data)
    var = sample_variance(data)

    return mean, var


def normal_mle(data):
    mean = sample_mean(data)
    var = sample_variance(data)

    return mean, var


# =========================
# 3. РАВНОМЕРНОЕ U(a, b)
# =========================

def uniform_method_of_moments(data):
    mean = sample_mean(data)
    var = sample_variance(data)

    delta = np.sqrt(3 * var)

    a = mean - delta
    b = mean + delta

    return a, b


def uniform_mle(data):
    a = np.min(data)
    b = np.max(data)

    return a, b
