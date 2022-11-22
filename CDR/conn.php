<?php
    $conn=mysqli_connect("localhost","root","","atenea");
    $now = getdate();
    $datanow=$now["year"]."-".$now["mon"]."-".$now["mday"];
    /*Filtres a fer:
        - Limit de tasques timetables?limit=1 // date[gte]=now&limit=1
        - date[gte] tasques a partir de data   date[gte]=now&limit=1&id_Estudiant=1
        - mark[lt] nota mes baixa que mark[lt]=5
        - mark[ht] nota mes alta que mark[ht]=5  
        - hour[gt] mes tard q aquesta hora day=Fri&hour[gt]=08:00
        */
    // "tasks?limit=1"

    $header=$_SERVER['PATH_INFO'];
    $headers=explode("/",$header);

    $uid=$headers[1];//12345678
    $header=$headers[2];
    $sql = "SELECT * FROM estudiants WHERE uid='$uid'";
    $res=mysqli_query($conn,$sql);

    if ( $row = $res->fetch_array(MYSQLI_BOTH)){
        $id_Estudiant=$row[0];
    }



    $query=$_SERVER['QUERY_STRING'];


    $header=str_replace("/","",$header);
    $query=str_replace("%5B","[",$query);
    $query=str_replace("%5D","]",$query);
    
    
    $sql= "SELECT * FROM $header ";
    if($header=='timetables' && empty($query)){
        if($now['wday']==1 ){
            $sql.="ORDER BY FIELD (day,'Mon','Tue','Wed','Thu','Fri') ASC, hour";
        }else if($now['wday']==2){
            $sql.="ORDER BY FIELD (day,'Tue','Wed','Thu','Fri','Mon') ASC, hour";
        }else if($now['wday']==3){
            $sql.="ORDER BY FIELD (day,'Wed','Thu','Fri','Mon','Tue') ASC, hour";
        }else if($now['wday']==4){
            $sql.="ORDER BY FIELD (day,'Thu','Fri','Mon','Tue','Wed') ASC, hour";
        }else if($now['wday']==5){
            $sql.="ORDER BY FIELD (day,'Fri','Mon','Tue','Wed','Thu') ASC, hour";
        }else{
            $sql.="ORDER BY FIELD (day,'Mon','Tue','Wed','Thu','Fri') ASC, hour";
        }
        
    }
    if(!empty($query)){
            
           // $sql=$sql."WHERE ";

            $filtres=explode("&",$query);
             $i=0;
            while($filtres[$i] && !str_contains($filtres[$i],'limit')){
                if($i==0){
                    $sql=$sql."WHERE ";
                }
                 if($i>0 && !str_contains($query,'limit')){
                      $sql=$sql." AND ";
                      
                 }
                $filtres2=explode("=",$filtres[$i]);
                
                if(str_contains($filtres[$i],'[lt]')){
                    $sql=$sql."mark"."<=".$filtres2[1];
                    
   
                } 
                else if(str_contains($filtres[$i],'[ht]')){
                    $sql=$sql."mark".">=".$filtres2[1];
                
                    

                }else if(str_contains($filtres[$i],'hour[gt]')){
                    $sql=$sql." hour >= '".$filtres2[1]."'";
                    
                }else if(str_contains($filtres[$i],'date[gte]')){
                    if($filtres2[1]=="now"){
                        $data=$datanow;
                    }else{
                        $data=$filtres2[1];
                    }
                    $sql=$sql." date >= '".$data."'";
                
                }
                else{
                    $sql=$sql.$filtres2[0]."='".$filtres2[1]."'";

                }
                $i=$i+1;


            }
                    
         }  
    
    
       
    if($header=='marks'){
        if(empty($query)){
            $sql.=" WHERE ";
        }else{
            $sql.= " AND ";
        }
        $sql.=" students = '".$id_Estudiant."' ORDER BY mark DESC";
    }else if($header=='tasks'){
        $sql.=" ORDER BY Date ASC";
    }
    
    if(str_contains($query,'limit')){

        $filtres2=explode("=",$filtres[$i]);
        $sql=$sql." limit ".$filtres2[1];
        
        
    }

   
    $result=mysqli_query($conn,$sql);
    $array=array(array());
    $j=0;
    while($r = mysqli_fetch_array($result)){
        if($header=="timetables"){
            $array[$j]['day']=$r[0];
            $array[$j]['hour']=$r[1];
            $array[$j]['subject']=$r[2];
            $array[$j]['room']=$r[3];
        }

        else if($header=="tasks"){
            $array[$j]['date']=$r[0];
            $array[$j]['subject']=$r[1];
            $array[$j]['name']=$r[2];
        }
        else if($header=="marks"){
            $array[$j]['subject']=$r[0];
            $array[$j]['mark']=$r[1];
            $array[$j]['name']=$r[2];
        }

        $j++;

    }
$json_result= json_encode($array, 128);
echo $json_result;
//print("<pre>".$json_result."</pre>");

$conn->close();

    
?>