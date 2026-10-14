"""
Система поддержки принятия решений для НРИ.

Файл: recommendation_system.py
Зависимость: SWI-Prolog + pyswip
База знаний: knowledge_base.pl (лежит рядом с этим файлом)

Программа:
1. принимает вопросы на русском языке;
2. определяет намерение пользователя;
3. извлекает все найденные сущности (персонажи, игроки, классы, расы,
   города и год);
4. выбирает запрос к Prolog в зависимости от числа и типа параметров;
5. выводит ответ на русском языке.

"""

from datetime import datetime
from pathlib import Path
import re
import sys
from pyswip import Prolog

# ---------------------------------------------------------------------------
# 1. ПОДКЛЮЧЕНИЕ БАЗЫ ЗНАНИЙ
# ---------------------------------------------------------------------------
YEAR = int(input("Введите нынешний год по игровому времени для продолжения: "))
BASE_DIR = Path(__file__).resolve().parent
KB_FILE = BASE_DIR / "knowledge_base.pl"

if not KB_FILE.is_file():
    print(f"Не найден файл базы знаний: {KB_FILE}")
    print("Поместите knowledge_base.pl рядом с recommendation_system.py.")
    sys.exit(1)

prolog = Prolog()
try:
    prolog.consult(str(KB_FILE))
except Exception as error:
    print(f"Не удалось загрузить базу знаний: {error}")
    sys.exit(1)

# ---------------------------------------------------------------------------
# 2. СЛОВАРИ СУЩНОСТЕЙ
# ---------------------------------------------------------------------------

CHARACTERS = {
    "торвальд": "torvald", "торвальда": "torvald",
    "торвальду": "torvald", "торвальдом": "torvald",
    "шмыг": "shmig", "шмыга": "shmig", "шмыгу": "shmig",
    "шмыгом": "shmig",
    "иллидан": "illidan", "иллидана": "illidan",
    "иллидану": "illidan", "иллиданом": "illidan",
    "сенши": "senshi", "сенши́": "senshi",
    "буга": "buga", "бугу": "buga", "бугой": "buga", "буге": "buga",
    "вуга": "vuga", "вугу": "vuga", "вугой": "vuga", "вуге": "vuga",
}

PLAYERS = {
    "иван": "ivan", "ивана": "ivan", "ивану": "ivan",
    "иваном": "ivan",
    "олег": "oleg", "олега": "oleg", "олегу": "oleg",
    "олегом": "oleg",
    "артем": "artem", "артема": "artem", "артему": "artem",
    "артемом": "artem", "артём": "artem", "артёма": "artem",
    "артёму": "artem", "артёмом": "artem",
    "ваня": "vanya", "вани": "vanya", "ване": "vanya",
    "ваней": "vanya",
}

CLASSES = {
    "маг": "mage", "мага": "mage", "магом": "mage", "маги": "mage",
    "магами": "mage", "магический класс": "mage", "магов": "mage",
    "волшебник": "mage", "волшебника": "mage", "волшебники": "mage", "волшебников": "mage",
    "бард": "bard", "барда": "bard", "бардом": "bard", "барды": "bard", "бардов": "bard",
    "разбойник": "rogue", "разбойника": "rogue",
    "разбойником": "rogue", "разбойники": "rogue", "разбойников": "rogue",
    "воин": "warrior", "воина": "warrior", "воином": "warrior",
    "воины": "warrior", "воинами": "warrior", "воинов": "warrior",
}

RACES = {
    "человек": "human", "человека": "human", "люди": "human",
    "людей": "human", "людьми": "human",
    "эльф": "elf", "эльфа": "elf", "эльфы": "elf", "эльфов": "elf",
    "гоблин": "goblin", "гоблина": "goblin", "гоблины": "goblin",
    "гоблинов": "goblin",
    "дварф": "dwarf", "дварфа": "dwarf", "дварфы": "dwarf",
    "дварфов": "dwarf",
}

CITIES = {
    "ада": "ada", "аду": "ada", "аде": "ada",
    "гага": "gaga", "гаге": "gaga", "гагу": "gaga",
    "рора": "rora", "роре": "rora", "рору": "rora",
}

DISPLAY_NAMES = {
    "torvald": "Торвальд", "shmig": "шмыг",
    "illidan": "Иллидан", "senshi": "Сенши",
    "buga": "Буга", "vuga": "Вуга",
    "ivan": "Иван", "oleg": "Олег", "artem": "Артём",
    "vanya": "Ваня",
    "mage": "маг", "bard": "бард", "rogue": "разбойник",
    "warrior": "воин",
    "human": "человек", "elf": "эльф", "goblin": "гоблин",
    "dwarf": "дварф",
    "ada": "Ада", "gaga": "Гага", "rora": "Рора",
}

