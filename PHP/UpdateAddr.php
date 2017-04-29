<?php
    $id = $_POST["id"];
    $type = $_POST["type"];
    $address = $_POST["address"];
//
//    $id = "1";
//    $type = "SUPP";
//    $address = "v;v;v;v;v;v";

    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }
     else{
        if($type === "CUST"){
            $sql = "UPDATE customer SET address = '".$address."' WHERE cid = '".$id."'";
        }else{
            $sql="UPDATE supplier SET address = '".$address."' WHERE sid = '".$id."'";
        }
        if ($result=mysqli_query($conn,$sql))
        {
            $response["success"] = true;
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>