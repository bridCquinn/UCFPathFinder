// Profile

// All functions for profile tab
// Also holds the functions that deal with switching Term or Year on Schedule Page

function changeTerm() {
  var term = document.getElementById("profileTerm").value;
  var year = document.getElementById("profileYear").value;
  document.getElementById("termYear").innerHTML= term + " " + year;

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
    }

    for(j = 0; j < scheduleList.length; j++)
    {
      makeTile(scheduleList[j]);
    }

  }
  catch(err)
  {
    // make new error message
    // document.getElementById("loginResult").innerHTML = err.message;
  }
}

function popUp(elementId) {
  if(document.getElementById("term").value != "Term" && document.getElementById("year").value != "Year" && gTerm != "" && gYear!= "" && (document.getElementById("year").value != gYear || document.getElementById("term").value != gTerm))
    $("#" + elementId + "Check").modal();
}

function change() {
  document.getElementById("profileTerm").value = document.getElementById("term").value;
  document.getElementById("profileYear").value = document.getElementById("year").value;
  changeTerm();

  if(elementId == "term")
    gTerm = document.getElementById(elementId).value;
  else
    gYear = document.getElementById(elementId).value;
    
  document.getElementById('newSch').style.display = 'none';
}

function resume() {
  document.getElementById("term").value = gTerm;
  document.getElementById("year").value = gYear;
}
