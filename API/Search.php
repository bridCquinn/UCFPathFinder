<?php
	$inData = getRequestInfo();
	
	$searchResults = "";
	$searchCount = 0;
	
	$test = "%".$inData["search"]."%";
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	if ($conn->connect_error) 
	{	
		returnWithError( $conn->connect_error );
	} 
	else
	{  
	$sql = ;
	$stmt = 0;
		if($stmt = $conn->prepare($sql))
    		{
            		$stmt->bind_param('isss', $inData["userID"],$test, $test, $test);
            		$stmt->execute();
            		$result = $stmt->get_result();
		
			if ($result->num_rows > 0)
			{
				while($row = $result->fetch_assoc())
				{
					if( $searchCount > 0 )
					{
						$searchResults .= ",";
					}
				
					$searchCount++;
                    // Create list of results                    
    					
				}
				returnWithInfo( $searchResults );
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
		$retValue = '{"id":0,"firstName":"","lastName":"","error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
	
	function returnWithInfo( $searchResults )
	{
		$retValue = '{"results":[' . $searchResults . '],"error":""}';
		sendResultInfoAsJson( $retValue );
	}
	
?>
