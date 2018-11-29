
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
var temp = [];
var lastPressed = -1;

// this is used to add to the physical list
// creates the list node with all of the information
function addtoList(course){
  var li = document.createElement("li");
  var bold = document.createElement("b");
  var t = document.createTextNode(course.classCode + " - " + course.className);
  li.appendChild(bold);
  bold.appendChild(t);
  document.getElementById("myUL").appendChild(li);
  var span = document.createElement("SPAN");
  var txt = document.createTextNode("\u00D7");
  li.id = "list" + course.classID;
  span.appendChild(txt);
  li.setAttribute('onclick', "choose(this.id)");

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
  course.term = document.getElementById("term").value;
  course.year = document.getElementById("year").value;
  gTerm = course.term;
  gYear = course.year;

  if(course.className != "" && course.classCode != "" && course.term != "" && course.year != "" && course.term != "Term" && course.year != "Year")
  {
    scheduleList.push(course);

    addtoList(course);
  }
  else {
    $("#exampleModalCenter").modal();
  }

  temp.push(course);
  makeSchedule();
  temp=[];
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

// is used in the add class and submits to database
function makeSchedule() {

  term = document.getElementById("term").value;
  year = document.getElementById("year").value;

  document.getElementById("termYear").innerHTML= term + " " + year;
  document.getElementById("makeSchResult").innerHTML = "";


  var jsonPayload = '{"userID" : "'+userId+'", "schedule" : '+JSON.stringify(temp)+'}';
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
    var jsonObject = JSON.parse( xhr.responseText );
	}
	catch(err)
	{
		document.getElementById("makeSchResult").innerHTML = err.message;
	}

  alert(jsonObject.classID);
}

function makeSchHelp() {

  var term  = document.getElementById("term").value;
  var year  = document.getElementById("year").value;

  document.getElementById("classes").innerHTML = "";
  document.getElementById("myUL").innerHTML = "";
  scheduleList = [];

  var jsonPayload = '{"userID" : "'+ userId +'", "term" : "'+ term +'", "year" : "'+ year +'"}';
  var url = urlBase + '/GetSchedule.' + extension;

  var xhr = new XMLHttpRequest();
  xhr.open("POST", url, false);
  xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
  try
  {
  	xhr.send(jsonPayload);

  	var jsonObject = JSON.parse( xhr.responseText );
  }
  catch(err)
  {
    // make new error message
  	// document.getElementById("loginResult").innerHTML = err.message;
  }

  alert(jsonObject.schedule.length);
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
  }

  for(j = 0; j < scheduleList.length; j++)
  {
    makeTile(scheduleList[j]);
  }


  document.getElementById('newSch').style.display = 'none';
  document.getElementById('delSch').style.display = 'block';

  document.getElementById("name").value = "";
  document.getElementById("code").value = code.defaultValue;
  document.getElementById("ddlPattern").selectedIndex = 0;
  document.getElementById("start").value = start.defaultValue;
  document.getElementById("end").value = end.defaultValue;
  document.getElementById("address").value = address.defaultValue;
  document.getElementById("note").value = note.defaultValue;
}

function delModal() {
    $("#delCheck").modal();
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
  card.id = "edit" + course.classID;
  body.classList.add("card-body");
  span.classList.add("close");
  span.id = "del" + course.classID;
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

  var course = findCourse("del",code);

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
  // delete list element
  deleteListElement(course);

}

// deletes tile and list
function deleteTile(course){
    var element = document.getElementById("edit" + course.classID);
    element.parentNode.removeChild(element);
}

function deleteListElement(course){
  var listelement = document.getElementById("list" + course.classID);
  listelement.parentNode.removeChild(listelement);
}

// find course in scheduleList from the course Code
function findCourse(word, id){

  //breaking down the id to only the classID
  var classID = id.substring(word.length);

  // finds course in array
  for(i = 0; i < scheduleList.length; i++)
  {
    if(scheduleList[i].classID == classID)
      return scheduleList[i];
  }
  alert("Not found!");
}

function findDaysIndex(days) {
  for(i = 0; i < document.getElementById("ddlPattern").options.length; i++)
    if(document.getElementById("ddlPattern").options[i].value == days)
      return i;
}

function choose(code)
{
  var course = findCourse("list",code);

  document.getElementById("name").value = course.className;
  document.getElementById("code").value = course.classCode;
  document.getElementById("ddlPattern").selectedIndex = findDaysIndex(course.classDays);
  document.getElementById("ddlPattern").value = course.classDays;
  document.getElementById("start").value = course.startTime;
  document.getElementById("end").value = course.endTime;
  document.getElementById("address").value = course.building;
  document.getElementById("note").value = course.notes;
  document.getElementById("term").value = course.term;
  document.getElementById("year").value = course.year;

  hideOrShow("add", false);
  hideOrShow("save", true);
  hideOrShow("sub", false);
  hideOrShow("fin", true);
  hideOrShow("back", true);

  lastPressed = course.classID;
}

function saveEdit(){

  // finding the correct course from the scheuleList to edit
  for(i = 0; i < scheduleList.length; i++)
    if(scheduleList[i].classID == lastPressed)
      break;

  gTerm = document.getElementById("term").value;
  gYear = document.getElementById("year").value;
  // changes values in schedule
  scheduleList[i].className = document.getElementById("name").value;
  scheduleList[i].classCode = document.getElementById("code").value;
  scheduleList[i].classDays = document.getElementById("ddlPattern").value;
  scheduleList[i].startTime = document.getElementById("start").value;
  scheduleList[i].endTime = document.getElementById("end").value;
  scheduleList[i].building = document.getElementById("address").value;
  scheduleList[i].notes = document.getElementById("note").value;


  // places default values in the text boxes
  document.getElementById("name").value = "";
  document.getElementById("code").value = code.defaultValue;
  document.getElementById("ddlPattern").selectedIndex = 0;
  document.getElementById("start").value = start.defaultValue;
  document.getElementById("end").value = end.defaultValue;
  document.getElementById("address").value = address.defaultValue;
  document.getElementById("note").value = note.defaultValue;

  deleteListElement(scheduleList[i]);

  var li = document.createElement("li");
  var t = document.createTextNode(scheduleList[i].classCode + " - " + scheduleList[i].className);
  li.appendChild(t);
  document.getElementById("myUL").appendChild(li);
  var span = document.createElement("SPAN");
  var txt = document.createTextNode("\u00D7");
  li.id = "list" + scheduleList[i].classID;
  span.appendChild(txt);
  li.setAttribute('onclick', "choose(this.id)");

}

function finishEdit(){

  for(i = 0; i < scheduleList.length; i++)
  {
    deleteTile(scheduleList[i]);
    makeTile(scheduleList[i]);
  }
    document.getElementById('newSch').style.display = 'none';

    back();

  var jsonPayload = '{"userID" : "'+ userId +'", "schedule" : '+JSON.stringify(scheduleList)+'}';

  var url = urlBase + '/EditClass.' + extension;

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

}

function back()
{
  hideOrShow("add", true);
  hideOrShow("save", false);
  hideOrShow("sub", true);
  hideOrShow("fin", false);
  hideOrShow("back", false);

  // places default values in the text boxes
  document.getElementById("name").value = "";
  document.getElementById("code").value = code.defaultValue;
  document.getElementById("ddlPattern").selectedIndex = 0;
  document.getElementById("start").value = start.defaultValue;
  document.getElementById("end").value = end.defaultValue;
  document.getElementById("address").value = address.defaultValue;
  document.getElementById("note").value = note.defaultValue;
}
