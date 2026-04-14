import matplotlib.pyplot as plt


# =========================
# Эмпирическая функция распределения
# =========================
def plot_ecdf(x, y, title="Эмпирическая функция распределения"):
    plt.figure()
    plt.step(x, y, where='post')
    plt.xlabel("x")
    plt.ylabel("F_n(x)")
    plt.title(title)
    plt.grid()
    plt.show()


# =========================
# Гистограмма
# =========================
def plot_histogram(data, bins, title):
    plt.figure()
    plt.hist(data, bins=bins)
    plt.title(title)
    plt.xlabel("x")
    plt.ylabel("Частота")
    plt.grid()
    plt.show()