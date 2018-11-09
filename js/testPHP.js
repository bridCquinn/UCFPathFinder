function dophpTest{

var jsonPayload = '{" "}';
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
