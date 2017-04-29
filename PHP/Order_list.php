<?php

    $id = $_POST["id"];
    $type = $_POST["type"];
//    $id = "1";
//    $type = "CUST";
//    $email = "email";
//    $type = "SUPP";

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
        $sql = "SELECT * FROM orders WHERE cid = 'c".$id."'";
        if($result=mysqli_query($conn,$sql)){
            $response["success"] = true;
            while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
                $row_array['oid'] = $row['oid'];
                $row_array['mid'] = $row['mid'];
                $row_array['time'] = $row['time'];
                $row_array['date'] = $row['date'];
                $row_array['quantity'] = $row['quantity'];
                $row_array['amount'] = $row['amount'];
                $row_array['disc'] = $row['disc'];
                $row_array['total'] = $row['total'];
                $row_array['deliv'] = $row['deliv'];
                $row_array['payby'] = $row['payby'];
                $row_array['status'] = $row['status'];
                $row_array['address'] = $row['address'];
                $row_array['tid'] = $row['tid'];
                $sql2="SELECT * FROM menu WHERE mid = '".$row['mid']."'";
                if($result2 = mysqli_query($conn, $sql2)){
                    $row2 = mysqli_fetch_array($result2, MYSQLI_ASSOC);
                    $row_array['mname'] = $row2['mname'];
                    $row_array['items'] = $row2['items'];
                }
                mysqli_free_result($result2);
                array_push($order_array, $row_array);
            }
            $response["order"] = $order_array;
            mysqli_free_result($result);
        }
            
        echo json_encode($response);
    }
    mysqli_close($conn);
?>







