// this is the main.js file
// it deals with the js in the toolbar
// this includes:
//   - LogOut
//   - tabs
//   - the changing of pages
//     ->the majority of content on each page will
//       have a seperate js file

function openPage(pageName, elmnt, color) {
    // Hide all elements with class="tabcontent" by default */
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Remove the background color of all tablinks/buttons
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].style.backgroundColor = "";
        tablinks[i].style.color = "";
    }

    // Show the specific tab content
    document.getElementById(pageName).style.display = "block";

    // Add the specific color to the button used to open the tab content
    elmnt.style.backgroundColor = color;
    elmnt.style.color = 'white';
}

function doLogout(){
	userId = 0;
	firstName = "";
	lastName = "";

  hideOrShow( "tabs", false);
  hideOrShow( "accessUIDiv", false);
  hideOrShow( "loginPage", true);
}
