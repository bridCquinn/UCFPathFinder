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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class AddDeleteWorker implements AddDeleteRunnable {

    private Course course;
    private Context context;
    private String action;

    public AddDeleteWorker(String action){
        setAction(action);
    }

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
        if(getAction().equals("add"))
        {
            try
            {
                // 1. submit the new course.
                addCourse();
                // 2. delete local database and update with new.
                updateCourseDatabase();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(getAction().equals("edit"))
        {

        }

        if(getAction().equals("delete"))
        {
            deleteCourse();
            try {
                updateCourseDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void addCourse() throws Exception
    {
        URL url = new URL("http://ucfpathfinder.com/API/MakeSchedule.php");

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();

        JSONObject jsonOutput = createAddJsonOutput("add");

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

    private void updateCourseDatabase() throws Exception
    {
        URL url = new URL("http://ucfpathfinder.com/API/GetSchedule.php");

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();

        JSONObject jsonOutput = createAddJsonOutput("update");

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
        StringBuilder resultOfGetSchedule = new StringBuilder();
        while((lineTemp = bufferedReader.readLine())!=null)
            resultOfGetSchedule.append(lineTemp);
        //resultOfRegister = resultOfRegister + lineTemp;
        Log.d("Get schedule result",resultOfGetSchedule.toString());

        bufferedReader.close();
        inputStream.close();
        httpURLConnection.disconnect();

        CoursesDAO database = Room.databaseBuilder(getContext(), CourseDatabase.class, "Course").build().getCourseDAO();
        String noRecordsFound = "", checkRecords = resultOfGetSchedule.toString().replace("\"","");
        if(checkRecords.contains("{error:No Records Found}"))
        {
            database.nukeTable();
            return;
        }

        JSONObject userInfo = new JSONObject(resultOfGetSchedule.toString());
        JSONArray courseArray = userInfo.getJSONArray("schedule");
        database.nukeTable();
        for(int i = 0; i < courseArray.length(); i++)
        {
            Course course = new Course(0,"","","","","","","","");
            JSONArray jsonArrayTemp = courseArray.getJSONArray(i);
            course.setCourseID(jsonArrayTemp.getInt(0));
            course.setBuilding(jsonArrayTemp.getString(1));
            course.setClassName(jsonArrayTemp.getString(2));
            course.setStartTime(jsonArrayTemp.getString(3));
            course.setEndTime(jsonArrayTemp.getString(4));
            course.setClassCode(jsonArrayTemp.getString(5));
            course.setTerm(jsonArrayTemp.getString(6));
            course.setYear(jsonArrayTemp.getString(7));
            // 8 is for notes.
            course.setDay(jsonArrayTemp.getString(9));
            database.insert(course);
        }


    }

    private void deleteCourse()
    {
        try {
            // URL.
            URL url = new URL("http://ucfpathfinder.com/API/DeleteClassMobile.php");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            JSONObject jsonOutput = createAddJsonOutput("delete");

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
            Log.d("Delete Result",resultOfRegister.toString());

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        }
        catch(HttpRetryException e)
        {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONObject createAddJsonOutput(String action) throws JSONException
    {
        if(action.equals("add")) {
            //TODO This needs to accept the building ID, not he abbreviation.
            JSONObject jsonCourse = new JSONObject();
            jsonCourse.put("building", findBuildingID());
            jsonCourse.put("className", course.getClassName());
            jsonCourse.put("startTime", reformatTimeToMilitary(course.getStartTime()));
            jsonCourse.put("endTime", reformatTimeToMilitary(course.getEndTime()));
            jsonCourse.put("classCode", course.getClassCode());
            jsonCourse.put("term", course.getTerm());
            jsonCourse.put("year", course.getYear());
            jsonCourse.put("notes", "");
            jsonCourse.put("classDays", course.getDay());

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonCourse);

            JSONObject jsonOutput = new JSONObject();
            jsonOutput.put("userID", getUserID());
            jsonOutput.put("schedule", jsonArray);
            Log.d("Output ADDING", jsonOutput.toString());
            return jsonOutput;
        }
        if(action.equals("update"))
        {
            JSONObject jsonOutput = new JSONObject();
            jsonOutput.put("userID", getUserID());
            jsonOutput.put("term",course.getTerm());
            jsonOutput.put("year",course.getYear());
            return jsonOutput;
        }
        if(action.equals("delete"))
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", Integer.parseInt(getUserID()));
            jsonObject.put("classID", course.getCourseID());
            return jsonObject;
        }
        return null;
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
            if(hour != 12)
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

    public void setCourse(Course course) {
        this.course = course;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
