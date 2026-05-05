import random

# =========================
# 1. ДАННЫЕ (матрица)
# =========================
dist = [
    [0, 1, 1, 11, 10],
    [1, 0, 5, 1, 9],
    [1, 5, 0, 6, 10],
    [11, 1, 6, 0, 7],
    [10, 9, 10, 7, 0]
]

POP_SIZE = 4
MUTATION_PROB = 0.01
GENERATIONS = 20


# =========================
# 2. СОЗДАНИЕ ОСОБИ
# =========================
def create_individual():
    route = list(range(5))
    random.shuffle(route)
    return route


# =========================
# 3. длина маршрута
# =========================
def length(route):
    total = 0
    for i in range(len(route) - 1):
        total += dist[route[i]][route[i + 1]]

    # возврат в начальный город
    total += dist[route[-1]][route[0]]
    return total


# =========================
# 4. ОТБОР
# =========================
def selection(population):
    # сортировка по качеству
    sorted_pop = sorted(population, key=length)

    # берём лучших
    return sorted_pop[:2]


# =========================
# 5. СКРЕЩИВАНИЕ
# =========================
def crossover(parent1, parent2):
    size = len(parent1)

    i, j = sorted(random.sample(range(size), 2))

    child = [None] * size

    # копируем сегмент
    child[i:j] = parent1[i:j]

    # начинаем с позиции j
    p2_index = j % size

    for k in range(size):
        pos = (j + k) % size  # циклический обход

        if child[pos] is None:
            while parent2[p2_index] in child:
                p2_index = (p2_index + 1) % size

            child[pos] = parent2[p2_index]
            p2_index = (p2_index + 1) % size

    return child

# =========================
# 6. МУТАЦИЯ
# =========================
def mutate(route):
    if random.random() < MUTATION_PROB:
        i, j = random.sample(range(len(route)), 2)
        route[i], route[j] = route[j], route[i]


# =========================
# 7. ОСНОВНОЙ ЦИКЛ
# =========================
def genetic_algorithm():

    # 🔹 Шаг 1: инициализация
    population = [create_individual() for _ in range(POP_SIZE)]

    for generation in range(GENERATIONS):

        print(f"\nПоколение {generation}")

        # 🔹 Шаг 2: оценка
        for ind in population:
            print([i+1 for i in ind], "=", length(ind))

        # 🔹 Шаг 3: отбор
        parents = selection(population)

        # 🔹 Шаг 4–5: скрещивание + мутация
        new_population = parents.copy()

        # создаём пары родителей
        while len(new_population) < POP_SIZE:

            # берём одну пару
            p1, p2 = parents[0], parents[1]

            # два ребёнка
            child1 = crossover(p1, p2)
            child2 = crossover(p2, p1)

            mutate(child1)
            mutate(child2)

            new_population.append(child1)

            if len(new_population) < POP_SIZE:
                new_population.append(child2)

        # 🔹 Шаг 6: новое поколение
        population = new_population

    # 🔹 итог
    best = min(population, key=length)

    print("\nЛучшее решение:")
    print([i+1 for i in best], "=", length(best))


genetic_algorithm()