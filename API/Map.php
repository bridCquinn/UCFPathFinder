<?php
/*
    JSON package expected
    { 
      "buildingID"   :  <<buildingID>>
    }
    JSON package returned
    {
      "results"  :  <<building plusCode>> 
      "error"    :  <<error if one exists>>
    }
    Each building array : [buildingID, buildingAbbreviation, buildingName]
*/
	$inData = getRequestInfo();
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	
		$sql = "CALL getBuildingCode (?);";
		$stmt = $conn->prepare($sql);
		if($stmt != false) 
		{
			$stmt->bind_param('i', $buildingID);
			$buildingID = $inData["buildingID"];			
            		$stmt->execute();
            		$result = $stmt->get_result();
            if ($result->num_rows > 0)
            {
                $row = $result->fetch_assoc();
	            returnWithInfo( $row["plusCode"] );
			       }
			else
			{
				returnWithError( "No Records Found" );
        	}
		}
		else
		{
			returnWithError($conn->error);
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
    function returnWithInfo( $searchResults )
	{
		$retValue = '{"results":' . $searchResults . ',"error":""}';
		sendResultInfoAsJson( $retValue );
	}
?>
