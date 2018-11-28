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
    for(i = 0; i < jsonObject.schedule.length; i++)
    {
      var course = new Course();

      course.classID = jsonObject.schedule[i][0];
      course.building = jsonObject.schedule[i][1];
      course.className = jsonObject.schedule[i][2];
      course.startTime = jsonObject.schedule[i][3];
      course.endTime = jsonObject.schedule[i][4];
      course.classCode = jsonObject.schedule[i][5];
      course.term = jsonObject.schedule[i][6];
      course.year = jsonObject.schedule[i][7];
      course.notes = jsonObject.schedule[i][8];
      course.classDays = jsonObject.schedule[i][9];

      scheduleList.push(course);

      addtoList(course);

      if(scheduleList < 1)
        document.getElementById("delSch").style.display = 'none';
      else
        document.getElementById("termYear").innerHTML= course.term + " " + course.year;
    }

    for(j = 0; j < scheduleList.length; j++)
    {
      makeTile(scheduleList[j]);
    }


		document.getElementById("loginName").value = loginName.defaultValue;
		document.getElementById("loginPassword").value =loginPassword.defaultValue;

    hideOrShow( "tabs", true);
		hideOrShow( "accessUIDiv", true);
		hideOrShow( "loginPage", false);
    hideOrShow("Profile", false);
    hideOrShow("scaleable-wrapper", false);


	}
	catch(err)
	{
		document.getElementById("loginResult").innerHTML = err.message;
	}

}
