package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ucfpathfinder.ucfpathfinder.CourseDirectory.Course;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CourseDatabase;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CourseListViewAdaptor;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CoursesDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Schedule");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // This thread should populate a list that shows the schedule.
                //TODO replaced buildings with schedule.

                CoursesDAO database = Room.databaseBuilder(MainActivity.this, CourseDatabase.class, "Course").build().getCourseDAO();
                List<Course> courseList = database.getCourses();
                ListView listView = findViewById(R.id.listView_mainActivity);
                if(!(courseList.isEmpty())) {
                    CourseListViewAdaptor courseListViewAdaptor = new CourseListViewAdaptor(MainActivity.this, courseList);
                    // Add the list to the activity.
                    listView.setAdapter(courseListViewAdaptor);
                }else
                {
                    if(listView.getAdapter() != null)
                    {
                        if(listView.getAdapter().getCount() > 0)
                            listView.removeAllViews();
                    }
                }
            }
        }).start();

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String id = sharedPreferences.getString("userID","No ID Found");

        // Adding a course
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,AddToSchedule.class));
            }
        });

        // Viewing course details.
        final ListView listView = findViewById(R.id.listView_mainActivity);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course courseSelected = (Course) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditDeleteCourse.class);
                intent.putExtra("CourseID", String.valueOf(courseSelected.getCourseID()));
                MainActivity.this.startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    // This will update the schedule list.
    public void onRestart() {
        super.onRestart();
        MainActivity.this.recreate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_agenda) {
            startActivity(new Intent(this,BuildingList.class));
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show();
            // TODO remove local user info.
            removeUserInfo();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeUserInfo()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().remove("userID").apply();
        sharedPreferences.edit().remove("username").apply();
        sharedPreferences.edit().remove("password").apply();
    }
}
