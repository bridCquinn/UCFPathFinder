<?php
/*
    JSON package expected
    { 
      "userID"   :  <<userID>>
      "schedule" :  <<Array of Class Objects in JSON Format>>
    }
    
*/
	$inData = getRequestInfo();
    	$userID = $inData["userID"];
    	$array  = $inData["schedule"];
    	$length = count($array);
	
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	if ($conn->connect_error) 
	{	
		returnWithError( $conn->connect_error );
	} 
	else
	{  
        // CALL addClass (<userID>, <buildingID>, '<className>',
        // <startTime>, <endTime>, '<classCode>', '<term>', <year>,
        //  '<notes>');
	    $sql = "CALL addClass (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	    $stmt = 0;
		
       	    if($stmt = $conn->prepare($sql))
            {
                for($i = 0; $i < $length; $i++) 
		{ 
		   //$class = $array[$i];
		    $temp = 1;
		    $class = json_decode($array[$i], true);
            	    $stmt->bind_param('iisssssis', $userID, $temp,
				      $class["className"],$class["startTime"],$class["endTime"],
				      $class["classCode"],$class["term"],$class["year"],
				      $class["notes"]);

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
