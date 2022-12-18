<?php
  session_start();
$conn=mysqli_connect("localhost","root","","atenea");
if (isset($_POST['send'])){
    if ($_POST['Buscador']!=""){
        $query=explode("?",$_POST['Buscador']);
        $taula=$query[0];
        if(!empty($query[1])){
        $filtre=$query[1];
      }

      if($taula == 'estudiants'){
        header ("Location: \Web3\principal.php ");
      }else{
        if($taula=='marks'){
            $sql = "SELECT `subject`,`mark`, `name` FROM $taula ";
        }else{
            $sql= "SELECT * FROM $taula ";
        }

        $now = getdate();
        $day=$now['wday'];
        $week=array("0"=>"Mon","1"=>"Tue","2"=>"Wed","3"=>"Thu","4"=>"Fri");


        if($taula=='timetables' && empty($filtre)){
            $sql.="ORDER BY FIELD (day,'".$week[fmod($day,5)]."','".$week[fmod(($day+1),5)]."','".$week[fmod(($day+2),5)]."','".$week[fmod(($day+3),5)]."','".$week[fmod(($day+4),5)]."') ASC, hour";
        }

        if(!empty($filtre)){
            $filtres=explode("&",$filtre);
             $i=0;
            while(!empty($filtres[$i]) && !str_contains($filtres[$i],'limit')){
                if($i==0){
                    $sql=$sql."WHERE ";
                }
                 if($i>0 && !str_contains($filtres[$i],'limit')){
                      $sql=$sql." AND ";

                 }
                $filtres2=explode("=",$filtres[$i]);
                $subjecte=explode("[",$filtres2[0]);
                $sql=$sql.$subjecte[0];
                if(!empty($subjecte[1])){
                switch ($subjecte[1]){
                    case "gt]":
                        $sql.=">";
                        break;
                    case "gte]":
                        $sql.=">=";
                        break;
                    case "lt]":
                        $sql.="<";
                        break;
                    case "lte]":
                        $sql.="<=";
                        break;
                }
              }else{
                $sql.="=";
              }
                if($filtres2[1]=="now"){
                    $filtres2[1]=$datanow;
                }

            $sql.="'".$filtres2[1]."'";
            $i=$i+1;
            }
        }


        if($taula=='marks'){
            if(empty($filtre) || str_contains($filtre,'limit') ){
                $sql.=" WHERE ";
            }else{
                $sql.= " AND ";
            }
            $sql.=" students = '".$_SESSION['S_idEstudiant']."' ORDER BY mark DESC";
            echo $sql;
        }else if($taula=='tasks'){
            $sql.=" ORDER BY Date ASC";
        }
        if(str_contains($filtres[$i],'limit')){
            $filtres2=explode("=",$filtres[$i]);
            $sql.= " LIMIT ".$filtres2[1];
        }
        if(mysqli_query($conn,$sql)){
        $result=mysqli_query($conn,$sql);
        $array=array();
        $j=0;
        while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
            $array[$j]=$row;
            $j++;
        }if(empty($array)){
          echo ("ERROR A LA BUSQUEDA");
          header ("Location: \Web3\principal.php ");
        }

    $_SESSION['S_taula']=$taula;
    $_SESSION['S_files']=$j;
    //buuidem el Buscador
    $_POST['Buscador']="";
    $json_result= json_encode($array, 128);
    $_SESSION['S_result']= $json_result;
    //redirigim a la pagina principal
    header ("Location: \Web3\principal.php ");
  }
}
}else{
  //redirigim a la pagina principal
  header ("Location: \Web3\principal.php ");
}

}else{
  echo "ERROR";
}

 ?>
