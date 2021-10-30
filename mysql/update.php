<?php
	require 'config.php';
	
	$fullname = $_POST['fullname'];
	$email = $_POST['myemail'];
	
	
	$sql = "SELECT * FROM user WHERE email = '$email'";
	$check = mysqli_query($conn,$sql);
	
	
	if(mysqli_num_rows($check) > 0){
		$result = "UPDATE user SET fullname = '$fullname' WHERE email = '$email'";
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