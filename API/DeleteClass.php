<?php
    /*
        Recieves userID, classCode, term and year 
        Deletes class associated with that userID classCode term and year
    */

	$inData = getRequestInfo();
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	
		$sql = "CALL deleteClass (?,?,?,?);";
		$stmt = $conn->prepare($sql);
		if($stmt != false) 
		{
			$stmt->bind_param('issi', $userID, $classCode, $term, $year);
			$userID = $inData["userId"];
            		$classCode = $inData["classCode"];
            		$term = $inData["term"];
            		$year = $inData["year"];
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
