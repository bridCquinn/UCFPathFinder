<?php
/*
    JSON package expected
    { 
      "userID"   :  <<userID>>
      "schedule" :  <<Class Object in JSON Format>>
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

	    $sql = "CALL updateClass (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	    $stmt = 0;
		
       	    if($stmt = $conn->prepare($sql))
            {     
		    for($i = 0; $i < $length; $i++)
		    {
			$class = $array[$i];
			$class["classDays"] = "mwf";
		    	$stmt->bind_param('iiissssss', $userID, $class["classID"],
			      $class["building"],$class["className"],$class["startTime"],
			      $class["endTime"], $class["classCode"],
			      $class["notes"], $class["classDays"]);


           	    	$stmt->execute();
           	    	$result = $stmt->get_result();
		    }
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
