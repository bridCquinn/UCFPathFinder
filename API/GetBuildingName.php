<?php
/*
    JSON package expected
    { 
      "search"   :  <<Full or Partial Building String>>
    }

    JSON package returned
    {
      "results"  :  <<Array of building arrays>> 
      "error"    :  <<error if one exists>>
    }
    Each building array : [buildingID, buildingAbbreviation, buildingName]
*/
	$inData = getRequestInfo();

	$info = json_decode(file_get_contents('info.json'), true);
	
	$conn = new mysqli("localhost", $info["name"], $info["pass"], $info["data"]);	
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	
		$sql = "CALL getBuildingName(?);";
		$stmt = $conn->prepare($sql);
		if($stmt != false) 
		{
			$stmt->bind_param('i', $argv[1]);

			$stmt->execute();
			
            		$result = $stmt->get_result();
			
			$searchCount = 0;
			$searchResults = "";
            		if ($result->num_rows > 0)
			{
				while($row = $result->fetch_assoc())
				{					
					$buildingName = $row["buildingName"];
					echo( $buildingName );
				}
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
		$retValue = '{"results": ' . $searchResults . ' ,"error":""}';
		sendResultInfoAsJson( $retValue );
	}
?>