# ---------------------------------------------------------------------------
# 3. РАСПОЗНАВАНИЕ НАМЕРЕНИЯ
# ---------------------------------------------------------------------------
# Проверяем длинные фразы раньше коротких, чтобы общее слово вроде
# «конфликтует» не перехватило более точный запрос про игроков

INTENTS = {
    "player_characters": [
        "какие персонажи принадлежат игроку",
        "каким персонажем играет",
        "какого персонажа играет",
        "за кого играет",
        "играет ли",
        "кто играет",
    ],
    "tension": [
        "между какими игроками возможен конфликт",
        "есть ли напряжение между",
        "напряжение между игроками",
        "с кем конфликтует игрок",
        "какие игроки конфликтуют",
    ],
    "conflict": [
        "могут ли конфликтовать",
        "может ли конфликтовать",
        "будут ли конфликтовать",
        "есть ли конфликт между",
        "возможен ли конфликт между",
        "могут ли враждовать",
        "с кем будет конфликт",
        "с кем конфликтует",
        "кто может конфликтовать",
        "какие персонажи враждуют",
        "кто враждует",
        "кто конфликтует",
        "найди конфликты",
        "конфликт между",
    ],
    "suppressed": [
        "кто подавит",
        "может ли подавить",
        "подавит ли",
    ],
    "birth": [
        "когда и где родился", "когда и где родилась",
        "когда родился", "когда родилась",
        "где родился", "где родилась",
        "место рождения", "год рождения",
        "кто родился в городе", "кто родилась в городе",
        "родился ли", "родилась ли",
    ],
    "history": [
        "кто знает историю",
        "кто родом из",
        "кто из города",
    ],
    "tank": [
        "кто может танковать", "кто умеет танковать",
        "кто способен танковать", "кто будет танком",
        "кто танкует", "может ли танковать",
        "умеет ли танковать", "танковать",
    ],
    "inspire": [
        "кто может вдохновлять", "кто умеет вдохновлять",
        "кто способен вдохновлять", "кто воодушевляет",
        "может ли вдохновлять", "умеет ли вдохновлять",
        "вдохновлять", "вдохновит ли",
    ],
    "age": [
        "сколько лет", "какой возраст", "возраст персонажа",
        "возраст у", "возраст",
    ],
    "race_search": [
        "найди всех",
        "какие персонажи относятся к расе",
        "кто относится к расе",
    ],
    "class_search": [
        "какие персонажи являются",
        "кто является",
        "какие персонажи относятся к классу",
        "кто относится к классу",
    ],
}


def normalize_text(text):
    """Нормализует текст для сравнения, не меняя смысл вопроса."""
    return " ".join(text.lower().replace("ё", "е").strip().split())


def display(value):
    """Возвращает человекочитаемое имя для значения из Prolog."""
    return DISPLAY_NAMES.get(str(value), str(value))


def find_all_aliases(text, aliases):
    """
    Возвращает все найденные значения из словаря.
    Границы слова не позволяют найти, например, «маг» внутри другого слова.
    """
    text = normalize_text(text)
    found = []

    for alias in sorted(aliases, key=len, reverse=True):
        alias_norm = normalize_text(alias)
        pattern = rf"(?<![а-яa-z]){re.escape(alias_norm)}(?![а-яa-z])"

        if re.search(pattern, text):
            value = aliases[alias]
            if value not in found:
                found.append(value)
    return found


def detect_intent(message):
    """Определяет тип вопроса по наиболее длинной совпавшей фразе."""
    text = normalize_text(message)
    phrases = [
        (normalize_text(phrase), intent)
        for intent, variants in INTENTS.items()
        for phrase in variants
    ]
    phrases.sort(key=lambda item: len(item[0]), reverse=True)

    for phrase, intent in phrases:
        if phrase in text:
            return intent

    return None


def extract_year(text):
    """Извлекает год, например из «в 1830 году» или «год: 1830»."""
    match = re.search(
        r"(?<!\d)(\d{3,4})(?!\d)(?:\s*года|\s*годом|\s*г\.?)?",
        normalize_text(text),
    )
    return int(match.group(1)) if match else None


