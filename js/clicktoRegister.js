function hideOrShow( elementId, showState )
{
	var vis = "visible";
	var dis = "block";
	if( !showState )
	{
		vis = "hidden";
		dis = "none";
	}

	document.getElementById( elementId ).style.visibility = vis;
	document.getElementById( elementId ).style.display = dis;
}

function clickToRegister()
{
		hideOrShow( "clicktoregister", true);
		hideOrShow( "loginform", false);
}

function backToLogin()
{
		hideOrShow( "clicktoregister", false);
		hideOrShow( "loginform", true);
}