<?php
  //s'inicia la sessió per poder utilitzar les variables $_SESSION
  session_start();
  $conn=mysqli_connect("localhost","root","","atenea");
    if (($_POST['password']!="") & ($_POST['username']!="")){
        $sql = "SELECT * FROM estudiants WHERE Nom='".$_POST['username']."' AND password='".($_POST['password'])."'";
        $rs=$conn->query($sql);
        if ( $row = $rs->fetch_array(MYSQLI_BOTH)){
             //guardem la info obtinguda a les variables $_SESSION
             $_SESSION['S_idEstudiant']=$row[0];
             $_SESSION['S_Nom']=$row[1];
             //per tal de que no torni a apareixer el contingut als botons
             $_POST['password']="";$_POST['username']="";
             //redirigeix la web al doc principal.p
             header ("Location: \Web3\principal.php ");
        	 exit;
        }
        else
        {
           $_POST['Usuari']="";
           $_POST['Contrasenya']="";
           //si la contrasenya es incorrecte es redirigeix
           header ("Location: \Web3\login_incorrect.html ");
        }
    }else{
      //si es pren el boto de login per no hi a res escrit
      header ("Location: \Web3\login.html ");
    }


 ?>
