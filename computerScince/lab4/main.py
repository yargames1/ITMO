import sqlite3
from sqlite3 import Error


def create_connection(path):
    connection = None
    try:
        connection = sqlite3.connect(path)
        print("Connection to SQLite DB successful")
    except Error as e:
        print(f"The error '{e}' occurred")

    return connection


def execute_query(connection, query):
    cursor = connection.cursor()
    try:
        cursor.execute(query)
        connection.commit()
        print("Query executed successfully")
    except Error as e:
        print(f"The error '{e}' occurred")


def execute_read_query(connection, query):
    cursor = connection.cursor()
    result = None
    try:
        cursor.execute(query)
        result = cursor.fetchall()
        return result
    except Error as e:
        print(f"The error '{e}' occurred")


connection = create_connection("C:\\Users\\Technopark\\PycharmProjects\\prog\\games_db.sqlite")

# ------------------------------------

create_users_table = """
CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nickname TEXT NOT NULL,
  steam_level INTEGER NOT NULL,
  location TEXT
);
"""
execute_query(connection, create_users_table)

create_games_table = """
CREATE TABLE IF NOT EXISTS games (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL
);
"""
execute_query(connection, create_games_table)

create_comments_table = """
CREATE TABLE IF NOT EXISTS comments (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  text TEXT NOT NULL,
  reaction BIT NOT NULL,
  user_id INTEGER NOT NULL,
  game_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) FOREIGN KEY (game_id) REFERENCES games (id)
);
"""
execute_query(connection, create_comments_table)

create_library_table = """
CREATE TABLE IF NOT EXISTS library (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER NOT NULL,
  game_id INTEGER NOT NULL,
  played_hours INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) FOREIGN KEY (game_id) REFERENCES games (id)
);
"""
execute_query(connection, create_library_table)

# ------------------------------------

create_users = """
INSERT INTO
  users (nickname, steam_level, location)
VALUES
  ('vzudin203', 7, NULL),
  ('Ephyr', 0, NULL),
  ('Muffen', 87, 'Switzerland'),
  ('DimazZaytsev', 5, NULL),
  ('Still', 27, 'Amsterdam, Noord-Holland, Netherlands');
"""
execute_query(connection, create_users)

create_games = """
INSERT INTO
  games (name)
VALUES
  ('Hollow Knight'),
  ('Only Up!'),
  ('Dota 2'),
  ('Half-Life'),
  ('Balatro');
"""
execute_query(connection, create_games)

create_comments = """
INSERT INTO
  comments (text, reaction, user_id, game_id)
VALUES
  ('С самого начала знал что игра отличная , но после того прошёл игру понял что это шедевр который стоит не так много как эмоции которые ты испытываешь при прохождении игры.Всем советую игра может и хардкорная , но слишком шикарна для этой вселенной.\n\n10/10 Ждём Силксонг', 1, 1, 1),
  ('Хорошая игра,но очень затягивает.', 1, 2, 3),
  ('Лучше потратьте деньги на шаурму, испытаете больше положительных эмоций, чем от этой игры\nIt`s better to spend money on a tasty treat, you will experience more positive emotions than from this game', 0, 3, 2),
  ('Мне понравилось игра. Прикольный шутер от первого лица, проходить паркуры и атмосфера игры. Я сожалел, когда выбрал сложность игры на высокий, думал попробовать проверить сложность игры и пипец было тяжело проходить, а так в целом понравилось игра.', 1, 4, 4),
  ('Великая игра, доказывающая, что всё гениальное - просто. Простой концепт, взявший классическую и многим известную систему, и связавший это с рог-лайт элементом. Итог - игра года.\nPS На деке играется очень хорошо.', 1, 5, 5);
"""
execute_query(connection, create_comments)

create_library = """
INSERT INTO
  library (user_id, game_id, played_hours)
VALUES
  (1, 1, 135),
  (2, 3, 6697),
  (3, 2, 1),
  (4, 4, 10),
  (5, 5, 17);
"""
execute_query(connection, create_library)

