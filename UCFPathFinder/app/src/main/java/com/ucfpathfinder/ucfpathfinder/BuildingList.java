package com.ucfpathfinder.ucfpathfinder;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.AppDatabase;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.Building;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.BuildingsDAO;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.CustomListViewAdaptor;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.Course;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CourseDatabase;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CourseListViewAdaptor;
import com.ucfpathfinder.ucfpathfinder.CourseDirectory.CoursesDAO;

import java.util.List;

public class BuildingList extends AppCompatActivity {

    private List<Building> listOfBuildings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        this.setTitle("Building List");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        new Thread(new Runnable() {

            @Override
            public void run() {
                BuildingsDAO database = Room.databaseBuilder(BuildingList.this, AppDatabase.class, "Building").build().getBuildingsDAO();
                listOfBuildings = database.getBuildings();
                ListView listView = findViewById(R.id.list_buildingListActivity);
                CustomListViewAdaptor buildingListViewAdaptor = new CustomListViewAdaptor(BuildingList.this, listOfBuildings);
                listView.setAdapter(buildingListViewAdaptor);
            }
        }).start();

        final ListView listView = findViewById(R.id.list_buildingListActivity);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Building courseSelected = (Building) parent.getItemAtPosition(position);
                Intent intent = new Intent(BuildingList.this, BuildingDetails.class);
                intent.putExtra("buildingName", String.valueOf(courseSelected.getBuildingName()));
                intent.putExtra("buildingID", String.valueOf(courseSelected.getBuildingID()));
                intent.putExtra("buildingAbb", String.valueOf(courseSelected.getBuildingAbbreviation()));
                intent.putExtra("buildingPlusCode", String.valueOf(courseSelected.getPlusCode()));
                BuildingList.this.startActivity(intent);
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
