<?php
	require 'config.php';
	
	$userId = (int)$_POST['userID'];
	$titleMovie = $_POST['titleMovie'];
	
	$sql = "SELECT * FROM bookmark WHERE user_id = '$userId' and title_movie = '$titleMovie'";
	$query1 ="SELECT * FROM user WHERE user_id = '$userId' ";
	$check = mysqli_query($conn,$sql);
	$result = array();
	if(mysqli_query($conn,$query1)){
		if(mysqli_num_rows($check)){
			$updateData = "DELETE FROM bookmark WHERE user_id = '$userId' and title_movie = '$titleMovie'";
			mysqli_query($conn,$updateData);
			$result['status'] = 'delete';
			echo json_encode($result);
		}else{
			$insertdata = "INSERT INTO bookmark(user_id,title_movie)VALUES('$userId','$titleMovie')";
			if(mysqli_query($conn,$insertdata)){
				$result['status'] = 'insert';
				echo json_encode($result);
				}
		}
	}
	
?>