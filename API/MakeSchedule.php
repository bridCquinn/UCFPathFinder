<?php
/*
    JSON package expected
    { 
      "userID"   :  <<userID>>
      "schedule" :  <<Array of Class Objects in JSON Format>> 
    }
    
    JSON package returned if error
    {
     "classID"   :  <<classID of the last class added>>
     "error"     :  <<error message if one exisits>>
    }
*/
	$inData = getRequestInfo();
    	$userID = $inData["userID"];
    	$array  = $inData["schedule"];
	$class = $array[0];
	$classID = -1;
	
	$info = json_decode(file_get_contents('info.json'), true);
	
	$conn = new mysqli("localhost", $info["name"], $info["pass"], $info["data"]);	
	
	if ($conn->connect_error) 
	{	
		returnWithError( $conn->connect_error );
	} 
	else
	{ 
	    // Add class to the database
	    $sql = "CALL addClass (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	    $stmt = 0;
		
       	    if($stmt = $conn->prepare($sql))
            {
 
            	    $stmt->bind_param('iisssssiss', $userID, $class["building"],
				      $class["className"],$class["startTime"],$class["endTime"],
				      $class["classCode"],$class["term"],$class["year"], $class["notes"],
				      $class["classDays"]);
	
        	    $stmt->execute();
            	    $result = $stmt->get_result();	 
	    }
	    else
	    {
		returnWithError( $conn->error );
            }
	
	    // Get the classID of the last class added to the database
	    $sql2 = "CALL getMostRecentClassID (?)";
		
	    if($stmt = $conn->prepare($sql2))
            {
		$stmt->bind_param('i', $userID);
		$stmt->execute();
		$result = $stmt->get_result();
		$row = $result->fetch_assoc();
	        $classID = $row["classID"];
	    }
		
	    returnWithInfo($classID);
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

	function returnWithInfo( $classID )
	{
		$retValue = '{"classID":' . $classID . ',"error":""}';
		sendResultInfoAsJson( $retValue );
	}
 ?>
