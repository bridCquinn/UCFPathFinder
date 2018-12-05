package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ucfpathfinder.ucfpathfinder.CourseDirectory.Course;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CourseDatabase;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CoursesDAO;


import java.util.List;


public class EditDeleteCourse extends AppCompatActivity {

    private Course courseToEdit;
    private int courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_course);
        this.setTitle("Course Details");


        Intent intent = getIntent();
        String temp = intent.getStringExtra("CourseID");

        courseID = Integer.parseInt(temp);




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

        // Course id row zero.
        TextView textViewCourseIdLeft = findViewById(R.id.textViewR0C0);
        textViewCourseIdLeft.setText("Course Code");
        TextView textViewCourseIdRight = findViewById(R.id.textViewR0C1);
        textViewCourseIdRight.setText(courseToEdit.getClassCode());

        // Course id row one.
        TextView textViewCourseYearLeft = findViewById(R.id.textViewR1C0);
        textViewCourseYearLeft.setText("Year");
        TextView textViewCourseYearRight = findViewById(R.id.textViewR1C1);
        textViewCourseYearRight.setText(courseToEdit.getYear());

        // Course id row two.
        TextView textViewCourseTermLeft = findViewById(R.id.textViewR2C0);
        textViewCourseTermLeft.setText("Term");
        TextView textViewCourseTermRight = findViewById(R.id.textViewR2C1);
        textViewCourseTermRight.setText(courseToEdit.getTerm());

        // Course id row three.
        TextView textViewCourseStartLeft = findViewById(R.id.textViewR3C0);
        textViewCourseStartLeft.setText("Start Time");
        TextView textViewCourseStartRight = findViewById(R.id.textViewR3C1);
        textViewCourseStartRight.setText(courseToEdit.getStartTime());

        // Course id row four.
        TextView textViewCourseEndLeft = findViewById(R.id.textViewR4C0);
        textViewCourseEndLeft.setText("End Time");
        TextView textViewCourseEndRight = findViewById(R.id.textViewR4C1);
        textViewCourseEndRight.setText(courseToEdit.getEndTime());

        // Course id row five.
        TextView textViewCourseDayLeft = findViewById(R.id.textViewR5C0);
        textViewCourseDayLeft.setText("Day");
        TextView textViewCourseDayRight = findViewById(R.id.textViewR5C1);
        textViewCourseDayRight.setText(courseToEdit.getDay());

        // Course id row five.
        TextView textViewCourseBuildingLeft = findViewById(R.id.textViewR6C0);
        textViewCourseBuildingLeft.setText("Building");
        TextView textViewCourseBuildingRight = findViewById(R.id.textViewR6C1);
        textViewCourseBuildingRight.setText(courseToEdit.getBuilding());


    }
}
