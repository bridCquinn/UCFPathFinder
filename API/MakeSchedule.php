<?php
	$inData = getRequestInfo();
	
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	if ($conn->connect_error) 
	{	
		returnWithError( $conn->connect_error );
	} 
	else
	{  
	    $sql = "CALL addClass (?, ?, ?, ?, ?, ?, ?, ?,  ?);";
	    $stmt = 0;
		
        if($stmt = $conn->prepare($sql))
        {
            	$stmt->bind_param('iisssssis', $inData["userID"],
                   $inData["buildingID"],$inData["className"],
                   $inData["startTime"],
                   $inData["endTime"],$inData["classCode"],
                   $inData["term"],
                   $inData["year"],$inData["notes"]);

            	$stmt->execute();
            	$result = $stmt->get_result();
		}
		else
		{
			returnWithError( $conn->error );
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
