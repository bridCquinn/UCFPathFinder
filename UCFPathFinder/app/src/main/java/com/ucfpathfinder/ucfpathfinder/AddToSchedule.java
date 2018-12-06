package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.AppDatabase;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.Building;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.BuildingsDAO;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.AddDeleteWorker;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddToSchedule extends AppCompatActivity {

    private List<Building> buildings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_schedule);
        setTitle("Add Course");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Thread to retrieve the building information used in the Building spinner.
        new Thread(new Runnable() {
            @Override
            public void run() {
                BuildingsDAO database = Room.databaseBuilder(AddToSchedule.this, AppDatabase.class, "Building").build().getBuildingsDAO();
                buildings = database.getBuildings();
                ArrayList<String> arrayList = new ArrayList<>();
                for(int i = 0; i < buildings.size();i++)
                {
                    if(!(buildings.get(i).getBuildingAbbreviation().equals("")))
                        arrayList.add(buildings.get(i).getBuildingAbbreviation());
                }
                Collections.sort(arrayList);
                Spinner buildingSpinner = findViewById(R.id.spinner_building_addToSchedule);
                ArrayAdapter<String> adapter =  new ArrayAdapter<>(AddToSchedule.this, android.R.layout.simple_spinner_item, arrayList);
                buildingSpinner.setAdapter(adapter);
            }
        }).start();

        Button addButton = findViewById(R.id.button_add_addToSchedule);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the course to the database and remote server.
                EditText classNameEditText = findViewById(R.id.editText_className_addToSchedule);
                EditText classIDEditText = findViewById(R.id.editText_classID_addToSchedule);
                Spinner yearSpinner = findViewById(R.id.spinner_year_addToSchedule);
                Spinner termSpinner = findViewById(R.id.spinner_term_addToSchedule);
                Spinner startTimeSpinner = findViewById(R.id.spinner_startTime_addToSchedule);
                Spinner endTimeSpinner = findViewById(R.id.spinner_endTime_addToSchedule);
                Spinner daySpinner = findViewById(R.id.spinner_day_addToSchedule);
                Spinner buildingSpinner = findViewById(R.id.spinner_building_addToSchedule);

                String className = classNameEditText.getText().toString();
                if(className.equals(""))
                {
                    Toast.makeText(AddToSchedule.this, "Please enter a class name.", Toast.LENGTH_LONG).show();
                    return;
                }
                String classID = classIDEditText.getText().toString();
                if(classID.equals(""))
                {
                    Toast.makeText(AddToSchedule.this, "Please enter a class ID.", Toast.LENGTH_LONG).show();
                    return;
                }
                String year = yearSpinner.getSelectedItem().toString();
                String term = termSpinner.getSelectedItem().toString();
                String startTime = startTimeSpinner.getSelectedItem().toString();
                String endTime = endTimeSpinner.getSelectedItem().toString();
                String day = daySpinner.getSelectedItem().toString();
                String building = buildingSpinner.getSelectedItem().toString();

                Course course = new Course(0, className, classID, year, term, startTime, endTime, day, building);

                AddDeleteWorker addDeleteWorker = new AddDeleteWorker("add");
                addDeleteWorker.setCourse(course, AddToSchedule.this);
                Thread thread = new Thread(addDeleteWorker);
                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                classNameEditText.getText().clear();
                classIDEditText.getText().clear();
                yearSpinner.setSelection(0);
                daySpinner.setSelection(0);
                startTimeSpinner.setSelection(0);
                endTimeSpinner.setSelection(0);
                daySpinner.setSelection(0);
                //Intent intent = getIntent();

                finish();
                //startActivity(intent);

            }
        });


        Button cancelButton = findViewById(R.id.button_cancel_addToSchedule);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToSchedule.this.finish();
            }
        });
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
