<?php
/*
    JSON package expected
    { 
      "username"   :  <<username>>
      "password"   :  <<password>>
    }
    JSON package returned
    {
      "userID"     :  <<Array of class arrays>> 
      "firstName"  :  <<User's first name>>
      "lastName"   :  <<User's last name>>
      "schedule"   :  <<Array of class Arrays>>
      "error"      :  <<error if one exists>>
    }
    Each class array : [classID, buildingID, className, startTime, endTime, 
                        classCode, term, year, notes, classDays]


Login:
SELECT userID, firstName, lastName, login FROM users WHERE login = '<login>' AND password = '<hashed password>';
*/
	$inData = getRequestInfo();
	
	$userID = 0;
	$firstName = "";
	$lastName = "";
	
	/*Connection to the database*/
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	
		$sql = "CALL login (?, ?)";
		
		if($stmt = $conn->prepare($sql))
		{
			/*creates the prepared statement*/
			$stmt->bind_param('ss', $inData["username"], $inData["password"]);/*Binds params to markers*/
			$stmt->execute();
			$result	= $stmt->get_result();
			//$result = $conn->query($sql);
			if ($result->num_rows > 0)
			{
				$row = $result->fetch_assoc();
				$firstName = $row["firstName"];
				$lastName = $row["lastName"];
				$userID = $row["userID"];
				
				/*
				$sql2 = "UPDATE users SET dateLastLoggedIn = NOW() WHERE userID = $userID";
				$conn->query($sql2);
				*/
			
				// Determine current year, month, and day
				$year = date("Y");
				$currentMonth = date("n");
				$currentDay = date("j");
				
				// Determine Term based on month and day for months that have 2 terms
				if($currentMonth >= 1 && $currentMonth <= 5)
				{
					$term = "Spring";
					if( $currentMonth == 5 && $currentDay < 8)
					{
						$term = "Spring";		
					}
					else if ($currentMonth == 5)
					{
						$term = "Summer";
					}
				}
				else if($currentMonth >= 5 && $currentMonth <= 8)
				{
					$term = "Summer";
					if( $currentMonth == 8 && $currentDay < 18) 
					{
						$term = "Summer";
					}
					else if ($currentMonth == 8)
					{
						$term = "Fall";
					}
				}
				else if($currentMonth >= 8 && $currentMonth <= 12)
				{
					$term = "Fall";
					if( $currentMonth == 12 && $currentDay < 18)
					{
						$term = "Fall";
					}
					else if ($currentMonth == 12)
					{
						$term = "Spring";
						$year = $year + 1;
					}
				}
				
				$call = "php TestGetSchedule.php ".$userID." ".$term." ".$year;
				$schedule = shell_exec($call);
			  
				returnWithInfo($firstName, $lastName, $userID, $schedule);
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
		$retValue = '{"userID":0,"firstName":"","lastName":"","error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
	
	function returnWithInfo( $firstName, $lastName, $userID, $schedule)
	{
		$retValue = '{"userID":' . $userID . ',"firstName":"' . $firstName . '","lastName":"' . $lastName . '","schedule":'.$schedule.',"error":""}';
		sendResultInfoAsJson( $retValue );
	}
?>