def extract_entities(message):
    """
    Извлекает все сущности, а не только первую.
    Это важно для запросов, в которых участвуют два персонажа,
    два игрока или несколько ограничений одновременно.
    """
    print("characters", find_all_aliases(message, CHARACTERS),
          "players", find_all_aliases(message, PLAYERS),
          "classes", find_all_aliases(message, CLASSES),
          "races", find_all_aliases(message, RACES),
          "cities", find_all_aliases(message, CITIES),
          )
    return {
        "characters": find_all_aliases(message, CHARACTERS),
        "players": find_all_aliases(message, PLAYERS),
        "classes": find_all_aliases(message, CLASSES),
        "races": find_all_aliases(message, RACES),
        "cities": find_all_aliases(message, CITIES),
        "year": extract_year(message),
    }


# ---------------------------------------------------------------------------
# 4. ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ДЛЯ PROLOG
# ---------------------------------------------------------------------------

def query_all(query):
    """Выполняет запрос к Prolog и возвращает все решения."""
    try:
        return list(prolog.query(query))
    except Exception as error:
        print(f"Ошибка запроса к базе знаний: {error}")
        return []


def known_people():
    """Все персонажи и противники, известные базе."""
    rows = query_all("(char(X); enemy(X))")
    return sorted({str(row["X"]) for row in rows})


def known_players():
    """Все игроки, которые встречаются в фактах playing/2."""
    rows = query_all("playing(Player, _)")
    return sorted({str(row["Player"]) for row in rows})


def character_class(name):
    """Возвращает класс персонажа или None."""
    rows = query_all(f"char_info({name}, Class)")
    return str(rows[0]["Class"]) if rows else None


def character_race(name):
    """Возвращает расу персонажа или None."""
    rows = query_all(f"born_race({name}, Race)")
    return str(rows[0]["Race"]) if rows else None


def player_character(player):
    """Возвращает персонажа игрока или None."""
    rows = query_all(f"playing({player}, Character)")
    return str(rows[0]["Character"]) if rows else None


def apply_character_filters(names, entities):
    """
    Применяет дополнительные фильтры к списку персонажей.
    """
    result = []

    for name in names:
        if entities["characters"] and name not in entities["characters"]:
            continue

        if entities["classes"] and character_class(name) not in entities["classes"]:
            continue

        if entities["races"] and character_race(name) not in entities["races"]:
            continue

        if entities["players"]:
            owner_rows = query_all(f"playing(Player, {name})")
            owners = {str(row["Player"]) for row in owner_rows}
            if not owners.intersection(entities["players"]):
                continue

        result.append(name)

    return result


def unique_unordered_pairs(rows, key_a, key_b):
    """Удаляет обратные и повторяющиеся пары из результатов Prolog."""
    pairs = set()

    for row in rows:
        a, b = str(row[key_a]), str(row[key_b])
        if a != b:
            pairs.add(tuple(sorted((a, b))))

    return sorted(pairs)


def get_birth_year(name):
    """Возвращает год рождения персонажа или None, если данных нет."""
    rows = query_all(f"birth_info({name}, Year, _)")
    return int(rows[0]["Year"]) if rows else None


def is_born(name):
    """True, если персонаж уже родился в текущем игровом году."""
    year = get_birth_year(name)
    return year is None or year <= YEAR


# ---------------------------------------------------------------------------
# 5. УНИВЕРСАЛЬНЫЙ ОБРАБОТЧИК КОНФЛИКТОВ
# ---------------------------------------------------------------------------

def answer_conflict(entities):
    """
    Количество найденных персонажей определяет форму запроса:
      0 персонажей — показать все уникальные конфликтующие пары;
      1 персонаж   — найти всех его возможных оппонентов;
      2 персонажа  — проверить только указанную пару.
    Если задана раса или класс, фильтр применяется к паре целиком:
    хотя бы один участник должен соответствовать указанному свойству.
    """
    chars = entities["characters"]

    if len(chars) > 2:
        return [
            "В вопросе найдено больше двух персонажей. "
            "Уточните, какую пару нужно проверить, введите запрос с одной парой"
        ]

    if len(chars) == 2:
        a, b = chars
        if not is_born(a) or not is_born(b):
            unborn = [display(c) for c in (a, b) if not is_born(c)]
            return [
                "Конфликт невозможен: " + ", ".join(unborn) + " ещё не родился."
            ]

        found = bool(
            query_all(f"will_conflict({a}, {b})")
        )

        # Если указан дополнительный фильтр, проверяем его и для пары.
        if found and entities["races"]:
            found = any(
                character_race(name) in entities["races"] for name in (a, b)
            )
        if found and entities["classes"]:
            found = any(
                character_class(name) in entities["classes"] for name in (a, b)
            )

        if found:
            return [f"Да, {display(a)} и {display(b)} могут конфликтовать."]
        return [
            f"По текущим правилам конфликт между {display(a)} и "
            f"{display(b)} не установлен."
        ]

    # Для одного или нуля персонажей сначала получаем решения Prolog.
    pairs = unique_unordered_pairs(
        query_all("will_conflict(A, B)"), "A", "B"
    )
    results = []

    for a, b in pairs:
        if not is_born(a) or not is_born(b):
            continue
        if chars and chars[0] not in (a, b):
            continue

        # Если в вопросе указана раса или класс, как минимум один
        # участник пары должен соответствовать этому уточнению.
        if entities["races"] and not any(
                character_race(name) in entities["races"] for name in (a, b)
        ):
            continue
        if entities["classes"] and not any(
                character_class(name) in entities["classes"] for name in (a, b)
        ):
            continue

        results.append(f"{display(a)} — {display(b)}")

    if results:
        return ["Возможные конфликты:\n" + "\n".join(results)]
    return ["Подходящих конфликтующих пар не найдено."]


