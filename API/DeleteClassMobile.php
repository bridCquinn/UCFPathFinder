<?php
/*
   		JSON package expected
    		{ 
      			"userID"  :  <<userID>>
			      "classID" :  <<classID>>
    		}
		
        	Deletes class associated with that userID classCode term and year
 */
	$inData = getRequestInfo();
	$info = json_decode(file_get_contents('info.json'), true);
	
	$conn = new mysqli("localhost", $info["name"], $info["pass"], $info["data"]);	
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	
		$sql = "CALL deleteClassByID (?,?);";
		$stmt = $conn->prepare($sql);
		if($stmt != false) 
		{
			$stmt->bind_param('issi', $userID, $classID);
			$userID = $inData["userID"];
      $classID = $inData["classID"];
      $userID = 25;
      $classID = 465;
                
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
