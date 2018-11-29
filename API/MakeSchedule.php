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
    	$array  = $inData["schedule"];
    	$length = count($array);
	
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
        $length = 1;
		    for($i = 0; $i < $length; $i++) 
		{ 
		    $class = $array[$i];
		    if($class["classID"] != -1) 
		    {
			continue;    
		    }
            	    $stmt->bind_param('iisssssiss', $userID, $class["building"],
				      $class["className"],$class["startTime"],$class["endTime"],
				      $class["classCode"],$class["term"],$class["year"], $class["notes"],
				      $class["classDays"]);

		    $userID = 25;
            	    $stmt->execute();
            	    $result = $stmt->get_result();
		    $classID = $conn->lastInsertId();
                }
	    }
	    else
	    {
		returnWithError( $conn->error );
            }
	echo $classID;
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
