<?php
/*
    JSON package expected
    { 
      "userID"   :  <<userID>>
      "term"     :  <<term>>
      "year"     :  <<year>>
    }

    JSON package returned
    {
      "schedule"  :  <<Array of class arrays>> 
      "error"     :  <<error if one exists>>
    }
    Each class array : [classID, buildingID, className, startTime, endTime, 
                        classCode, term, year, notes, classDays, buildingName]
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
		$sql = "CALL getClasses (?,?,?);";
		$stmt = $conn->prepare($sql);
		if($stmt != false) 
		{
			$stmt->bind_param('isi', $userID, $term, $year);
			if($argc > 1) {
				$userID = $argv[1];
				$term = $argv[2];
				$year = $argv[3];
			}
			else {
				$userID = $inData["userID"];
            			$term = $inData["term"];
				$year = $inData["year"];
			}
			
			$stmt->execute();  
			$result = $stmt->get_result();
			$searchResults = array();
        	
			if ($result->num_rows > 0)
			{
				while($row = $result->fetch_assoc())
				{					
					$classID = $row["classID"];
					$buildingID = $row["buildingID"];
					$className = $row["className"];
              	  			$startTime = $row["startTime"];
		                	$endTime = $row["endTime"];
                			$classCode = $row["classCode"];
                			$term = $row["term"];
                			$year = $row["year"];				
                			$notes = $row["notes"];
                			$classDays = $row["days"];
					
					$call = "php GetBuildingName.php ".$buildingID;
				
					$buildingName = shell_exec($call);				 
					
					$class = array($classID, $buildingID, $className, $startTime, $endTime, $classCode, $term, $year, $notes, $classDays, $buildingName);
					
					array_push($searchResults, $class);
            
				}
				returnWithInfo( $searchResults, $argc );
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

   	function returnWithInfo( $searchResults, $argc )
	{
		$retValue = '{"schedule":' . json_encode($searchResults) . ',"error":""}';
	    	if ($argc > 1 )
		{
			echo json_encode($searchResults);
			return;
		}
		sendResultInfoAsJson( $retValue );
	}
?>
