<?php
    $sid = $_POST["sid"];
    $aid = $_POST["aid"];
    $items = $_POST["items"];
    $mname = $_POST["mname"];
    $quan = $_POST["quan"];
    $cost = $_POST["cost"];
    $veg = $_POST["veg"];
    $time = $_POST["time"];
    $date = $_POST["date"];
    $sold = "0";

//    $aid = "1";
//    $items = "Chilli Paneer, Chowmein";
//    $mname = "Chineese";
//    $quan = "10";
//    $cost = "100";
//    $veg = "1";
//    $time = "1";
//    $date = "12-03-1992";
//    $sold = "0";
    
    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;

    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }

    else{
        $statement = mysqli_prepare($conn, "INSERT INTO menu (mname, items, sid, cost, time, date, veg, quan, sold, aid) VALUES (?, ?, ?, ?, ?, ?,?,?,?,?)");
        mysqli_stmt_bind_param($statement, 'ssssssssss', $mname, $items, $sid, $cost, $time, $date, $veg, $quan, $sold, $aid);
        mysqli_stmt_execute($statement);
        $response["success"] = true;
//        THIS PART SHALL CONTAIN MENU ID WHICH WIL B OTAINED BY ORDERING AND GETTING LAST ONE
//        $sql="SELECT * FROM  WHERE email = '".$email."'";
//        if ($result=mysqli_query($conn,$sql))
//        {
//            while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
//                $response['id'] = $row['cid'];
//                $response['balance'] = $row['balance'];
//            }
//        }
    }
    echo json_encode($response);
    mysqli_close($conn);
?>