# ---------------------------------------------------------------------------
# 6. УНИВЕРСАЛЬНЫЙ ОБРАБОТЧИК НАПРЯЖЕНИЯ МЕЖДУ ИГРОКАМИ
# ---------------------------------------------------------------------------

def answer_tension(entities):
    """
    0 игроков — все пары игроков с напряжением;
    1 игрок   — найти игроков, с которыми у него есть напряжение;
    2 игрока  — проверить конкретную пару.
    """
    players = entities["players"]

    if len(players) > 2:
        return ["Укажите не более двух игроков для проверки конкретной пары."]

    if len(players) == 2:
        a, b = players
        if a == b:
            return ["Для проверки напряжения укажите двух разных игроков."]

        found = bool(
            query_all(f"party_tension({a}, {b})")
        )
        if found:
            return [f"Между игроками {display(a)} и {display(b)} возможно напряжение."]
        return [f"Напряжение между {display(a)} и {display(b)} не установлено."]

    pairs = unique_unordered_pairs(
        query_all("party_tension(A, B)"), "A", "B"
    )
    results = []

    for a, b in pairs:
        if players and players[0] not in (a, b):
            continue
        results.append(f"{display(a)} — {display(b)}")

    if results:
        return ["Возможное напряжение между игроками:\n" + "\n".join(results)]
    return ["Подходящих пар игроков не найдено."]


# ---------------------------------------------------------------------------
# 7. РОЖДЕНИЕ: ПЕРСОНАЖ, ГОД И ГОРОД — НЕЗАВИСИМЫЕ ПАРАМЕТРЫ
# ---------------------------------------------------------------------------

def answer_birth(entities):
    chars = entities["characters"]
    cities = entities["cities"]
    year = entities.get("year")  # <-- ДОБАВЛЕНО: извлекаем год

    # 1. Если задан конкретный персонаж и хотя бы один параметр (город/год),
    # проверяем факт и отвечаем «Да» или «Нет» (для вопросов "родился ли")
    if len(chars) == 1 and (cities or year is not None):
        name = chars[0]
        if not is_born(name):
            return [f"{display(name)} ещё не родился."]

        rows = query_all(f"birth_info({name}, Year, City)")
        for row in rows:
            row_year = int(row["Year"])
            city = str(row["City"])

            year_ok = year is None or row_year == year
            city_ok = not cities or city in cities

            if year_ok and city_ok:
                return [f"Да, {display(name)} родился в {display(city)} в {row_year} году."]

        # Если ни одна запись не подошла под фильтры
        details = []
        if year is not None:
            details.append(f"в {year} году")
        if cities:
            details.append(f"в городе {display(cities[0])}")
        return [f"Нет, {display(name)} не родился {' '.join(details)}."]

    # 2. Общая логика поиска (0 или >1 персонажей)
    if len(chars) > 1:
        names_to_check = chars
    else:
        names_to_check = chars or known_people()

    results = []
    unborn = []

    for name in names_to_check:
        if not is_born(name):
            unborn.append(display(name))
            continue

        rows = query_all(f"birth_info({name}, Year, City)")
        for row in rows:
            row_year = int(row["Year"])
            city = str(row["City"])

            # <-- ДОБАВЛЕНО: фильтрация по году
            if year is not None and row_year != year:
                continue
            if cities and city not in cities:
                continue

            results.append(
                f"{display(name)}: год рождения — {row_year}, "
                f"место рождения — {display(city)}."
            )

    # Если имя не задано, ищем по всей базе
    if not chars:
        results = []
        rows = query_all("birth_info(Character, Year, City)")
        for row in rows:
            name = str(row["Character"])
            row_year = int(row["Year"])
            city = str(row["City"])

            if row_year > YEAR:
                unborn.append(display(name))
                continue
            
            # <-- ДОБАВЛЕНО: фильтрация по году
            if year is not None and row_year != year:
                continue
            if cities and city not in cities:
                continue

            results.append(
                f"{display(name)}: год рождения — {row_year}, "
                f"место рождения — {display(city)}."
            )

    if unborn:
        results.insert(0, "Ещё не родились: " + ", ".join(unborn) + ".")

    if results:
        return results
    return ["По указанным параметрам сведения о рождении не найдены."]

