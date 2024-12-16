<?php
header('Content-type: text/html');
$a = 10;
$b = 20;
echo nl2br("$a\n$b\n");

$c = $a+$b;
echo nl2br("$c\n");
$c *= 3;
print("$c");
echo nl2br("$c\n");
$c /= $b-$a;
echo nl2br("$c\n");

echo nl2br($p = 'Программа');
echo nl2br($b = 'работает');

$result = $p . ' ' . $b;

$result .= ' хорошо';
echo nl2br("$result\n");
$q = 5;
$w =7;
$q = $w + $q;
$w = $q - $w;
$q = $q - $w;
echo nl2br("$q $w\n");

for ($i = 23; $i < 79; $i++) {
    echo "$i ";
}
echo "<ul>";
for ($i = 1; $i <= 10; $i++) {
    echo "<li>Пункт $i</li>";
}
echo "</ul>";

$numbers = array();
for ($i = 0; $i < 100; $i++) {
    $numbers[] = rand(1, 1000);
}

echo "Вывод с использованием цикла while:<br>";
$i = 0;
while ($i < count($numbers)) {
    echo $numbers[$i] . " ";
    $i++;
}
echo "<br><br>";

echo "Вывод с использованием цикла foreach:<br>";
foreach ($numbers as $number) {
    echo $number . " ";
}

echo nl2br("\n");

$dayOfWeek = date("w");

switch ($dayOfWeek) {
    case 0:
        echo nl2br("Сегодня воскресенье.\n");
        break;
    case 1:
        echo nl2br("Сегодня понедельник.\n");
        break;
    case 2:
        echo nl2br("Сегодня вторник.\n");
        break;
    case 3:
        echo nl2br("Сегодня среда.\n");
        break;
    case 4:
        echo nl2br("Сегодня четверг.\n");
        break;
    case 5:
        echo nl2br("Сегодня пятница.\n");
        break;
    case 6:
        echo nl2br("Сегодня суббота.\n");
        break;
    default:
        echo nl2br("Сегодня что-то не так\n");
        break;
}


function getPlus10($number)
{
    $result = $number + 10;
    echo "Сумма числа $number и 10 равна: $result";
    echo nl2br("$c\n");
}

getPlus10(25);


?>