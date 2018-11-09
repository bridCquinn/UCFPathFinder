<?php
/*
    JSON package expected
    { 
      "userID"   :  <<userID>>
      "schedule" :  <<Array of classes>>
    }
    
*/

	$inData = getRequestInfo();
//    	$userID = $inData["userID"];
//    	$array  = $inData["schedule"];

	echo("HERE");
//    	$length = count($array);
	
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

		    /*
                for($i = 0; $i < $length; $i++) { 
            	    $stmt->bind_param('iisssssis', $userID, $array[?],
                        $array[?],$array[?],$array[?],
                        $array[?],$array[?],$array[?],
                        $array[?]);

            	    $stmt->execute();
            	    $result = $stmt->get_result();
                }
		*/
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
