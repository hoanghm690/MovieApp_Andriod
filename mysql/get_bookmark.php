<?php
	require 'config.php';
	
	$userId = $_POST['userID'];
	
	$query = "SELECT * FROM bookmark WHERE user_id = '$userId' ";
	$result = mysqli_query($conn, $query);
    	$number_of_rows = mysqli_num_rows($result);

    	$response = array();

    	if($number_of_rows > 0) {
        	while($row = mysqli_fetch_assoc($result)) {
            		$response[] = $row;
        	}
    	}

    	header('Content-Type: application/json');
    	echo json_encode(array("myListMovies"=>$response));
	mysqli_close($conn); 
	
?>