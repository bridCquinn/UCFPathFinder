<?php
/*
    JSON package expected
    { 
      "plusCode"   :  <<building plusCode>> 
    }
    JSON package returned
    {
      "results"  :  <<encodedURL>> 
      "error"    :  <<error if one exists>>
    }
*/
	$inData = getRequestInfo();
	$conn = new mysqli("localhost", "root", "orlando", "ucfpathfinder");
	
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{	

            if (!empty($indata))
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
    function returnWithInfo( $encodedURL )
	{
		$retValue = '{"results":' . $encodedURL . ',"error":""}';
		sendResultInfoAsJson( $retValue );
	}
?>
