package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.AppDatabase;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.Building;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.BuildingsDAO;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.AddDeleteWorker;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.Course;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CourseDatabase;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CoursesDAO;


import java.util.List;


public class EditDeleteCourse extends AppCompatActivity {

    private Course courseToEdit;
    private int courseID;
    private String buildingAbb;
    private String buildingPlusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_course);
        this.setTitle("Course Details");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String temp = intent.getStringExtra("CourseID");

        courseID = Integer.parseInt(temp);

        Button editButton = findViewById(R.id.button_edit_editDeleteActivity);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button routeButton = findViewById(R.id.button_route_editDeleteActivity);
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDeleteCourse.this, MapsActivity.class);
                intent.putExtra("plusCode", buildingPlusCode);
                startActivity(intent);
                finish();
            }
        });

        Button deleteButton = findViewById(R.id.button_delete_editDeleteActivity);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDeleteWorker addDeleteWorker = new AddDeleteWorker("delete");
                addDeleteWorker.setCourse(courseToEdit, EditDeleteCourse.this);
                Thread thread = new Thread(addDeleteWorker);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    public void onResume()
    {
        super.onResume();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CoursesDAO database = Room.databaseBuilder(EditDeleteCourse.this, CourseDatabase.class, "Course").build().getCourseDAO();
                List<Course> courseList = database.getCourses();
                for(int i = 0; i < courseList.size(); i++) {
                    if (courseList.get(i).getCourseID() == courseID)
                        courseToEdit = courseList.get(i);
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView temp = findViewById(R.id.textView_courseName_editDeleteActivity);
        temp.setText(courseToEdit.getClassName());
        temp.setGravity(Gravity.CENTER);

        // Course id row zero.
        TextView textViewCourseIdLeft = findViewById(R.id.textViewR0C0);
        textViewCourseIdLeft.setText("Course Code");
        textViewCourseIdLeft.setGravity(Gravity.LEFT);
        TextView textViewCourseIdRight = findViewById(R.id.textViewR0C1);
        textViewCourseIdRight.setText(courseToEdit.getClassCode());
        textViewCourseIdRight.setGravity(Gravity.RIGHT);

        // Course id row one.
        TextView textViewCourseYearLeft = findViewById(R.id.textViewR1C0);
        textViewCourseYearLeft.setText("Year");
        textViewCourseIdLeft.setGravity(Gravity.LEFT);
        TextView textViewCourseYearRight = findViewById(R.id.textViewR1C1);
        textViewCourseYearRight.setText(courseToEdit.getYear());
        textViewCourseYearRight.setGravity(Gravity.RIGHT);

        // Course id row two.
        TextView textViewCourseTermLeft = findViewById(R.id.textViewR2C0);
        textViewCourseTermLeft.setText("Term");
        textViewCourseTermLeft.setGravity(Gravity.LEFT);
        TextView textViewCourseTermRight = findViewById(R.id.textViewR2C1);
        textViewCourseTermRight.setText(courseToEdit.getTerm());
        textViewCourseTermRight.setGravity(Gravity.RIGHT);

        // Course id row three.
        TextView textViewCourseStartLeft = findViewById(R.id.textViewR3C0);
        textViewCourseStartLeft.setText("Start Time");
        textViewCourseStartLeft.setGravity(Gravity.LEFT);
        TextView textViewCourseStartRight = findViewById(R.id.textViewR3C1);
        textViewCourseStartRight.setText(courseToEdit.getStartTime());
        textViewCourseStartRight.setGravity(Gravity.RIGHT);

        // Course id row four.
        TextView textViewCourseEndLeft = findViewById(R.id.textViewR4C0);
        textViewCourseEndLeft.setText("End Time");
        textViewCourseEndLeft.setGravity(Gravity.LEFT);
        TextView textViewCourseEndRight = findViewById(R.id.textViewR4C1);
        textViewCourseEndRight.setText(courseToEdit.getEndTime());
        textViewCourseEndRight.setGravity(Gravity.RIGHT);

        // Course id row five.
        TextView textViewCourseDayLeft = findViewById(R.id.textViewR5C0);
        textViewCourseDayLeft.setText("Day");
        textViewCourseDayLeft.setGravity(Gravity.LEFT);
        TextView textViewCourseDayRight = findViewById(R.id.textViewR5C1);
        textViewCourseDayRight.setText(courseToEdit.getDay());
        textViewCourseDayRight.setGravity(Gravity.RIGHT);

        // Course id row five.
        TextView textViewCourseBuildingLeft = findViewById(R.id.textViewR6C0);
        textViewCourseBuildingLeft.setText("Building");
        textViewCourseBuildingLeft.setGravity(Gravity.LEFT);
        TextView textViewCourseBuildingRight = findViewById(R.id.textViewR6C1);
        textViewCourseBuildingRight.setText(getBuildingAbbr());
        textViewCourseBuildingRight.setGravity(Gravity.RIGHT);
    }

    private String getBuildingAbbr()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BuildingsDAO database = Room.databaseBuilder(EditDeleteCourse.this, AppDatabase.class, "Building").build().getBuildingsDAO();
                List<Building> list = database.getBuildings();
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getBuildingID() == Integer.parseInt(courseToEdit.getBuilding())) {
                        buildingAbb = list.get(i).getBuildingName();
                        buildingPlusCode = list.get(i).getPlusCode();
                        break;
                    }
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buildingAbb;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
