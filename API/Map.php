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
	$plusCode = $inData["plusCode"];
	    if (!empty($inData))
		{
		    $base = "https://www.google.com/maps/dir/?api=1&destination=";
		    $encodedURL = "";
		/*' ' to '+'
		',' to "%2C"*/
		   

		    $mode = "&travelmode=walking";
			
		    returnWithInfo($base.$encodedURL.$mode);
		 }
	else{
		returnWithError("Empty Field");	
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
		$retValue = '{"results":"' . $searchResults . '","error":""}';
		sendResultInfoAsJson( $retValue );
	}
?>