# ------------------------------------


# Добавление данных в каждую таблицу
add_user = """
INSERT INTO
    users (nickname, steam_level, location)
VALUES
  ('question mark', 49, NULL)
"""
execute_query(connection, add_user)

add_game = """
INSERT INTO
    games (name)
VALUES
  ('Magicka 2')
"""
execute_query(connection, add_game)

add_comment = """
INSERT INTO
    comments (text, reaction, user_id, game_id)
VALUES
  ('Я рад что balatro стала популярной игрой, при условии что в ней сложная прогрессия игровых очков. Играл ещё в закрытую демку, и концепт игры уже тогда понравился, хотя контента было мало. Всем хороших джокеров!', 1, 6, 5)
"""
execute_query(connection, add_comment)

add_library = """
INSERT INTO
  library (user_id, game_id, played_hours)
VALUES
  (6, 5, 13);
"""
execute_query(connection, add_library)

# ------------------------------------


# выбрать все записи из таблиц
def print_all_tables():
    print('--------------------')
    select_users = "SELECT * from users"
    users = execute_read_query(connection, select_users)

    for user in users:
        print(user)
    print('--------------------')

    select_games = "SELECT * from games"
    games = execute_read_query(connection, select_games)

    for games in games:
        print(games)
    print('--------------------')

    select_comments = "SELECT * from comments"
    comments = execute_read_query(connection, select_comments)

    for comments in comments:
        print(comments)
    print('--------------------')

    select_library = "SELECT * from library"
    library = execute_read_query(connection, select_library)

    for library in library:
        print(library)
    print('--------------------')


print("выбрать все записи из таблиц")
print_all_tables()

# составить запрос по извлечению данных с использованием JOIN
print("Сколько часов играли люди в игры, которым оставили коментарии?")
select_users_comments = """
SELECT
  users.nickname,
  games.name,
  library.played_hours
FROM
  comments
  INNER JOIN users ON users.id = comments.user_id 
  INNER JOIN games ON games.id = comments.game_id
  INNER JOIN library ON library.user_id = comments.user_id
"""
users_comments = execute_read_query(connection, select_users_comments)
for users_comment in users_comments:
    print(users_comment)
print("---------------------")

# составить запрос по извлечению данных с использованием WHERE и GROUP BY
print("Сколько часов кто играл в BALATRO?")
select_users_hours_in_balatro = """
SELECT
  users.nickname,
  played_hours
FROM
  library
  INNER JOIN users ON users.id = user_id 
  INNER JOIN games ON games.id = game_id
  WHERE games.name = "Balatro"
  GROUP BY played_hours
"""
users_hours_in_balatro = execute_read_query(connection, select_users_hours_in_balatro)
for users_hours in users_hours_in_balatro:
    print(users_hours)
print("---------------------")

# Составить два запроса, в которых будет вложенный SELECT-запрос (вложение с помощью WHERE.
print("Какие коиентарии оставил об играх Ephyr?")
select_user_Ephyr_comments = """
SELECT text, games.name
FROM comments
INNER JOIN games ON games.id = game_id
WHERE user_id IN (SELECT id FROM users WHERE nickname = "Ephyr")"""
user_Ephyr_comments = execute_read_query(connection, select_user_Ephyr_comments)
for comment in user_Ephyr_comments:
    print(comment)
print("---------------------")
print("в какие игры играл DimazZaytsev?")
select_user_DimazZaytsev_games = """
SELECT name
FROM games
WHERE id IN (SELECT game_id FROM library INNER JOIN users ON user_id = users.id WHERE users.nickname = "DimazZaytsev")"""
user_DimazZaytsev_games = execute_read_query(connection, select_user_DimazZaytsev_games)
for game in user_DimazZaytsev_games:
    print(game)
