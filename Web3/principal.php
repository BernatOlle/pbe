<?php
session_start();
 ?>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Atenea</title>
	 <link rel="shortcut icon" href="favicon.ico" />
	<link rel="stylesheet" href="style.css">
</head>
<body>
	<form class="login-form" method="post" action="filtres.php">
	<div class="contenedor2">
		<div class="divi">
				<div class=columna1>
						 <h4> Welcome: <?php echo $_SESSION['S_Nom'] ?></h4>
				</div>
				<div class="botons">
						<button class="button2" type="button" onclick="location.href='login.html'">Logout</button>
				</div>
		</div>
		<div class="divi">
			<div class="columna1">
						<input class="controls" type="text" name="Buscador" placeholder="Buscador">
			</div>
			<div class="botons">
						<input  class="button2" type="submit" name="send" value="send"></input>
			</div>
		</div>
		<div>
			<?php
				if(!empty($_SESSION['S_result'])){
          $resultat=array();
          $resultat=json_decode($_SESSION['S_result']);
          $_SESSION['S_result']=null;
          $keys=array();
          if(!empty($resultat)){
          foreach($resultat[0] as $key => $value){
                $keys[] = $key;
            }
          $num_columnas= count($keys);
			?>
          <table class=taula>
            <caption class=titol ><?php echo $_SESSION['S_taula'] ?></caption>
            <?php
            $k=0;
            while($k<$num_columnas){
                  $valor=$keys[$k];
            ?>
              <th><?php echo $valor ?></th>
              <?php
              $k=$k+1;
            }
                $i=0;
                while($i<$_SESSION['S_files']){
                  $j=0;
                  ?>
                  <tr>
                    <?php
                  while($j<$num_columnas){
                    $valor=$keys[$j];
              ?>

                  <td class=columnas><?php  echo $resultat[$i]->$valor ?></td>

             <?php
                    $j=$j+1;
                  }?>
                </tr>
              <?php
                 $i=$i+1;
                   }
                 }
                 else{
                    ?>
                      <td> Search error</td>
                    <?php
                 }
     }

              ?>
          </table>
      </div>
		</div>
</form>
</body>
</html>
