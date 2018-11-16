
function Course(){
  this.classID = -1;
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

// this is used to add to the physical list
// creates the list node with all of the information
function addtoList(course){
  var li = document.createElement("li");
  var t = document.createTextNode(course.classCode + " - " + course.className);
  li.appendChild(t);
  document.getElementById("myUL").appendChild(li);
  var span = document.createElement("SPAN");
  var txt = document.createTextNode("\u00D7");
  li.id = "list" + course.classCode;
  li.setAttribute('onclick', "toggle(course)");
  span.appendChild(txt);

  document.getElementById("name").value = "";
  document.getElementById("code").value = code.defaultValue;
  document.getElementById("ddlPattern").selectedIndex = 0;
  document.getElementById("start").value = start.defaultValue;
  document.getElementById("end").value = end.defaultValue;
  document.getElementById("address").value = address.defaultValue;
  document.getElementById("note").value = note.defaultValue;
}

// triggered by the addClass button
// creates a new course object and fills it with data
// then adds it to the scheduleList
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

// used to create a completely new schedule and submits the final
// classes to the API
function makeSchedule() {

  document.getElementById("classes").innerHTML = "";

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
  document.getElementById('delSch').style.display = 'block';


}

// delete the entire schedule
function deleteSchedule()
{
  var jsonPayload = '{"userId": "'+ userId +'"}';
  var url = urlBase + '/DeleteAllClasses.' + extension;

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

  document.getElementById("classes").innerHTML = "";
  document.getElementById("myUL").innerHTML = "";
  scheduleList = [];
  document.getElementById("delSch").style.display = 'none';
}

// makes the GUI tile of each class
function makeTile(course)
{
  var card = document.createElement("div");
  var span = document.createElement("span");
  var body = document.createElement("div");
  var h5 = document.createElement("H5")
  var h6code = document.createElement("H6");
  var h6time = document.createElement("H6");
  var ptime = document.createElement("p");
  var pnote = document.createElement("p");
  var close = document.createTextNode("x");
  var title = document.createTextNode(course.className);
  var code = document.createTextNode(course.classCode);
  var days = document.createTextNode(course.classDays);
  var time = document.createTextNode(course.startTime + " - " + course.endTime);
  var place = document.createTextNode(course.building);
  var notes = document.createTextNode(course.notes);

  card.appendChild(body);
  body.appendChild(h5);
  h5.appendChild(span);
  span.appendChild(close);
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
  card.id = course.classCode + course.className;
  // needs editing ( is not getting the code)
  // card.setAttribute("onclick", "editClass(this.id)");
  body.classList.add("card-body");
  span.classList.add("close");
  span.id = course.classCode;
  span.setAttribute("onclick", "deleteClass(this.id)");
  span.style.top = 0;
  span.style.right = 0;
  h5.classList.add("card-title");
  h6code.classList.add("card-subtitle");
  ptime.classList.add("card-text");
  pnote.classList.add("card-text");
  document.getElementById("classes").appendChild(card);
  var span = document.createElement("SPAN");
  var txt = document.createTextNode("\u00D7");
  span.appendChild(txt);

}

// deletes class, updates array
// also removes delete button on empty list
function deleteClass(code){

  var course = findCourse(code);

  var jsonPayload = '{"userId": "'+ userId +'","classCode": "'+ course.classCode +'", "term": "'+ course.term +'", "year": "' + course.year + '"}';
  var url = urlBase + '/DeleteClass.' + extension;

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
		document.getElementById("DeleteClass").innerHTML = err.message;
	}

  // makes sure the scheduleList is updated
  scheduleList.splice( scheduleList.indexOf(course), 1 );

  // takes the tile off of the GUI
  deleteTile(course);

  if(scheduleList < 1)
    document.getElementById("delSch").style.display = 'none';
}

// deletes tile and list
function deleteTile(course){
    var element = document.getElementById(course.classCode + course.className);
    element.parentNode.removeChild(element);

    var listelement = document.getElementById("list" + course.classCode);
    listelement.parentNode.removeChild(listelement);

}

// find course in scheduleList from the course Code
function findCourse(code){
  for(i = 0; i < scheduleList.length; i++)
  {
    if(scheduleList[i].classCode == code)
      return scheduleList[i];
  }
  alert("Not found!");
}

// when tile is pressed, the make/edit modal is unhidden
// when list item is pressed, info is placed back in text boxes
// this allows them to be edited
// function editClass(code)
// {
//   // get course from scheduleList
//   var course = findCourse(code);
//   // brings up the schedule modal
//   newSchedule();
//
//   document.getElementById("name").value = course.className;
//   document.getElementById("code").value = course.classCode;
//   document.getElementById("ddlPattern").selectedIndex = course.days;
//   document.getElementById("start").value = course.startTime;
//   document.getElementById("end").value = course.endTime;
//   document.getElementById("address").value = course.building;
//   document.getElementById("note").value = course.notes;
//
//   var jsonPayload = '{"userID" : "'+userId+'", "schedule" : '+JSON.stringify(scheduleList)+'}';
//
//   var url = urlBase + '/EditClass.' + extension;
//
//   var xhr = new XMLHttpRequest();
//   xhr.open("POST", url, true);
//   xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
//   try
//   {
//     xhr.onreadystatechange = function()
//     {
//       if (this.readyState == 4 && this.status == 200)
//       {
//         //document.getElementById("contactAddResult").innerHTML = "Contact has been added";
//         ;
//       }
//     };
//     xhr.send(jsonPayload);
//   }
//   catch(err)
//   {
//     document.getElementById("makeSchResult").innerHTML = err.message;
//   }
//
// }
