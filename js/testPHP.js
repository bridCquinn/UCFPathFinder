function dophpTest()
{


var jsonPayload = '{"userID" : 1, "schedule" : [1,2]}';
	var url = urlBase + '/MakeSchedule.' + extension;

	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, false);
	xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
	try
	{
		xhr.send(jsonPayload);

		var jsonObject = JSON.parse( xhr.responseText );

	}
	catch(err)
	{
		document.getElementById("loginResult").innerHTML = err.message;
	}

    }