print("---------------------")

# составить 2 запроса с использованием UNION (объединение запросов).
print("Сколько часов в каких играх провели Muffen и Still?")
select_users_Muffen_and_Still_hours = """
SELECT
  users.nickname,
  games.name,
  played_hours
FROM
  library
  INNER JOIN users ON users.id = user_id 
  INNER JOIN games ON games.id = game_id
  WHERE users.nickname = "Muffen"
UNION
SELECT
  users.nickname,
  games.name,
  played_hours
FROM
  library
  INNER JOIN users ON users.id = user_id 
  INNER JOIN games ON games.id = game_id
  WHERE users.nickname = "Still"
"""
users_Muffen_and_Still_hours = execute_read_query(connection, select_users_Muffen_and_Still_hours)
for user in users_Muffen_and_Still_hours:
    print(user)
print("---------------------")
print("Какие коментарии оставляли Ephyr и vzudin203?")
select_users_Ephyr_and_vzudin203_comments = """
SELECT
  users.nickname,
  games.name,
  text
FROM
  comments
  INNER JOIN games ON games.id = game_id
  INNER JOIN users ON users.id = user_id
  WHERE users.nickname = "Ephyr"
UNION
SELECT
  users.nickname,
  games.name,
  text
FROM
  comments
  INNER JOIN games ON games.id = game_id
  INNER JOIN users ON users.id = user_id
  WHERE users.nickname = "vzudin203"
"""
users_Ephyr_and_vzudin203_comments = execute_read_query(connection, select_users_Ephyr_and_vzudin203_comments)
for user in users_Ephyr_and_vzudin203_comments:
    print(user)
print("---------------------")

# Составить 1 запрос с использованием DISTINCT. Если для демонстрации работы этого ключевого слова недостаточно
# данных – предварительно дополните таблицу.
print("У каких игр есть коментарии?")
select_games_with_comments = """
SELECT DISTINCT games.name
FROM comments
INNER JOIN games ON games.id = game_id
"""
games_with_comments = execute_read_query(connection, select_games_with_comments)
for game in games_with_comments:
    print(game)
print("---------------------")

# Обновить две записи в двух разных таблицах Вашей базы данных
update_user_steam_level = """
UPDATE
  users
SET
  steam_level = steam_level+1
WHERE
  id = 1 or id = 3
"""
execute_query(connection, update_user_steam_level)

update_user_1_played_hours_game_1 = """
UPDATE
  library
SET
  played_hours = played_hours+1
WHERE
  user_id = 1 and game_id = 1
"""
execute_query(connection, update_user_1_played_hours_game_1)

update_user_2_played_hours_game_3 = """
UPDATE
  library
SET
  played_hours = played_hours+1
WHERE
  user_id = 2 and game_id = 3
"""
execute_query(connection, update_user_2_played_hours_game_3)

# 8. Удалить по одной записи из каждой таблицы.

delete_comment_user_vzudin203 = """DELETE FROM comments WHERE user_id IN (SELECT id FROM users WHERE nickname = 
"vzudin203")"""
execute_query(connection, delete_comment_user_vzudin203)

delete_game_Magicka_2 = """DELETE FROM games WHERE name = 'Magicka 2'"""
execute_query(connection, delete_game_Magicka_2)

delete_library_user_vzudin203_game_Hollow_Knight = """DELETE FROM library WHERE user_id IN (SELECT id FROM users WHERE nickname = 
'vzudin203') and game_id IN (SELECT id FROM games WHERE name = 'Hollow Knight')"""
execute_query(connection, delete_library_user_vzudin203_game_Hollow_Knight)

delete_user_1 = """DELETE FROM users WHERE id = 1"""
execute_query(connection, delete_user_1)

print_all_tables()

# Удалите все записи в одной из таблиц.

delete_comments = """DELETE FROM comments"""
execute_query(connection, delete_comments)

print_all_tables()