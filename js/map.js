
function searchLocation()
{
	while(mapUL.hasChildNodes())
	{
		mapUL.removeChild(mapUL.childNodes[0]);
	}

		var srch = document.getElementById("address").value;

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
						li.name = jsonObject['results'][i][2];
						li.setAttribute('onclick', "setBuildingTo(this.id); setBuildingID(this.name)"); // does nothing right now

						span.appendChild(txt);

					//	if(document.getElementById("div" + li.id) != null)
					//	{
						//	continue;
					//	} document.getElementById("searchText").value =  searchText.defaultValue; for laters



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

function setBuildingID(id)
{
	alert(id);
}

function setBuildingTo(location)
{

 	document.getElementById("address").value = location;
	alert(location);

}
