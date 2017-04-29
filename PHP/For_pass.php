
<?php
$email = $_POST['email'];
$email = 'agarwal.harshnu@gmail.com';
$to = $email;
$subject = "Forgot Password";
$new_pass = substr(str_shuffle("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"), -6);
$txt = "Your New Password in the registered email id is : ".$new_pass."\r\n You can Now login from the app using this new Password!! ";
$conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");

$response["success"] = false;
$response["exist"] = true;
$response["update"] = false;

if(mysqli_connect_errno()){
    //Connection Error
    echo "Failed to Connect to MySQL: " .mysqli_connect_error();
    echo json_encode($response);  
}
else{
    $response["success"] = true;
    //first check customer database
    $sql="SELECT * FROM customer WHERE email = '".$email."'";
    if ($result=mysqli_query($conn,$sql))
    {
        if(mysqli_num_rows($result)==1)
        {
            $response["exist"] = true;
            $sql = "UPDATE customer SET pass = '".$new_pass."' WHERE email = '".$email."'";
            if($result=mysqli_query($conn,$sql)){
                $response['update'] = true;
                mail($to,$subject,$txt,$headers);                    
            }

        }
        else
        {
            $sql="SELECT * FROM supplier WHERE email = '".$email."' ";
            if ($result2=mysqli_query($conn,$sql))
            {
                if(mysqli_num_rows($result2)==1)
                {
                    $response["exist"] = true;
                    $sql = "UPDATE supplier SET pass = '".$new_pass."' WHERE email = '".$email."'";
                    if($result=mysqli_query($conn,$sql)){
                        $response['update'] = true;
                        mail($to,$subject,$txt,$headers);
                    }
                    
                }
            }                
        }
    }
    echo json_encode($response);
}
mysqli_close($conn);
?>
