<?php

    $id = $_POST["id"];
    $amount = $_POST["amount"];
    date_default_timezone_set('Asia/Calcutta');
    $a = "d-m-Y H:i:s";
    $t = date($a);
    $oid = "0";
    $time = $t;
    $transtatus = "1";
    $transtype = "3";
    
   
    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }
    else{
        $sql = "UPDATE supplier SET balance = '0' WHERE sid = '".$id."'";
        if ($result=mysqli_query($conn,$sql))
        {
            $id = "s".$id;
            $sql2 = "INSERT INTO transaction (amount, id, time, oid, transtype, transtatus) VALUES ('$amount', '$id', '$time', '$oid', '$transtype', '$transtatus')";
            if(mysqli_query($conn,$sql2)){
                $response["success"] = true;                
            }
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>