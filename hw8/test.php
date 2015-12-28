<?php
header("Content-type:text/html;charset=utf8");
include("Json.php");
$name = $_POST['name'];
$sex = $_POST['sex'];
$age = $_POST['age'];
$json_arg = array('name'=>$name,'sex'=>$sex,'age'=>$age);
$json = new JSON;
$json_result = $json->encode($json_arg);
echo $json_result;
?>

