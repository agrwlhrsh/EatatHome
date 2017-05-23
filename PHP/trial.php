<?php

$db = mysqli_connect('mysql.hostinger.in','u403310507_app','eat@home123','u403310507_app')
or die('Error connecting to MySQL server.');
?>

<html>
    <head>
    <title> Eat @ Home Delivery</title>
       <style>

    p.normal{
    
    font-weight: bold;
    font-size: 40px;
    font-family: "Times New Roman", Times, serif;
    color:white;
    padding-right: 20px;
    padding-bottom: 10px;
    padding-left: 400px;
    }
    p.abnormal{
    	 font-family: "Times New Roman", Times, serif;
    	 color:white;
    	 font-weight: bold;
    	 font-size: 20px;
    	 padding-left: 400px;
    	 
    }
    #asd{
        margin: 10px 10px 10px 10px;
        border-radius: 6px;
        border : 2px dotted;
        padding : 10px 10px 10px 400px;
        font-family : sans-serif;
    }
    #kkk{
    padding-top: 10px;
    padding-right: 30px;
    padding-bottom: 10px;
    padding-left: 80px;
    border: 1px solid black;
   
    background-color: #B80000;
    }
    img{
    	padding-left: 500px;
    }
    #katha{
        padding-left: 500px;
    }
    input[type=text],select{
	padding:5px 15px;
	 border: 2px solid red;
         border-radius: 4px;

    }
    input[type=submit] {
        margin-left:195px;
        padding:5px 5px 5px 5px; 
        background: #B80000; 
        font-weight: bold;
        cursor:pointer;
        -webkit-border-radius: 5px;
        border-radius: 5px;     
        color: White;
        font-family: arial;
    }
    </style>
    </head>
    <body>
        <div id = "kkk">
        <img src="http://eatathome.pe.hu/ic_launcher(1).png">
        <p class="normal"> Eat @ Home Delivery </p>
            <form method="post" id = "katha">
                <select id="mySelect"  name="did" onchange="submit()">
                    <option>Select Delivery ID </option>
                    <?php
                    $res=mysqli_query($db,"SELECT * FROM delivery WHERE status=0");
                    while($row=mysqli_fetch_array($res)) 
                    {
                    ?>
                    <option><?php echo $row["did"];?> </option>
                    <?php
                    }
                    ?>
                </select> 
            </form>            
        </div> 
        
    </body>
</html>
<?php
    $did = $_POST['did'];
    $db = mysqli_connect('mysql.hostinger.in','u403310507_app','eat@home123','u403310507_app')
        or die('Error connecting to MySQL server.');
    $res=mysqli_query($db,"SELECT oid, caddr, saddr FROM delivery WHERE did ='".$did."'");
    //$oid=mysql_fetch_array($oid);
    while($row=mysqli_fetch_array($res)) 
    {
        echo "<div id='asd'>";
        echo "<b>Delivery ID:  </b>".$did."<br><br>";
        echo "<b>Order ID:  </b>".$row["oid"]."<br><br>";
        echo "<b>Pickup Address:  </b>".$row["saddr"]."<br><br>";
        echo "<b>Delivery Address:  </b>".$row["caddr"]."<br><br>";
        echo "<form method=post>";
        echo "<input type=hidden name=oid value='".$row['oid']."'>";
        echo "<input type=submit value = 'DELIVER'>";
        echo "</form></div>";
    }
?>
<?php
    if(isset($_POST['oid'])){
        $oid = $_POST['oid'];
        echo "<br>";
        date_default_timezone_set('Asia/Calcutta');
        $a = "d-m-Y H:i:s";
        $t = date($a);
        $db = mysqli_connect('mysql.hostinger.in','u403310507_app','eat@home123','u403310507_app')
              or die('Error connecting to MySQL server.');

        $sql = "UPDATE delivery SET deltime='".$t."', picktime='".$t."', status='1' WHERE oid='".$oid."'";

        $orderupdate="UPDATE orders SET status='1' WHERE oid='".$oid."'";

        if (mysqli_query($db, $sql) && mysqli_query($db, $orderupdate) ) 
        {
            echo "OID <b>".$oid."</b> Delivered and Record updated successfully";
        } 
        else 
        {
            echo "Error updating record: " . mysqli_error($db);
        }
    }

?>