# ---------------------------------------------------------------------------
# 8. ВОЗРАСТ И РАЗНИЦА В ВОЗРАСТЕ
# ---------------------------------------------------------------------------

def answer_age(entities):
    """
    0 персонажей — возраст всех персонажей и противников;
    1 персонаж   — возраст одного объекта;
    2 персонажа  — сравнение возрастов конкретной пары.
    Возраст рассчитывается приблизительно по году рождения.
    """
    chars = entities["characters"]

    if len(chars) > 2:
        return ["Для сравнения укажите не более двух персонажей."]

    names = chars or known_people()
    results = []

    for name in names:
        if not is_born(name):
            results.append(f"{display(name)}: ещё не родился.")
            continue
        rows = query_all(f"age({name}, Age, {YEAR})")
        if rows:
            results.append(f"{display(name)}: примерно {int(rows[0]['Age'])} лет.")

    if len(chars) == 2:
        if not is_born(chars[0]) or not is_born(chars[1]):
            unborn = [display(c) for c in chars if not is_born(c)]
            results.append(
                "Нельзя сравнить возраст: " + ", ".join(unborn) + " ещё не родился."
            )
        else:
            birth_a = query_all(f"birth_info({chars[0]}, YearA, _)")
            birth_b = query_all(f"birth_info({chars[1]}, YearB, _)")
            if birth_a and birth_b:
                age_a = YEAR - int(birth_a[0]["YearA"])
                age_b = YEAR - int(birth_b[0]["YearB"])
                difference = abs(age_a - age_b)
                if age_a > age_b:
                    comparison = f"{display(chars[0])} старше"
                elif age_b > age_a:
                    comparison = f"{display(chars[1])} старше"
                else:
                    comparison = "Персонажи одного возраста"
                results.append(
                    f"{comparison}; разница — примерно {difference} лет."
                )

    if results:
        return results
    return ["Возраст для указанных параметров не найден."]


def answer_age_difference(entities):
    """Находит пары, где первый персонаж старше второго более чем на 20 лет."""
    chars = entities["characters"]

    if len(chars) == 2:
        a, b = chars
        if not is_born(a) or not is_born(b):
            unborn = [display(c) for c in (a, b) if not is_born(c)]
            return [
                "Нельзя применить правило can_suppressed: "
                + ", ".join(unborn) + " ещё не родился."
            ]
        if query_all(f"can_suppressed({a}, {b})"):
            return [f"{display(a)} старше {display(b)} более чем на 20 лет."]
        if query_all(f"can_suppressed({b}, {a})"):
            return [f"{display(b)} старше {display(a)} более чем на 20 лет."]
        return [
            f"Разница в возрасте между {display(a)} и {display(b)} не превышает 20 лет по правилу can_suppressed/2."]

    rows = query_all("can_suppressed(Older, Younger)")
    results = []

    for row in rows:
        older, younger = str(row["Older"]), str(row["Younger"])
        if not is_born(older) or not is_born(younger):
            continue
        if chars and chars[0] not in (older, younger):
            continue
        results.append(
            f"{display(older)} старше {display(younger)} более чем на 20 лет."
        )

    return results or ["Подходящих пар с разницей более 20 лет не найдено."]


# ---------------------------------------------------------------------------
# 9. СПОСОБНОСТИ: ТАНКОВАНИЕ И ВДОХНОВЕНИЕ
# ---------------------------------------------------------------------------

def answer_ability(entities, predicate, ability_name):
    """
    Универсальная проверка одноаргументного правила

    После получения решений Prolog применяем дополнительные условия:
    персонаж, класс, раса и игрок
    """

    rows = query_all(f"{predicate}(Character)")
    names = sorted({str(row["Character"]) for row in rows})
    names = apply_character_filters(names, entities)

    if names:
        return [f"{ability_name}: " + ", ".join(display(name) for name in names) + "."]

    return [f"Персонажей, подходящих под условия для «{ability_name}», не найдено."]


