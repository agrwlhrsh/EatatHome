<?php

    $email = $_POST["email"];
    $type = $_POST["type"];
    $name = $_POST["name"];
    $phone = $_POST["phone"];
    $pass = $_POST["pass"];
//    
//    $email ="abc@gmail.com";
//    $type = "CUST";
//    $name = "name";
//    $phone = "phone";;
//    $pass = "pass";



//    $email = "email";
//    $type = "SUPP";

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
            if($pass === ""){
                $sql = "UPDATE customer SET phone = '".$phone."', name = '".$name."' WHERE email = '".$email."'";
            }else{
                    $sql="UPDATE customer SET phone = '".$phone."', pass = '".$pass."', name = '".$name."' WHERE email = '".$email."'";
            }
        }else{
            if($pass === ""){
                $sql="UPDATE supplier SET phone = '".$phone."', name = '".$name."' WHERE email = '".$email."'";   
            }else{
                $sql="UPDATE supplier SET phone = '".$phone."', pass = '".$pass."', name = '".$name."' WHERE email = '".$email."'";
            }
        }
        if ($result=mysqli_query($conn,$sql))
        {
            $response["success"] = true;
            
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>