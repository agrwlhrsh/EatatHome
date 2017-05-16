<?php
    
    $mid = $_POST["mid"];
    $sid = $_POST["sid"];
    $mname = $_POST["mname"];
    $items = $_POST["items"];
    $cost = $_POST["cost"];
    $time = $_POST["time"];
    $date = $_POST["date"];
    $veg = $_POST["veg"];
    $quan = $_POST["quan"];
    $aid = $_POST["aid"];
    $sold = "0";

//    $mid = "22";
//    $sid = "13";
//    $mname = "Chineese";
//    $items = "No Chow, No Baby Corn";
//    $cost = "130";
//    $time = "1";
//    $date = "16-5-2018";
//    $veg = "0";
//    $quan = "1000";
//    $aid = "2";

    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    if(mysqli_connect_errno()){
        echo json_encode($response);  
    }
    else{
        $sql="UPDATE menu SET mname = '".$mname."', items = '".$items."', cost= '".$cost."', time= '".$time."', date= '".$date."', veg= '".$veg."', quan= '".$quan."' WHERE mid = '".$mid."', sold = '".$sold."'";
        if ($result=mysqli_query($conn,$sql))
        {
            $response["success"] = true;
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>