# ---------------------------------------------------------------------------
# 10. ПОИСК ПО РАСЕ И КЛАССУ
# ---------------------------------------------------------------------------

def answer_race_search(entities):
    """Выводит персонажей/противников с указанной расой, при наличии фильтров."""
    names = known_people()
    results = []

    for name in names:
        race = character_race(name)
        if entities["races"] and race not in entities["races"]:
            continue
        if entities["classes"] and character_class(name) not in entities["classes"]:
            continue
        if entities["players"]:
            owners = {
                str(row["Player"])
                for row in query_all(f"playing(Player, {name})")
            }
            if not owners.intersection(entities["players"]):
                continue
        if entities["characters"] and name not in entities["characters"]:
            continue
        if race:
            results.append(f"{display(name)} — {display(race)}.")

    return results or ["Персонажей с указанными характеристиками не найдено."]


def answer_class_search(entities):
    """Выводит персонажей/противников с указанным классом, применяя остальные фильтры."""
    names = known_people()
    results = []

    for name in names:
        char_class = character_class(name)
        if entities["classes"] and char_class not in entities["classes"]:
            continue
        if entities["races"] and character_race(name) not in entities["races"]:
            continue
        if entities["characters"] and name not in entities["characters"]:
            continue
        if entities["players"]:
            owners = {
                str(row["Player"])
                for row in query_all(f"playing(Player, {name})")
            }
            if not owners.intersection(entities["players"]):
                continue
        if char_class:
            results.append(f"{display(name)} — класс {display(char_class)}.")

    return results or ["Персонажей с указанными характеристиками не найдено."]


# ---------------------------------------------------------------------------
# 11. СВЯЗЬ ИГРОКА С ПЕРСОНАЖЕМ
# ---------------------------------------------------------------------------

def answer_player_characters(entities):
    """
    playing(Player, Character) — отношение с двумя аргументами.
    Если заданы и игрок, и персонаж, проверяется конкретная связь.
    Если задан один параметр, ищутся связанные с ним объекты.
    Если параметров нет, выводятся все связи.
    """
    players = entities["players"]
    chars = entities["characters"]

    if len(players) > 1 or len(chars) > 1:
        return ["Для проверки связи укажите одного игрока и одного персонажа."]

    if players and chars:
        player, char = players[0], chars[0]
        found = bool(query_all(f"playing({player}, {char})"))
        if found:
            return [f"Да, игрок {display(player)} играет за {display(char)}."]
        return [f"В базе нет факта, что игрок {display(player)} играет за {display(char)}."]

    rows = query_all("playing(Player, Character)")
    results = []

    for row in rows:
        player, char = str(row["Player"]), str(row["Character"])
        if players and player not in players:
            continue
        if chars and char not in chars:
            continue
        results.append(f"{display(player)} играет за {display(char)}.")

    return results or ["Связей между указанными игроками и персонажами не найдено."]


# ---------------------------------------------------------------------------
# 12. ПРАВИЛО knows_history/2
# ---------------------------------------------------------------------------

def answer_history(entities):
    """
    knows_history(Character, City) — связь персонажа с местом рождения.
    Можно искать по персонажу, по городу или по обоим параметрам.
    """
    chars = entities["characters"]
    cities = entities["cities"]

    if len(chars) > 1 or len(cities) > 1:
        # Если названы несколько объектов одного типа, проверяем каждую
        # комбинацию подходящих значений и не дублируем ответы.
        char_candidates = chars or known_people()
        city_candidates = cities or sorted(CITIES.values())
        results = []

        for char in char_candidates:
            for city in set(city_candidates):
                if query_all(f"knows_history({char}, {city})"):
                    results.append(f"{display(char)} родом из города {display(city)}.")
        return sorted(set(results)) or ["Подходящих сведений не найдено."]

    if chars and cities:
        if query_all(f"knows_history({chars[0]}, {cities[0]})"):
            return [f"Да, {display(chars[0])} родом из города {display(cities[0])}."]
        return [f"В базе не подтверждено, что {display(chars[0])} родом из города {display(cities[0])}."]

    rows = query_all("knows_history(Character, City)")
    results = []

    for row in rows:
        char, city = str(row["Character"]), str(row["City"])
        if chars and char not in chars:
            continue
        if cities and city not in cities:
            continue
        results.append(f"{display(char)} родом из города {display(city)}.")

    return results or ["Подходящих сведений о месте рождения не найдено."]


# ---------------------------------------------------------------------------
# 13. СПРАВКА И ПРИМЕРЫ
# ---------------------------------------------------------------------------

