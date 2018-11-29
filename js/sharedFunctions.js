var urlBase = 'http://ucfpathfinder.com/API';
var extension = "php";

var userId = 0;
var firstName = "";
var lastName = "";

var gTerm = "Fall";
var gYear = "2018";

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
