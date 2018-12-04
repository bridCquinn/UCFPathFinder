package com.ucfpathfinder.ucfpathfinder.CourseDirectory;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.AppDatabase;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.Building;
import com.ucfpathfinder.ucfpathfinder.BuildingDirectory.BuildingsDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AddDeleteWorker implements AddDeleteRunnable {

    private Course course;
    private Context context;

    @Override
    public void setCourse(Course course, Context context) {
        this.course = course;
        this.context = context;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public void run() {
        // 1) Add course to the database.
        // 2) Update the ROOM Course database.
        try
        {
            URL url = new URL("http://ucfpathfinder.com/API/MakeSchedule.php");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            JSONObject jsonOutput = createAddJsonOutput();


            // Output to the server.
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonOutput.toString());
            writer.flush();
            writer.close();
            outputStream.close();

            // JSON received from server.
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String lineTemp;
            StringBuilder resultOfRegister = new StringBuilder();
            while((lineTemp = bufferedReader.readLine())!=null)
                resultOfRegister.append(lineTemp);
            //resultOfRegister = resultOfRegister + lineTemp;
            Log.d("Add Result",resultOfRegister.toString());

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private JSONObject createAddJsonOutput() throws JSONException
    {
        //TODO This needs to accept the building ID, not he abbreviation.
        JSONObject jsonCourse = new JSONObject();
        jsonCourse.put("building",findBuildingID());
        jsonCourse.put("className",course.getClassName());
        jsonCourse.put("startTime",reformatTimeToMilitary(course.getStartTime()));
        jsonCourse.put("endTime",reformatTimeToMilitary(course.getEndTime()));
        jsonCourse.put("classCode",course.getClassCode());
        jsonCourse.put("term",course.getTerm());
        jsonCourse.put("year",course.getYear());
        jsonCourse.put("notes","");
        jsonCourse.put("classDays",course.getDay());

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonCourse);

        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("userID", getUserID());
        jsonOutput.put("schedule", jsonArray);
        Log.d("Output ADDING", jsonOutput.toString());
        return jsonOutput;
    }

    private String reformatTimeToMilitary(String time)
    {
        //TODO Finish this function: reformat the time.
        String result = "";
        if(time.contains("AM"))
        {
            char[] array = time.toCharArray();
            array[5] = ':';
            array[6] = '0';
            array[7] = '0';
            result = String.copyValueOf(array);
            return result;
        }
        if (time.contains("PM"))
        {
            char[] array = time.toCharArray();
            int hour = Integer.parseInt(time.substring(0,2));
            hour = hour + 12;
            char[] temp = Integer.toString(hour).toCharArray();
            array[0] = temp[0];
            array[1] = temp[1];
            array[5] = ':';
            array[6] = '0';
            array[7] = '0';
            result = String.copyValueOf(array);
            return result;
        }


        return result;
    }

    private int findBuildingID()
    {
        BuildingsDAO database = Room.databaseBuilder(this.context, AppDatabase.class, "Building").build().getBuildingsDAO();
        List<Building> list = database.getBuildings();
        int buildingID = -1;
        String buildingAbb = course.getBuilding();
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getBuildingAbbreviation().equals(buildingAbb))
            {
                buildingID = list.get(i).getBuildingID();
                break;
            }
        }
        // TODO if building not found, return -1. Need to implement a handler.
        return buildingID;
    }

    private String getUserID()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String userID = sharedPreferences.getString("userID","No ID Found");
        return  userID;
    }
}
