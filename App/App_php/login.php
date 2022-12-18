<?php
    session_start();

    $conn=mysqli_connect("localhost","root","","atenea");

    $uid = $_SERVER['PATH_INFO'];
    $values=explode("/",$uid);
    $username=$values[1];
    $password=$values[2];

    
    $sql = "SELECT * FROM estudiants WHERE Nom='".$username."' AND password='".$password."' ";
    $res=mysqli_query($conn,$sql);

    
    $array=array();
    if ( $row = $res->fetch_array(MYSQLI_BOTH)){
        $array['Username']=$row['Nom'];
        $array['idEstudiant']=$row['idEstudiant'];
    }else{
        $array['Username']='ERROR';
    }
    $json=json_encode($array);
    echo $json;
    


?>