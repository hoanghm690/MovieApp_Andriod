<?php
	require 'config.php';
	
	$oldpass = $_POST['oldpassword'];
	$newpass = $_POST['newpassword'];
	$confirmpass = $_POST['confirmpassword'];
	$email = $_POST['email'];
	
	$sql = "SELECT * FROM user WHERE email = '$email' AND password = '$oldpass'";
	$query = mysqli_query($conn,$sql);
	
	if($newpass == $confirmpass){
	if(!mysqli_num_rows($query) > 0 ){
		echo ("Mật khẩu hiện tại không khớp");
	}else{
		$update = "UPDATE user SET password = '$newpass' WHERE email = '$email'";
		$res = mysqli_query($conn,$update);
		if($res){
			echo "Thay đổi mật khẩu thành công";
		}else{
				echo "Đã xảy ra lỗi !!";
		}
	}
	}else{
		echo "Xác nhận mật khẩu mới không khớp";
	}
?>