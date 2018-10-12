
function Course(){
  this.className = "";
  this.classCode = "";
  this.startTime = "";
  this.endTime = "";
  this.building = "";
  this.notes = "";
};

var scheduleList = [];

function addClass(){
  var course = new Course();

  course.className = document.getElementById("code").value;
  course.classCode = document.getElementById("name").value;
  course.startTime = document.getElementById("start").value;
  course.endTime = document.getElementById("end").value;
  course.building = document.getElementById("address").value;
  course.notes = document.getElementById("note").value;

  scheduleList.push(course);

  // alert(scheduleList[0].className + "," + course.classCode+ "," +course.startTime+ "," +course.endTime+ "," +course.building+ "," +course.notes);

  // need to make sure the list is updates properly
  // the scroll bar needs to be put in
}

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
