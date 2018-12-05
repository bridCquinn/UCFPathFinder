<?php
/*
    JSON package expected
    { 
      "buildingID"   :  <<buildingID>>
    }

    JSON package returned
    {
      "results"  :  <<buildingName>> 
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
			$stmt->bind_param('i', $search);

			$search = $inData["buildingID"];
			
			if($argc > 1) 
			{
				$search = $argv[1];
			}
			
			$stmt->execute();
			
            		$result = $stmt->get_result();
			
			$searchCount = 0;
			$searchResults = "";
            		if ($result->num_rows > 0)
			{
				while($row = $result->fetch_assoc())
				{					
					$buildingName = $row["buildingName"];
					if ($argc > 1) 
					{
						echo( $buildingName );
					}
					else
					{
						returnWithInfo($buildingName);
					}
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
		$retValue = '{"results": "' . $searchResults . '" ,"error":""}';
		sendResultInfoAsJson( $retValue );
	}
?>
