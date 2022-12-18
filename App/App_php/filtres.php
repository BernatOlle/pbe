<?php
$conn = mysqli_connect("localhost", "root", "", "atenea");
$now = getdate();
$datanow = $now["year"] . "-" . $now["mon"] . "-" . $now["mday"];

$idEstudiant;
$url = $_SERVER['PATH_INFO'];
//$url="/D70CCF64/timetables";


$query = $_SERVER['QUERY_STRING'];
//$query="subject=PBE";

$headers = explode("/", $url);
if(!empty($headers[2])){
    $table=$headers[2];
}
$uid=$headers[1];

if(empty($table)){
    $sql = "SELECT * FROM estudiants WHERE uid='$uid'";
    $res=mysqli_query($conn,$sql);
    
    
    $array=array();
    if ( $row = $res->fetch_array(MYSQLI_BOTH)){
        $array['Username']=$row['Nom'];
        
    }else{
        $array['Username']='ERROR';
    }
    $json=json_encode($array);
    echo $json;


}
else{

    $sql = "SELECT idEstudiant FROM estudiants WHERE Nom='$uid'";
    $res=mysqli_query($conn,$sql);
  
    if ( $row = $res->fetch_array(MYSQLI_BOTH)){
        $student_id=$row['idEstudiant'];
        
    }else{
        $student_id='ERROR';
    }


$query = str_replace("%5B", "[", $query);
$query = str_replace("%5D", "]", $query);

if ($table == 'marks') {
    $sql = "SELECT `subject`,`mark`, `name` FROM $table ";
} else {
    $sql = "SELECT * FROM $table ";
}

$now = getdate();
$day = $now['wday'];
$week = array("0" => "Mon", "1" => "Tue", "2" => "Wed", "3" => "Thu", "4" => "Fri");

if ($table == 'timetables' && empty($query)) {
    $sql .= "ORDER BY FIELD (day,'" . $week[fmod($day, 5)] . "','" . $week[fmod(($day + 1), 5)] . "','" . $week[fmod(($day + 2), 5)] . "','" . $week[fmod(($day + 3), 5)] . "','" . $week[fmod(($day + 4), 5)] . "') ASC, hour";
}

if (!empty($query)) {
    $query1 = explode("&", $query);
    $i = 0;
    while (!empty($query1[$i]) && !str_contains($query1[$i], 'limit')) {
        if ($i == 0) {
            $sql = $sql . "WHERE ";
        }
        if ($i > 0 && !str_contains($query1[$i], 'limit')) {
            $sql = $sql . " AND ";
        }
        $value = explode("=", $query1[$i]);
        $column = explode("[", $value[0]);
        $sql = $sql . $column[0];
        if (!empty($column[1])) {
            switch ($column[1]) {
                case "gt]":
                    $sql .= ">";
                    break;
                case "gte]":
                    $sql .= ">=";
                    break;
                case "lt]":
                    $sql .= "<";
                    break;
                case "lte]":
                    $sql .= "<=";
                    break;
            }
        } else {
            $sql .= "=";
        }

        if (!empty($column[1]) && $column[1] == "now") {
            $value[1] = $datanow;
        }
        $sql .= "'" . $value[1] . "'";
        $i = $i + 1;
    }
}


if ($table == 'marks') {
    if (empty($query)) {
        $sql .= " WHERE ";
    } else {
        $sql .= " AND ";
    }
    $sql .= " students = '" .$student_id. "' ORDER BY mark DESC";
} else if ($table == 'tasks') {
    $sql .= " ORDER BY Date ASC";
}

if (mysqli_query($conn, $sql)) {
    
    $result = mysqli_query($conn, $sql);
    $array = array();
    $j = 0;
    while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
        $array[$j] = $row;
        $j++;
    }
    if (empty($array)) {
        echo ("ERROR A LA BUSQUEDA");
    }
    $json_result = json_encode($array, 128);
    echo $json_result;
}
}