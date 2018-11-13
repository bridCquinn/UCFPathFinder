
function Course(){
  this.building = "";
  this.className = "";
  this.classDays = "";
  this.startTime = "";
  this.endTime = "";
  this.classCode = "";
  this.term = "";
  this.year = "";
  this.notes = "";
};

var scheduleList = [];

// this is used to add to the physical collapsable list
function addtoList(course){
  var li = document.createElement("li");
  var t = document.createTextNode(course.classCode + " - " + course.className);
  li.appendChild(t);
  document.getElementById("myUL").appendChild(li);
  var span = document.createElement("SPAN");
  var txt = document.createTextNode("\u00D7");
  li.id = course.classCode;
  li.setAttribute('onclick', "toggle(course)");
  span.appendChild(txt);

  var div = document.createElement("div");
  div.id = ("div" + li.id);
  div.style.display = "none";
  div.style.visibility = "hidden";

  document.getElementById("name").value = "";
  document.getElementById("code").value = code.defaultValue;
  document.getElementById("ddlPattern").selectedIndex = 0;
  document.getElementById("start").value = start.defaultValue;
  document.getElementById("end").value = end.defaultValue;
  document.getElementById("address").value = address.defaultValue;
  document.getElementById("note").value = note.defaultValue;
}

// adds class to the schedulelist and is triggered by the addClass button
function addClass(){
  var course = new Course();

  course.className = document.getElementById("name").value;
  course.classCode = document.getElementById("code").value;
  course.classDays = document.getElementById("ddlPattern").value;
  course.startTime = document.getElementById("start").value;
  course.endTime = document.getElementById("end").value;
  course.building = document.getElementById("address").value;
  course.notes = document.getElementById("note").value;

  scheduleList.push(course);

  addtoList(course);
}

// is activated when newSchedule is pressed
// brings up the newsch modal
function newSchedule() {
  document.getElementById('newSch').style.display = 'block';
}

function toggle(elementId){
  var x = document.getElementById(elementId);
    if (x.style.visibility === "visible") {
        x.style.visibility = "hidden";
    } else {
        x.style.visibility = "visible";
    }
}

// used to create a completely new schedule and submits that final
// classes to the API
function makeSchedule() {
  term = document.getElementById("term").value;
  year = document.getElementById("year").value;

  for(i = 0; i < scheduleList.length; i++)
  {
    makeTile(scheduleList[i]);
    scheduleList[i].year = year;
    scheduleList[i].term = term;
  }
  document.getElementById("termYear").innerHTML= term + " " + year;
  document.getElementById("makeSchResult").innerHTML = "";

  var jsonPayload = '{"userID" : "'+userId+'", "schedule" : '+JSON.stringify(scheduleList)+'}';

  var url = urlBase + '/MakeSchedule.' + extension;

	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
	try
	{
		xhr.onreadystatechange = function()
		{
			if (this.readyState == 4 && this.status == 200)
			{
				//document.getElementById("contactAddResult").innerHTML = "Contact has been added";
				;
			}
		};
		xhr.send(jsonPayload);
	}
	catch(err)
	{
		document.getElementById("makeSchResult").innerHTML = err.message;
	}

  document.getElementById('newSch').style.display = 'none';


}

// delete the entire schedule
function deleteSchedule()
{
  if(confirm("Confirm Delete"))
  {
    ;
  }
  else {
    return;
  }

  var jsonPayload = '{"userId": "'+ userId +'","contactId"}';
  var url = urlBase + '/DeleteSchedule.' + extension;

  var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
	try
	{
		xhr.onreadystatechange = function()
	 	{
	 		if (this.readyState == 4 && this.status == 200)
			{
				;
				//document.getElementById("deleteContactResult").innerHTML = "Contact has been deleted!";
			}
	 	};
	 	xhr.send(jsonPayload);
	}
	catch(err)
	{
		document.getElementById("DeleteSchedule").innerHTML = err.message;
	}

}

function makeTile(course)
{
  var card = document.createElement("div");
  var body = document.createElement("div");
  var h5 = document.createElement("H5")
  var h6code = document.createElement("H6");
  var h6time = document.createElement("H6");
  var ptime = document.createElement("p");
  var pnote = document.createElement("p");
  var title = document.createTextNode(course.className);
  var code = document.createTextNode(course.classCode);
  var days = document.createTextNode(course.classDays);
  var time = document.createTextNode(course.startTime + " - " + course.endTime);
  var place = document.createTextNode(course.building);
  var notes = document.createTextNode(course.notes);

  card.appendChild(body);
  body.appendChild(h5);
  h5.appendChild(title);
  body.appendChild(h6code);
  h6code.appendChild(code);
  body.appendChild(days);
  body.appendChild(ptime);
  ptime.appendChild(h6time);
  h6time.appendChild(time);
  body.appendChild(place);
  body.appendChild(pnote);
  pnote.appendChild(notes);

  card.classList.add("card-1");
  card.style.float = "left";
  body.classList.add("card-body");
  h5.classList.add("card-title");
  h6code.classList.add("card-subtitle");
  ptime.classList.add("card-text");
  pnote.classList.add("card-text");
  document.getElementById("classes").appendChild(card);
  var span = document.createElement("SPAN");
  var txt = document.createTextNode("\u00D7");
  span.appendChild(txt);

}
