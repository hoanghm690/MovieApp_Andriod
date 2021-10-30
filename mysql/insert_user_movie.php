<?php
	require 'config.php';
	
	$userId = $_POST['userID'];
	$categoryMovie = $_POST['categoryMovie'];
	
	$sql = "SELECT * FROM history_movie WHERE user_id = '$userId' and category_movie = '$categoryMovie'";
	$check = mysqli_query($conn,$sql);
	
	if(mysqli_num_rows($check)){
		$updateData = "UPDATE history_movie SET count=count+1 WHERE user_id = '$userId' and category_movie = '$categoryMovie'";
		mysqli_query($conn,$updateData);
		return;
	}
	
	$insertdata = "INSERT INTO history_movie(user_id,category_movie,count)VALUES('$userId','$categoryMovie',1)";
	if(mysqli_query($conn,$insertdata)){
			echo "Thành công";
		}else{
			echo "Thất bại";
		}
?>