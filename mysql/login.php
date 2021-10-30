<?php
	require 'config.php';
	
	$email = $_POST['email'];
	$password = $_POST['password'];
	
	$sql = "SELECT * FROM user WHERE email = '$email' AND password = '$password'";
	$result = array();
	$result["data"] = array();
	$responce = mysqli_query($conn,$sql);
	
	if(mysqli_num_rows($responce) === 1){
		$row = mysqli_fetch_assoc($responce);
		$ds['fullname'] = $row['fullname'];
		$ds['email'] = $row['email'];
		$ds['userID'] = $row['user_id'];
		$ds['image'] = $row['image_url'];
		
		array_push($result['data'],$ds);
		$result['status'] = 'success';
		echo json_encode($result);
		mysqli_close($conn);
		
		}else{
			$result['status'] = 'error';
			echo json_encode($result);
			mysqli_close($conn);
		}
?>