function toLogPage()
{
  // hides regPage and shows login
  hideOrShow( "loginPage", true);
  hideOrShow( "registerPage", false);
}

function register()
{
	var regPassword;
	var regFirstName = document.getElementById("reg_firstname").value;
	var regLastName = document.getElementById("reg_lastname").value;
	var regUsername = document.getElementById("reg_username").value;
	var regEmail = document.getElementById("reg_email").value;
	var regtempPassword = document.getElementById("reg_password").value;
	var regPasswordConfirm = document.getElementById("reg_password_confirm").value;
	document.getElementById("registerResult").innerHTML = "";

	// Check passwords match
	if(regtempPassword != regPasswordConfirm)
	{
		document.getElementById("registerResult").innerHTML = "Passwords don't match!";
		//document.getElementById("reg_firstname").value = reg_firstname.defaultValue;
		//document.getElementById("reg_lastname").value =reg_lastname.defaultValue;
		//document.getElementById("reg_username").value =reg_username.defaultValue;
		document.getElementById("reg_password").value =reg_password.defaultValue;
		document.getElementById("reg_password_confirm").value =reg_password_confirm.defaultValue;
		return;
	}

	// Check password exists
	if(regtempPassword == "" || regtempPassword == null || regtempPassword == undefined || regtempPassword == "" || regtempPassword == null || regPasswordConfirm == undefined)
	{
		document.getElementById("registerResult").innerHTML = "Invalid password!";
		//document.getElementById("reg_firstname").value = reg_firstname.defaultValue;
		//document.getElementById("reg_lastname").value =reg_lastname.defaultValue;
		//document.getElementById("reg_username").value =reg_username.defaultValue;
		document.getElementById("reg_password").value =reg_password.defaultValue;
		document.getElementById("reg_password_confirm").value =reg_password_confirm.defaultValue;
		return;
	}

	// sanitizing the login and Password
	var i;
	for(i = 0; i < regUsername.length; i++)
	{
		if(regUsername[i] === ';' || regUsername[i] === '/' || regUsername[i] === '-' || regUsername[i] == ')' || regUsername[i]=='(')
		{
			alert("There are illegal characters in your username");
			document.getElementById("regUsername").value = regUsername.defaultValue;
			document.getElementById("regPassword").value =regPassword.defaultValue;
			return;
		}
	}
	for(i = 0; i < regtempPassword.length; i++)
	{
		if(regtempPassword[i] === ';' || regtempPassword[i] === '/' || regtempPassword[i] === '-'|| regtempPassword[i] == ')' || regtempPassword[i]=='(')
		{
			alert("There are illegal characters in your password.");
			document.getElementById("loginName").value = loginName.defaultValue;
			document.getElementById("loginPassword").value =loginPassword.defaultValue;
			return;
		}
	}

		regPassword =  md5(regtempPassword);

	var jsonPayload = '{"firstName" : "' + regFirstName + '", "lastName" : "' + regLastName + '", "username" : "' + regUsername
	+ '", "password" : "' + regPassword + '", "email" : "' + regEmail +'"}';
	var url = urlBase + '/Register.' + extension;

	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
	try
	{
		xhr.onreadystatechange = function()
		{
			if (this.readyState == 4 && this.status == 200)

			{
				document.getElementById("registerResult").innerHTML = "registration successful!";
			}
		};
		xhr.send(jsonPayload);

		// forces you to login after registration is successful, I know it seems inefficient but
		// the register,php function is written like so
		// i could technically add the functionality by allowing this function to call the php.login function but we'll see for now
		//hideOrShow( "loggedInDiv", true);
		//hideOrShow( "accessUIDiv", true);
		//hideOrShow( "loginDiv", false);
	}
	catch(err)
	{
		document.getElementById("registerResult").innerHTML = err.message;
	}



	document.getElementById("reg_firstname").value = reg_firstname.defaultValue;
	document.getElementById("reg_lastname").value =reg_lastname.defaultValue;
	document.getElementById("reg_username").value =reg_username.defaultValue;
	document.getElementById("reg_password").value =reg_password.defaultValue;
	document.getElementById("reg_password_confirm").value =reg_password_confirm.defaultValue;
}
