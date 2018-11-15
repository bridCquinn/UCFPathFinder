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
    	$class  = $inData["class"];
	
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
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
           	   // $stmt->bind_param('iiissssss', $userID, $class["classID"],
			   //   $class["building"],$class["className"],$class["startTime"],
			   //   $class["endTime"], $class["classCode"],
			   //   $class["notes"], $class["classDays"]);
                $stmt->bind_param('iiissssss', $userID, $temp1,$temp2,
                            $temp3,$temp4,$temp5,$temp6,$temp7,$temp8);

                $userID = 25;
                $temp1 = 19;
                $temp2 = 1;
                $temp3 = "change";
                $temp4 = "01:00:00";
                $temp5 = "02:00:00";
                $temp6 = "change123";
                $temp7 = "does this work?";
                $temp8 = "MWF";

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
