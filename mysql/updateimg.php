<?php
	require 'config.php';
	
	$image = $_POST['image'];
	$email = $_POST['myemail'];
	$sql = "SELECT * FROM user WHERE email = '$email'";
	$check = mysqli_query($conn,$sql);
	
	
	if(mysqli_num_rows($check) > 0){
		$result = "UPDATE user SET image_url = '$image' WHERE email = '$email'";
		if(mysqli_query($conn,$result)){
			echo "Cập nhật thành công";
		}else
		{
			echo "Đã xảy ra lỗi !!";
		}
	}else{
			echo "Không xác nhận được người dùng";
	}
?>