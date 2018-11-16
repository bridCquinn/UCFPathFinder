function toRegPage()
{
  // hides loginPage and shows register
  hideOrShow( "registerPage", true);
  hideOrShow( "loginPage", false);

}

function doLogin()
{
	userId = 0;
	firstName = "";
	lastName = "";
	lastIdClicked = 0;

	var login = document.getElementById("loginName").value;
	var password = md5(document.getElementById("loginPassword").value);

	// sanitizing the login and Password
	var i;
	for(i = 0; i < login.length; i++)
	{
		if(login[i] === ';' || login[i] === '/' || login[i] === '-' || login[i] == ')' || login[i]=='(')
		{
			alert("There are illegal characters in your login");
			document.getElementById("loginName").value = loginName.defaultValue;
			document.getElementById("loginPassword").value =loginPassword.defaultValue;
			return;
		}

		if(password[i] === ';' || password[i] === '/' || password[i] === '-'|| password[i] == ')' || password[i]=='(')
		{
			alert("There are illegal characters in your password.");
			document.getElementById("loginName").value = loginName.defaultValue;
			document.getElementById("loginPassword").value =loginPassword.defaultValue;
			return;
		}
	}

	document.getElementById("loginResult").innerHTML = "";

	var jsonPayload = '{"username" : "' + login + '", "password" : "' + password + '"}';
	var url = urlBase + '/Login.' + extension;

	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, false);
	xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
	try
	{
		xhr.send(jsonPayload);

		var jsonObject = JSON.parse( xhr.responseText );

		userId = jsonObject.userID;

		if( userId < 1 || userId == 'undefined' || userId == null)
		{
			document.getElementById("loginResult").innerHTML = "User/Password combination incorrect";
			return;
		}

		firstName = jsonObject.firstName;
		lastName = jsonObject.lastName;

		//document.getElementById("userName").innerHTML = firstName + " " + lastName;

    for(i = 0; i < jsonObject.length; i++)
    {
      scheduleList[i].classID = jsonObject[i][0];
      scheduleList[i].building = jsonObject[i][1];
      scheduleList[i].className = jsonObject[i][2];
      scheduleList[i].startTime = jsonObject[i][3];
      scheduleList[i].endTime = jsonObject[i][4];
      scheduleList[i].classCode = jsonObject[i][5];
      scheduleList[i].term = jsonObject[i][6];
      scheduleList[i].year = jsonObject[i][7];
      scheduleList[i].notes = jsonObject[i][8];
      scheduleList[i].classDays = jsonObject[i][9];
    }

    for(j = 0; j < scheduleList.length; j++)
      makeTile(scheduleList[j]);
    
		document.getElementById("loginName").value = loginName.defaultValue;
		document.getElementById("loginPassword").value =loginPassword.defaultValue;

    hideOrShow( "tabs", true);
		hideOrShow( "accessUIDiv", true);
		hideOrShow( "loginPage", false);
    hideOrShow("Map", false);
    hideOrShow("Profile", false);
    hideOrShow("scaleable-wrapper", false);


	}
	catch(err)
	{
		document.getElementById("loginResult").innerHTML = err.message;
	}

}
