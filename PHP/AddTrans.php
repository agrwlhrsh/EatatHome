<?php
    date_default_timezone_set('Asia/Calcutta');
    $a = "d-m-Y H:i:s";
    $t = date($a);
    $oid = $_POST["oid"];
    $id = $_POST["id"];
    $tid = $_POST["tid"];
    $time = $t;
    $amount = $_POST["amount"];
    $transtatus = $_POST["transtatus"];
    $transtype = $_POST["transtype"];
    $type = $_POST["type"];


    if($type === "CUST"){
        $id = "c".$id;
    }else{
        $id = "s".$id;
    }
    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;

    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }

    else{
        $statement = mysqli_prepare($conn, "INSERT INTO transaction (tid, amount, id, time, oid, transtype, transtatus) VALUES (?, ?, ?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($statement, 'sssssss', $tid, $amount, $id, $time, $oid, $transtype, $transtatus);
        mysqli_stmt_execute($statement);
        $response["success"] = true;
    }
    echo json_encode($response);
    mysqli_close($conn);
?>