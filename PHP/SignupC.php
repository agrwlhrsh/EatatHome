<?php
    $email = $_POST["email"];
    $pass = $_POST["pass"];
    $phone = $_POST["phone"];
    $name = $_POST["name"];
    $balance = "0";
    $addr = $_POST["address"];
//    $email = "agarwal";
//    $pass = "123";
//    $phone = "2345";
//    $name = "jaja";
  
    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    if(mysqli_connect_errno()){
        echo json_encode($response);  
    }
    else{
        $statement = mysqli_prepare($conn, "INSERT INTO customer (name, email, phone, address, pass, balance) VALUES (?, ?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($statement, 'ssssss', $name, $email, $phone, $addr, $pass, $balance);
        mysqli_stmt_execute($statement);
    
        $response["success"] = true;
        
        $sql="SELECT * FROM customer WHERE email = '".$email."'";
        if ($result=mysqli_query($conn,$sql))
        {
            while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
                $response['id'] = $row['cid'];
                $response['balance'] = $row['balance'];
            }
            
            mysqli_free_result($result);
        }
    }
    echo json_encode($response);
    mysqli_close($conn);
?>