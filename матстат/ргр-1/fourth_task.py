import numpy as np
from scipy.stats import norm

# =========================
# 0. общее
# =========================

def emperical_prob(data):
    mean = np.mean(data)
    std = np.sqrt(np.mean((data - mean)**2))
    x0 = mean+std
    n = len(data)
    s = 0
    for x in data:
        if x > x0:
            s += 1
    return s/n

# =========================
# 1. EXP
# =========================

def exp_param_prob(x0, lam, c):
    if x0 < c:
        return 1.0
    return np.exp(-lam * (x0 - c))


# =========================
# 2. NORMAL
# =========================

def normal_param_prob(x0, mean, var):
    std = np.sqrt(var)
    z = (x0 - mean) / std
    return 1 - norm.cdf(z)


# =========================
# 3. UNIFORM
# =========================

def uniform_param_prob(x0, a, b):
    if x0 >= b:
        return 0.0
    if x0 <= a:
        return 1.0
    return (b - x0) / (b - a)
