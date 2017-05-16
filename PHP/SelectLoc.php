<?php
    $conn = mysqli_connect("mysql.hostinger.in", "u403310507_app", "eat@home123", "u403310507_app");
    $response = array();
    $response["success"] = false;
    $location_array = [];
    if(mysqli_connect_errno()){
        //Connection Error
        echo "Failed to Connect to MySQL: " .mysqli_connect_error();
        echo json_encode($response);  
    }
    else{
        $sql = "SELECT * FROM area ORDER by aid";
        if($result=mysqli_query($conn,$sql)){
            $response["success"] = true;
            while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
                $row_array['aid'] = $row['aid'];
                $row_array['ctname'] = $row['ctname'];
                $row_array['stname'] = $row['stname'];
                $row_array['pincode'] = $row['pincode'];
                $row_array['aname'] = $row['aname'];
                array_push($location_array, $row_array);
            }
            $response["area"] = $location_array;
            mysqli_free_result($result);
        }
        echo json_encode($response);
    }
    mysqli_close($conn);
?>







