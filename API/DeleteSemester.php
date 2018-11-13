<?php
    /*
        Recieves userID, term and year 
        Deletes all classes associated with that userID term and year
    */

	$inData = getRequestInfo();
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	
		$sql = "CALL deleteClassesTerm (?, ?, ?);";
		$stmt = $conn->prepare($sql);
		if($stmt != false) 
		{
			$stmt->bind_param('isi', $userID, $term, $year);
			//$userID = $inData["userId"];
            		//$term = $inData["term"];
            		//$year = $inData["year"];
			$userID = 25;
			$term = 'fall';
			$year = 2019
			$stmt->execute();
		}
		else
		{
			returnWithError($conn->error);
		}
		$conn->close();
	}
	
	function getRequestInfo()
	{
		return json_decode(file_get_contents('php://input'), true);
	}
	function sendResultInfoAsJson( $obj )
	{
		header('Content-type: application/json');
		echo $obj;
	}
	
	function returnWithError( $err )
	{
		$retValue = '{"error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
?>
