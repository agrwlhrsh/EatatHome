<?php

    $email = $_POST["email"];
    $type = $_POST["type"];

//    $email = "abc@gmail.com";
//    $type = "SUPP";

    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["exists"] = true;
    $response["success"] = false;
    if(mysqli_connect_errno()){
        echo json_encode($response);  
    }
    else{
        
        $sql1="SELECT * FROM customer WHERE email = '".$email."'";
        $sql2="SELECT * FROM supplier WHERE email = '".$email."'";
        
        if (($result1=mysqli_query($conn,$sql1)) && ($result2=mysqli_query($conn,$sql2)))
        {
            $response["success"] = true;
            if((mysqli_num_rows($result1)>0) || (mysqli_num_rows($result2)>0))
            {
                $response["exists"] = true;
            }else{
                $response["exists"] = false;
            }
            mysqli_free_result($result1);
            mysqli_free_result($result2);
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>