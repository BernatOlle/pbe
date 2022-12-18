<?php
  session_start();
  $conn=mysqli_connect("localhost","root","","atenea");
  if (isset($_POST['Login'])){
    if (($_POST['password']!="") & ($_POST['username']!="")){
        //print "Hola";
        $sql = "SELECT * FROM estudiants WHERE Nom='".$_POST['username']."' AND password='".($_POST['password'])."'";
        $rs=$conn->query($sql);
        if ( $row = $rs->fetch_array(MYSQLI_BOTH)){
             $_SESSION['S_idEstudiant']=$row[0];
             $_SESSION['S_Nom']=$row[1];
             $_POST['password']="";$_POST['username']="";
             header ("Location: \Web3\principal.php ");
        	 exit;
        }
        else
        {
           $_POST['Usuari']="";
           $_POST['Contrasenya']="";
           header ("Location: \Web3\login_incorrect.html ");
        }
    }else{
      header ("Location: \Web\login_incorrect.html ");
    }
}

 ?>


//uid i php ha de estar junt
//contraseña incorrecte
//principal
