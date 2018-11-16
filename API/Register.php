<?php
/*
   	 JSON package expected
    	{ 
		"firstName"  :  <<firstName>>
		"lastName"   :  <<lastName>>
		"username"   :  <<username>>
		"password"   :  <<password>>
		"email"      :  <<email>>
    	}   
*/
	$inData = getRequestInfo();
	
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{
		$sql = "CALL addUser (?, ?, ?, ?, ?)";
		if($stmt = $conn->prepare($sql))
		{
			/*creates the prepared statement*/
			$stmt->bind_param('sssss', $inData["firstName"], $inData["lastName"], $inData["username"], $inData["password"], $inData["email"]);
			/*Binds params to markers*/
			
			$stmt->execute();
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
