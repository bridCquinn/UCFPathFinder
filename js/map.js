

function myMap()
{
	lastSearchLocationClicked = 0;

	//var mapOptions = {
	//	center: new google.maps.LatLng(51.5, -0.12),
	//	zoom: 10,
	//	mapTypeId: google.maps.MapTypeId.HYBRID
	// curly bracket goes here
	//var map = new google.maps.Map(document.getElementById("map"), mapOptions);

	searchLocation();
}

function searchLocation()
{
	while(mapUL.hasChildNodes())
	{
		mapUL.removeChild(mapUL.childNodes[0]);
	}

		var srch = document.getElementById("searchText").value;

		if(srch == "")
			return;

		var jsonPayload =  '{"search" : "' + srch + '"}';
		var url = urlBase + '/SearchBuildings.' + extension;

		var xhr = new XMLHttpRequest();
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
		try
		{
			xhr.onreadystatechange = function()
			{
				if(this.readyState == 4 && this.status == 200)
				{
					var jsonObject = JSON.parse(xhr.responseText);
					var i;
					//var array = JSON.stringify(jsonObject[result"][1]);
					var x = "";



					for(i=0; i < jsonObject['results'].length ; i++)
					{
						x = jsonObject['results'][i][2];
						//console.log(x);
						var li = document.createElement("li");
						var t = document.createTextNode(JSON.stringify(x));

						li.appendChild(t);
						document.getElementById("mapUL").appendChild(li);

						var span = document.createElement("SPAN");
						var txt = document.createTextNode("\u00D7");
						li.id = jsonObject['results'][i][0];
						li.setAttribute('onclick', "setBuildingTo(this.id)"); // does nothing right now
						span.appendChild(txt);

					//	if(document.getElementById("div" + li.id) != null)
					//	{
						//	continue;
					//	}



					}

				}
			};
			xhr.send(jsonPayload);
		}
		catch(err)
		{
			;
		}

}


function setBuildingTo(location)
{

 	document.getElementById("searchText").value =  location;

}






function displayInfo(location)
{
	if(lastSearchLocationClicked >= 1)
	{
		document.getElementById("div" + lastSearchLocationClicked).style.display = "none";
		document.getElementById("div" + lastSearchLocationClicked).style.visibility = "hidden";
	}

	document.getElementById("div" + location).style.display = "block";
	document.getElementById("div" + location).style.visibility = "visible";

	lastSearchLocationClicked = location;

}
