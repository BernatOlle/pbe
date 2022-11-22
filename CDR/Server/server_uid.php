<?php
    //connexio amb la base de dades de mySql 
    $conn=mysqli_connect("localhost","root","","atenea"); 
 
    //Agafem fins el la primera
    $uid = $_SERVER['PATH_INFO'];
    $uid = str_replace("/","",$uid);
    

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
    


?>
