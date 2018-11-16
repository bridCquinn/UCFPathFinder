<?php
/*
Login:
SELECT userID, firstName, lastName, login FROM users WHERE login = '<login>' AND password = '<hashed password>';
*/
	$inData = getRequestInfo();
	
	$userID = 0;
	$firstName = "";
	$lastName = "";
	
	/*Connection to the database*/
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{
		
		$sql = "CALL login (?, ?)";
		
		if($stmt = $conn->prepare($sql))
		{
			/*creates the prepared statement*/
			$stmt->bind_param('ss', $inData["username"], $inData["password"]);/*Binds params to markers*/
			$stmt->execute();
			$result	= $stmt->get_result();
			//$result = $conn->query($sql);
			if ($result->num_rows > 0)
			{
				$row = $result->fetch_assoc();
				$firstName = $row["firstName"];
				$lastName = $row["lastName"];
				$userID = $row["userID"];
				
				/*
				$sql2 = "UPDATE users SET dateLastLoggedIn = NOW() WHERE userID = $userID";
				$conn->query($sql2);
				*/
				// TESTING
				$call = "php TestGetSchedule.php ".$userID." fall 2018";
				$callResult = shell_exec($call);
				$schedule = json_decode($callResult);
			  
				returnWithInfo($firstName, $lastName, $userID, $schedule);
			}
			
			else
			{
				returnWithError( "No Records Found" );
			}
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
		$retValue = '{"userID":0,"firstName":"","lastName":"","error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
	
	function returnWithInfo( $firstName, $lastName, $userID, $schedule)
	{
		$retValue = '{"userID":' . $userID . ',"firstName":"' . $firstName . '","lastName":"' . $lastName . '","schedule":"'.$schedule.'","error":""}';
		sendResultInfoAsJson( $retValue );
	}
	
?>
