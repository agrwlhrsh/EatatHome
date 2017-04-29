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
    $trans_array = [];
    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }
    else{
        if($type === "CUST"){
            $sql="SELECT * FROM transaction WHERE id = 'c".$id."'";
        }else{
            
            $sql="SELECT * FROM transaction WHERE id = 's".$id."'";
        }
        
        if($result=mysqli_query($conn,$sql)){
            $response["success"] = true;
            while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
                $row_array['tid'] = $row['tid'];
                $row_array['oid'] = $row['oid'];
                $row_array['date'] = $row['time'];
                $row_array['tstatus'] = $row['transtatus'];
                $row_array['ttype'] = $row['transtype'];
                $row_array['amount'] = $row['amount'];
                array_push($trans_array, $row_array);
            }
            $response["trans"] = $trans_array;
            mysqli_free_result($result);
        }
            
        echo json_encode($response);
    }
    mysqli_close($conn);
?>