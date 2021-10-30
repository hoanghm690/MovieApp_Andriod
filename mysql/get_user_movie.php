<?php
	require 'config.php';
	
	$userId = (int)$_POST['userID'];
	
	$query = "SELECT * FROM history_movie WHERE user_id = '$userId' ORDER BY count DESC LIMIT 3";
	$result = mysqli_query($conn, $query);
    	$number_of_rows = mysqli_num_rows($result);

    	$response = array();

    	if($number_of_rows > 0) {
        	while($row = mysqli_fetch_assoc($result)) {
            		$response[] = $row;
        	}
    	}

    	header('Content-Type: application/json');
    	echo json_encode(array("historyMovies"=>$response));
    	mysqli_close($conn); 
?>