def get_unique_names(dictionary):
    """Извлекает уникальные человекочитаемые имена из словаря сущностей."""
    names = set()
    for value in dictionary.values():
        if value in DISPLAY_NAMES:
            names.add(DISPLAY_NAMES[value])
    return sorted(names)


def show_help():
    """Отдельная подпрограмма справки со всеми основными командами."""
    characters = get_unique_names(CHARACTERS)
    players = get_unique_names(PLAYERS)
    classes = get_unique_names(CLASSES)
    races = get_unique_names(RACES)
    cities = get_unique_names(CITIES)

    print("""
================================================================
             СПРАВКА ПО СИСТЕМЕ ПОДДЕРЖКИ РЕШЕНИЙ
================================================================

ПОДДЕРЖИВАЕМЫЕ ФОРМУЛИРОВКИ ВОПРОСОВ (из парсера INTENTS):
""")

    for intent, phrases in INTENTS.items():
        print(f"  [{intent.upper().replace('_', ' ')}]")
        for phrase in phrases:
            print(f"    - {phrase.capitalize()}?")
        print()

    print(f"""
ДОСТУПНЫЕ СУЩНОСТИ ДЛЯ ПОДСТАНОВКИ:
  Персонажи: {", ".join(characters) if characters else "нет данных"}
  Игроки: {", ".join(players) if players else "нет данных"}
  Классы: {", ".join(classes) if classes else "нет данных"}
  Расы: {", ".join(races) if races else "нет данных"}
  Города: {", ".join(cities) if cities else "нет данных"}
  Текущий игровой год: {YEAR}


СЛУЖЕБНЫЕ КОМАНДЫ:
  помощь / help       — показать эту справку
  примеры / examples  — показать примеры
  выход / exit / quit — завершить работу
================================================================
""")


def show_examples():
    """
    Показывает примеры запросов.
    Каждая базовая фраза берётся из INTENTS,
    а сущности подставляются из реальных словарей.
    """
    characters = get_unique_names(CHARACTERS)
    players = get_unique_names(PLAYERS)
    classes = get_unique_names(CLASSES)
    races = get_unique_names(RACES)
    cities = get_unique_names(CITIES)

    examples = []

    # --- TANK: берём реальные фразы из INTENTS["tank"] ---
    if INTENTS.get("tank"):
        # Первая фраза из INTENTS как есть
        examples.append(INTENTS["tank"][0].capitalize() + "?")
        # Подстановка персонажа в фразу "может ли танковать"
        if characters and "может ли танковать" in INTENTS["tank"]:
            examples.append(
                f"Может ли {characters[0]} танковать?"
            )

    # --- INSPIRE: берём реальные фразы из INTENTS["inspire"] ---
    if INTENTS.get("inspire"):
        examples.append(INTENTS["inspire"][0].capitalize() + "?")
        # Подстановка расы в фразу "кто может вдохновлять"
        if races:
            examples.append(
                f"Кто из {races[0]}ов может вдохновлять?"
            )

    # --- CONFLICT: берём реальные фразы из INTENTS["conflict"] ---
    if INTENTS.get("conflict"):
        # Фраза "могут ли конфликтовать" из INTENTS с подстановкой двух персонажей
        if len(characters) >= 2 and "могут ли конфликтовать" in INTENTS["conflict"]:
            examples.append(
                f"Могут ли конфликтовать {characters[0]} и {characters[1]}?"
            )
        # Фраза "с кем конфликтует" из INTENTS с подстановкой персонажа
        if characters and "с кем конфликтует" in INTENTS["conflict"]:
            examples.append(
                f"С кем конфликтует {characters[0]}?"
            )

    # --- TENSION: берём реальные фразы из INTENTS["tension"] ---
    if INTENTS.get("tension"):
        # Фраза "конфликтуют ли" из INTENTS с подстановкой двух игроков
        if len(players) >= 2 and "конфликтуют ли" in INTENTS["tension"]:
            examples.append(
                f"Конфликтуют ли {players[0]} и {players[1]}?"
            )
        # Фраза "с кем конфликтует игрок" из INTENTS с подстановкой игрока
        if players and "с кем конфликтует игрок" in INTENTS["tension"]:
            examples.append(
                f"С кем конфликтует игрок {players[0]}?"
            )

    # --- BIRTH: берём реальные фразы из INTENTS["birth"] ---
    if INTENTS.get("birth"):
        # Фраза "родился ли" из INTENTS с подстановкой персонажа, города и года
        if characters and cities and "родился ли" in INTENTS["birth"]:
            examples.append(
                f"Родился ли {characters[0]} в {cities[0]} в {YEAR} году?"
            )
        # Фраза "когда и где родился" из INTENTS с подстановкой персонажа
        if characters and "когда и где родился" in INTENTS["birth"]:
            examples.append(
                f"Когда и где родился {characters[0]}?"
            )

    # --- PLAYER_CHARACTERS: берём реальные фразы из INTENTS["player_characters"] ---
    if INTENTS.get("player_characters"):
        # Фраза "играет ли" из INTENTS с подстановкой игрока и персонажа
        if players and characters and "играет ли" in INTENTS["player_characters"]:
            examples.append(
                f"Играет ли {players[0]} за {characters[0]}?"
            )

    # --- AGE: берём реальные фразы из INTENTS["age"] ---
    if INTENTS.get("age"):
        # Фраза "сколько лет" из INTENTS с подстановкой персонажа
        if characters and "сколько лет" in INTENTS["age"]:
            examples.append(
                f"Сколько лет {characters[0]}?"
            )

    # --- RACE_SEARCH: используем ТОЛЬКО "найди всех" с расами ---
    if INTENTS.get("race_search"):
        # Фраза "найди всех" из INTENTS с подстановкой расы
        if races:
            examples.append(
                f"Найди всех {races[0]}"
            )
        # Фраза "кто относится к расе" из INTENTS
        if races and "кто относится к расе" in INTENTS["race_search"]:
            examples.append(
                f"Кто относится к расе {races[0]}?"
            )

    # --- CLASS_SEARCH: НЕ используем "найди всех" (только для рас) ---
    # Используем другие фразы из INTENTS["class_search"]
    if INTENTS.get("class_search"):
        # Фраза "кто является" из INTENTS с подстановкой класса
        if classes and "кто является" in INTENTS["class_search"]:
            examples.append(
                f"Кто является {classes[0]}?"
            )
        # Фраза "какие персонажи относятся к классу" из INTENTS
        if classes and "какие персонажи относятся к классу" in INTENTS["class_search"]:
            examples.append(
                f"Какие персонажи относятся к классу {classes[0]}?"
            )

    print("\nПримеры запросов:")
    for example in examples:
        print(f"  - {example}")


