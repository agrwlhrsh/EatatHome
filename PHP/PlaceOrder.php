<?php
    $cid = $_POST["cid"];
    $mid = $_POST["mid"];
    $tid = $_POST["tid"];
    $amount = $_POST["amount"];
    $disc = $_POST["disc"];
    $deliv = $_POST["deliv"];
    $quan = $_POST["quan"];
    $total = $_POST["total"];
    $payby = $_POST["payby"];
    $time = $_POST["time"];
    $date = $_POST["date"];
    $status = $_POST["status"];
    $address = $_POST["address"];
    $sold = $_POST["sold"];
    $sid = $_POST["sid"];
    $s_balance = $_POST["s_balance"];
    $c_balance = $_POST["c_balance"];
    $sup_address = $_POST["sup_address"];
    $dbid = $_POST["dbid"];

//    $cid = "30";
//    $mid = "22";
//    $tid = "DJHGFDSDF";
//    $amount = "999";
//    $disc = "0";
//    $deliv = "20";
//    $quan = "1";
//    $total = "120";
//    $payby = "1";
//    $time = "1";
//    $date = "12-02-1992";
//    $status = "0";
//    $address = "Harsh Agarwal;50-C;bohra Hostel;Bangur Avenue;Kolkata;WB;700055";
//    $sold = "7";
//    $sid = "13";
//    $s_balance = "963852";
//    $c_balance = "741852";
//    $sup_address = "Harsh Kaka;nahi milega;kalika;Bangur Avenue;Kolkata;WB;700055";
//    $dbid ="1";
    

    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    
    if(mysqli_connect_errno()){
        echo json_encode($response);  
    }
    else{
        $sql = "INSERT INTO orders (mid, cid, tid, time, date, quan, amount, deliv, disc, total, payby, status, address) VALUES ('$mid', '$cid', '$tid', '$time', '$date', '$quan', '$amount', '$deliv', '$disc', '$total', '$payby', '$status', '$address')";
        if (mysqli_query($conn, $sql)) {
            $oid = mysqli_insert_id($conn);
            $sql2 = "INSERT INTO delivery (dbid, oid, saddr, caddr, picktime, deltime, status) VALUES ('$dbid', '$oid', '$sup_address', '$address', '', '', '0')";
            if (mysqli_query($conn, $sql2)) {
                $did = mysqli_insert_id($conn);
                $sql3 = "UPDATE menu SET sold = '".$sold."' WHERE mid = '".$mid."'";
                $sql4 = "UPDATE supplier SET balance = '".$s_balance."' WHERE sid = '".$sid."'";
                $sql5 = "UPDATE customer SET balance = '".$c_balance."' WHERE cid = '".$cid."'";
                if( ($result=mysqli_query($conn,$sql3)) && ($result2 = mysqli_query($conn,$sql)) && ($result3 = mysqli_query($conn,$sql5))){
                    $response["success"] = true;
                    $response["oid"] = $oid;
                    $response["did"] = $did;
                }
            } 
        } 
    }
    echo json_encode($response);
    mysqli_close($conn);
?>