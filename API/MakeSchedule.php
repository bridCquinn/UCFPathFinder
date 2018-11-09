<?php
/*
    JSON package expected
    { 
      "userID"   :  <<userID>>
      "schedule" :  <<Array of classes>>
    }
    
    Array Format :  Every 8 indices is a class 
    Indices      :  buildingID, className, startTime, endTime, classCode,
                    term, year, notes
*/

	$inData = getRequestInfo();
	$temp = 15;
	$temp1 = 1;
	$temp2 = "test";
	$temp3 = "12:00:00";
	$temp4 = "13:15:00";
	$temp5 = "test1234";
	$temp6 = "fall";
	$temp7 = 2018;
	$temp8 = "test test test";
//    	$userID = $inData["userID"];
//    	$array  = $inData["schedule"];

//	$userID = 15;
//    	$array = [0,"test","","","test","test",0,"test"];    
//      $array = [0,"test","","","test","test",0,"test",
//              1,"test1","","","test1","test1",1,"test1"];

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
		    
		   $stmt->bind_param('iisssssis', $temp,$temp1,$temp2,$temp3,$temp4,$temp5,$temp6,$temp7,$temp8 );

            	    $stmt->execute();
            	    $result = $stmt->get_result();
		    /*
                for($i = 0; $i < ($length % 8); $i++) { 
            	    $stmt->bind_param('iisssssis', $userID, $array[$i*8],
                        $array[$i*8+1],$array[$i*8+2],$array[$i*8+3],
                        $array[$i*8+4],$array[$i*8+5],$array[$i*8+6],
                        $array[$i*8+7]);

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
