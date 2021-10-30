<?php
	require 'config.php';
	$fullname = $_POST['fullname'];
	$email = $_POST['email'];
	$password = $_POST['password'];
	
	$sql = "SELECT * FROM user WHERE email = '$email'";
	$check = mysqli_query($conn,$sql);
	if(mysqli_num_rows($check)){
		echo "Tài khoản $email đã tồn tại";
	}else{
		$insertdata = "INSERT INTO user(fullname,email,password)VALUES('$fullname','$email','$password')";
		if(mysqli_query($conn,$insertdata)){
			echo "Đăng ký thành công";
		}else{
			echo "Đăng ký thất bại";
		}
	}
?>