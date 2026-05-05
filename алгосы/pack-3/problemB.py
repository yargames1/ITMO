import sys

data = sys.stdin.read().split()
    
n = int(data[0])
rounds = []
final_scores = {}
idx = 1

# 1-й проход: запоминаем ходы и считаем финальные очки
for _ in range(n):
    name = data[idx]
    score = int(data[idx+1])
    idx += 2
    rounds.append((name, score))
    final_scores[name] = final_scores.get(name, 0) + score
    
# Находим максимальный финальный счёт
max_score = max(final_scores.values())

# Находим всех, кто набрал этот максимум
candidates = [name for name, sc in final_scores.items() if sc == max_score]

# Если победитель один, выводим его сразу
if len(candidates) == 1:
    print(candidates[0])
else:
    # 2-й проход: определяем, кто первым набрал >= max_score
    current_scores = {}
    for name, score in rounds:
        current_scores[name] = current_scores.get(name, 0) + score
        # Проверяем только кандидатов и условие "как минимум m"
        if name in candidates and current_scores[name] >= max_score:
            print(name)
            break