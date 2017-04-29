<?php
    $name = $_POST["name"]; 
    $email = $_POST["email"];
    $address = $_POST["address"];
    $phone = $_POST["phone"];   
    $pass = $_POST["pass"];
    $ifsc = $_POST["ifsc"];
    $acno = $_POST["acno"];
    $acname = $_POST["acname"];
    $aid = $_POST["aid"];
    $balance = "0";
//    $name = "name"; 
//    $email = "email";
//    $address = "address";
//    $phone = "phone";   
//    $pass = "pass";
//    $ifsc = "ifsc";
//    $acno = "1234";
//    $acname = "acname";    
    
    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }
    else{
        $statement = mysqli_prepare($conn,"INSERT INTO supplier (name, email, address, phone, pass, ifsc, acno, acname, balance,aid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($statement, 'ssssssssss', $name, $email, $address, $phone, $pass, $ifsc, $acno, $acname, $balance,$aid);
        mysqli_stmt_execute($statement);
        $response["success"] = true;
        $sql="SELECT * FROM supplier WHERE email = '".$email."'";
        if ($result=mysqli_query($conn,$sql))
        {
            while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
                $response['id'] = $row['sid'];
                $response['balance'] = $row['balance'];
            }
        }
    }
    echo json_encode($response);
    mysqli_close($conn);
?>