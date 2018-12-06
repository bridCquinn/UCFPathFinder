package com.ucfpathfinder.ucfpathfinder;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.Building;

import org.w3c.dom.Text;

public class BuildingDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);

        this.setTitle("Building Details");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String buildingName = intent.getStringExtra("buildingName");
        String buildingID = intent.getStringExtra("buildingID");
        String buildingAbb = intent.getStringExtra("buildingAbb");
        final String buildingPlusCode = intent.getStringExtra("buildingPlusCode");

        Button routeButton = findViewById(R.id.button_route_buildingDetail);
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuildingDetails.this, MapsActivity.class);
                intent.putExtra("plusCode", buildingPlusCode);
                startActivity(intent);
                finish();
            }
        });


        TextView buildingNameTV = findViewById(R.id.textView_buildingName_buildingDetails);
        buildingNameTV.setText(buildingName);
        buildingNameTV.setGravity(Gravity.CENTER);
        buildingNameTV.setTextSize(18);

        TextView buildingIDTV = findViewById(R.id.textView_buildingID_buildingDetails);
        buildingIDTV.setText(buildingID);
        buildingIDTV.setGravity(Gravity.CENTER);
        buildingIDTV.setTextSize(18);

        TextView buildingAbbTV = findViewById(R.id.textView_buidlingAbb_buildingDetails);
        buildingAbbTV.setText(buildingAbb);
        buildingAbbTV.setGravity(Gravity.CENTER);
        buildingAbbTV.setTextSize(18);

        TextView buildingPlusCodeTV = findViewById(R.id.textView_buildingPlusCode_buidlingDetails);
        buildingPlusCodeTV.setText(buildingPlusCode);
        buildingPlusCodeTV.setGravity(Gravity.CENTER);
        buildingPlusCodeTV.setTextSize(18);

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
