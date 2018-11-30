<?php
/*
    JSON package expected
    { 
      "userID"   :  <<userID>>
      "schedule" :  <<Array of Class Objects in JSON Format>>
    }
    
    JSON package returned if error
    {
     "error"     :  <<error message if one exisits>>
    }
*/
	$inData = getRequestInfo();
    	$userID = $inData["userID"];
    	$class  = $inData["schedule"];
	
	$info = json_decode(file_get_contents('info.json'), true);
	
	$conn = new mysqli("localhost", $info["name"], $info["pass"], $info["data"]);	
	
	if ($conn->connect_error) 
	{	
		returnWithError( $conn->connect_error );
	} 
	else
	{ 
	    $sql = "CALL addClass (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	    $stmt = 0;
		
       	    if($stmt = $conn->prepare($sql))
            {
 
            	    $stmt->bind_param('iisssssiss', $userID, $class["building"],
				      $class["className"],$class["startTime"],$class["endTime"],
				      $class["classCode"],$class["term"],$class["year"], $class["notes"],
				      $class["classDays"]);
		    
		    $class["building"] = 0;
		    $class["className"] = "test";
		    $class["startTime"] = "00:00:00";
		    $class["endTime"] = "00:00:00";
		    $class["classCode"] = "slkj";
		    $class["term"] = "Fall";
		    $class["year"] = 2018;
		    $class["notes"] = "fs";
		    $class["classDays"] = "MWF";
		    
        	    $stmt->execute();
            	    $result = $stmt->get_result();	 
	    }
	    else
	    {
		returnWithError( $conn->error );
            }
	
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
		$retValue = '{"classID":"' . $classID . '","error":""}';
		sendResultInfoAsJson( $retValue );
	}
 ?>
