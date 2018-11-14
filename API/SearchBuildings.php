<?php
/*
    JSON package expected
    { 
      "search"   :  <<Full or Partial Building String>>
    }

    JSON package returned
    {
      "results"  :  <<Array of arrays. Each index being a building>> 
    }
    Each building array : [buildingID, buildingAbbreviation, buildingName, plusCode]
*/

	$inData = getRequestInfo();
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	
		$sql = "CALL searhBuildingsBoth(?,?);";
		$stmt = $conn->prepare($sql);
		if($stmt != false) 
		{
			$stmt->bind_param('ss', $buildingName, $buildingName);
			//$buildingName = $inData["search"];
			$buildingName = "a";
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
                			// Create and initialize variable with contact attribute
					$buildingID = $row["buildingID"];
					$buildingAbb = $row["buildingAbbreviation"];
					$buildingName = $row["buildingName"];
              	  			$plusCode = $row["plusCode"];

					$searchResults .= '["' . $buildingID . '","' . $buildingAbb 
                            . '","' . $buildingName . '","' . $plusCode . '"]';
				}
				returnWithInfo( $searchResults );
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
