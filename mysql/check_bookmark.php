<?php
	require 'config.php';
	
	$userId = $_POST['userID'];
	$titleMovie = $_POST['titleMovie'];
    
	
	$sql = "SELECT * FROM bookmark WHERE user_id = '$userId' and title_movie = '$titleMovie'";

    $result = array();
	$check = mysqli_query($conn,$sql);
	
	if(mysqli_num_rows($check)){
		$result['status'] = 'true';
		echo json_encode($result);
		}else{
            $result['status'] = 'false';
			echo json_encode($result);
	}
?>