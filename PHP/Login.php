<?php
    $ip_email = $_POST["email"];
    $ip_pass = $_POST["pass"];
    
//    $ip_email = "abc@gmail.com";
//    $ip_pass = "123";
    
    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["error"] = true;
    $response["auth"] = false;
    $response["type"] = "FAIL";
    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
        
        
    }
    else{
        $response["error"] = false;
        //first check customer database
        $sql="SELECT * FROM customer WHERE email = '".$ip_email."'";
        if ($result=mysqli_query($conn,$sql))
        {
            if(mysqli_num_rows($result)==1)
            {
                $response["type"] = 'CUST';
                while($row=mysqli_fetch_row($result))
                {
                    if($ip_pass === $row[5])
                    {
                        $response["auth"] = true;
                        $response["id"] = $row[0];
                        $response["name"] = $row[1];
                        $response["email"] = $row[2];
                        $response["phone"] = $row[3];
                        $response["address"] = $row[4];
                        $response["balance"] = $row[6];
                        
                    }
                }
            }
            else
            {
                $sql="SELECT * FROM supplier WHERE email = '".$ip_email."'";
                if ($result2=mysqli_query($conn,$sql))
                {
                    if(mysqli_num_rows($result2)==1)
                    {
                        $response["type"] = 'SUPP';
                        while($row=mysqli_fetch_row($result2)) 
                        {
                            if($ip_pass === $row[5])
                            {
                                $response["auth"] = true;
                                $response["id"] = $row[0];
                                $response["name"] = $row[1];
                                $response["email"] = $row[2];
                                $response["phone"] = $row[4];
                                $response["address"] = $row[3];
                                $response["ifsc"] = $row[6];
                                $response["acno"] = $row[7];
                                $response["acname"] = $row[8];
                                $response["balance"] = $row[9];
                                $response["aid"] = $row[11];
                            }
                        }
                    }
                    mysqli_free_result($result2);
                }                
            }
            mysqli_free_result($result);
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>