# ---------------------------------------------------------------------------
# 14. ЕДИНАЯ ТОЧКА ОБРАБОТКИ СООБЩЕНИЙ
# ---------------------------------------------------------------------------

def process_message(message):
    """
    Общий маршрутизатор:
      1. проверяет служебные команды;
      2. определяет намерение;
      3. извлекает все сущности;
      4. направляет запрос нужному обработчику.

    Конкретные обработчики учитывают число параметров и при необходимости
    проверяют связь между двумя аргументами, а не фильтруют по одному.
    """
    text = normalize_text(message)

    if text in {"помощь", "help", "справка"}:
        show_help()
        return

    if text in {"примеры", "пример", "examples", "example"}:
        show_examples()
        return

    if text in {"выход", "exit", "quit"}:
        raise SystemExit

    intent = detect_intent(text)
    entities = extract_entities(text)

    if intent is None:
        print(
            "Не удалось определить тип вопроса. "
            "Введите «помощь», чтобы увидеть примеры."
        )
        return

    # Для вопросов о конфликте между игроками важно сначала проверить
    # намерение tension, иначе общий поиск конфликтов мог бы перехватить запрос.
    handlers = {
        "conflict": answer_conflict,
        "tension": answer_tension,
        "birth": answer_birth,
        "history": answer_history,
        "tank": lambda e: answer_ability(e, "can_tank", "Танковать могут"),
        "inspire": lambda e: answer_ability(e, "can_inspire", "Вдохновлять могут"),
        "age": answer_age,
        "suppressed": answer_age_difference,
        "race_search": answer_race_search,
        "class_search": answer_class_search,
        "player_characters": answer_player_characters,
    }

    handler = handlers.get(intent)

    answers = handler(entities)

    print("\nОтвет системы:")
    for answer in answers:
        print(f"- {answer}")


# ---------------------------------------------------------------------------
# 15. ЗАПУСК ДИАЛОГА
# ---------------------------------------------------------------------------

def main():
    """Запускает интерактивный цикл общения с пользователем."""
    print("Система поддержки принятия решений запущена.")
    print("Введите «помощь» для просмотра команд.")

    while True:
        try:
            message = input("\nВаш вопрос: ").strip()
            if not message:
                continue

            process_message(message)

        except SystemExit:
            print("Работа программы завершена.")
            break
        except (KeyboardInterrupt, EOFError):
            print("\nРабота программы завершена.")
            break


if __name__ == "__main__":
    main()
