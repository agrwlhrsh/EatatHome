<?php

    $sid = $_POST["sid"];
//    $sid = "1";

    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    $order_array = [];
    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }
    else{
        $sql = "SELECT * FROM menu WHERE sid = '".$sid."'";
        if($result=mysqli_query($conn,$sql)){
            $response["success"] = true;
            while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
                $row_array['mid'] = $row['mid'];
                $row_array['time'] = $row['time'];
                $row_array['date'] = $row['date'];
                $row_array['quan'] = $row['quan'];
                $row_array['items'] = $row['items'];
                $row_array['mname'] = $row['mname'];
                $row_array['cost'] = $row['cost'];
                $row_array['veg'] = $row['veg'];
                array_push($order_array, $row_array);
            }
            $response["menu"] = $order_array;
            mysqli_free_result($result);
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>







