<?php require_once ("connections\MySiteDB.php");
$link = mysqli_connect ("localhost", "admin", "admin");
$db = "Lab5DB";

$select = mysqli_select_db($link, $db);

$selecting_all_from_notes = "SELECT * FROM notes";

$select_note = mysqli_query($link, $selecting_all_from_notes);

while ($note = mysqli_fetch_array($select_note)){
    echo $note ['id'], "<br>";
    echo $note ['created'], "<br>";
    echo $note ['title'], "<br>";
    echo $note ['article'], "<br>";}

?>

