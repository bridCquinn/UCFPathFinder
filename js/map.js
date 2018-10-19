

function myMap()
{
	lastSearchLocationClicked = 0;
	hideOrShow("mapUIDiv", true);
	
	//var mapOptions = {
	//	center: new google.maps.LatLng(51.5, -0.12),
	//	zoom: 10,
	//	mapTypeId: google.maps.MapTypeId.HYBRID
	//}
	//var map = new google.maps.Map(document.getElementById("map"), mapOptions);
	
	searchLocation();
}

function searchLocation()
{
	while(myUL.hasChildNodes())
	{
		myUL.removeChild(myUL.childNodes[0]);
	}
	
		var srch = document.getElementById("searchText").value;
		
		var jsonPayload =  '{"userID": "'+ userId +'","search" : "' + srch + '"}';
		var url = urlBase + '/Search.' + extension;
		
		var xhr = new XMLHttpRequest();
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
		try
		{
			xhr.onreadystatechange = function()
			{
				if(this.readyState == 4 && this.status == 200)
				{
					var jsonObject=JSON.parse(xhr.responseText);
					var i;
					
					for( i=0; i<jsonObject.results.length-6; i+=11)
					{
						var li = document.createElement("li");
						var t = document.createTextNode(jsonObject.results[i+1] + " " + jsonObject.results[i+2]);
						
						li.appendChild(t);
						document.getElementById("mapUL").appendChild(li);
						
						var span = document.createElement("SPAN");
						var txt = document.createTextNode("\u00D7");
						li.id = jsonObject.results[i];
					//	li.setAttribute('onclick', "displayInfo(this.id)");
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
