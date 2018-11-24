package com.ucfpathfinder.ucfpathfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class BackgroundWorker extends AsyncTask<Void, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String username;
    private String email;
    private String preHashPassword;
    private String actionType;
    private String firstName;
    private String lastName;
    private boolean requestResult;
    private boolean recordsFound;

    // User information to be stored.
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditior;


    BackgroundWorker(String username, String password, String actionType, Context context)
    {
        // Construction for Login.
        setContext(context.getApplicationContext());
        setUsername(username);
        setPreHashPassword(password);
        setActionType(actionType);
        setRequestResult(false);
    }

    BackgroundWorker(String username, String password, String firstName, String lastName, String email,String actionType, Context context)
    {
        // Constructor for Registration.
        setContext(context.getApplicationContext());
        setUsername(username);
        setPreHashPassword(password);
        setActionType(actionType);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setRequestResult(false);
        setRecordsFound(false);
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result)
    {
        if(getActionType().equals("login")) {
            if (getRequestResult() && isRecordsFound()) {
                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();

                // Store the user information.
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                spEditior = sharedPreferences.edit();
                spEditior.putString("userID", result);
                spEditior.putString("username", getUsername());
                spEditior.putString("password", getPreHashPassword());
                spEditior.commit();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("loginResultJson", result);
                getContext().startActivity(intent);
            }
            else if(getRequestResult() && !(isRecordsFound()))
                Toast.makeText(getContext(), "No Records Found", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), "Login Unsuccessful", Toast.LENGTH_LONG).show();
        }
        else if (getActionType().equals("register"))
        {
            if(getRequestResult())
                Toast.makeText(getContext(),"Register Successful",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(),"Registration Unsuccessful", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(Void... params) {
        if(getActionType().equals("login"))
        {
            try
            {
                //TODO Need to sanitize the login information.


                // Target URL.
                URL url = getURL();
                if(url == null)
                    return null;

                // Json to be send to server.
                JSONObject jsonOutput = constructJsonForLogin();
                if(jsonOutput == null)
                    return null;

                // Connection to the URL.
                HttpURLConnection httpURLConnection = getHTTPURLConnection(url);
                if(httpURLConnection == null)
                    return null;

                // Send JSON to the server.
                outputJson(httpURLConnection, jsonOutput);

                // Receive JSON from the server.
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                // Reading the response json from server.
                String lineTemp;
                StringBuilder resultOfLogin = new StringBuilder();
                while((lineTemp = bufferedReader.readLine())!=null)
                    resultOfLogin.append(lineTemp);
                Log.d("RESULT OF LOGIN", resultOfLogin.toString());

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                // Parsing string to JSON.
                JSONObject returnJSON = new JSONObject(resultOfLogin.toString());

                // Check if error occurred on server side.
                String errorCheckFromServer = returnJSON.getString("error");
                // No error == "".
                if((errorCheckFromServer.equals(""))) {
                    setRequestResult(true);
                    setRecordsFound(true);
                    // Return the ID for storing in shared preferences.
                    return returnJSON.getString("userID");
                }
                else if(errorCheckFromServer.equals("No Records Found"))
                    setRequestResult(true);
                else
                    return null;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else if (getActionType().equals("register"))
        {
            try {
                // Target URL.
                URL url = getURL();
                if(url == null)
                    return null;

                // JSON to be sent.
                JSONObject jsonOutput = constructJsonForRegistration();
                if(jsonOutput == null)
                    return null;

                // Connection to the URL.
                HttpURLConnection httpURLConnection = getHTTPURLConnection(url);
                if(httpURLConnection == null)
                    return null;

                // Send JSON to server.
                outputJson(httpURLConnection,jsonOutput);

                // JSON received from server.
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String lineTemp;
                StringBuilder resultOfRegister = new StringBuilder();
                while((lineTemp = bufferedReader.readLine())!=null)
                    resultOfRegister.append(lineTemp);
                    //resultOfRegister = resultOfRegister + lineTemp;
                Log.d("Register Result",resultOfRegister.toString());

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                // If null is returned, no error.
                if(resultOfRegister.toString().equals(""))
                    setRequestResult(true);

                return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Only if error.
        return null;
    }

    private void outputJson(HttpURLConnection httpURLConnection, JSONObject jsonOutput)
    {
        try {
            // Output to the server.
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonOutput.toString());
            writer.flush();
            writer.close();
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private JSONObject constructJsonForLogin()
    {
        try
        {
            JSONObject json = new JSONObject();
            json.put("username", getUsername());
            json.put("password", passwordHashMD5(getPreHashPassword()));
            return json;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject constructJsonForRegistration()
    {
        try
        {
            JSONObject json = new JSONObject();
            json.put("firstName",getFirstName());
            json.put("lastName",getLastName());
            json.put("username",getUsername());
            json.put("password",passwordHashMD5(getPreHashPassword()));
            json.put("email",getEmail());
            return json;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private URL getURL()
    {
        try {
            if (getActionType().equals("login"))
                return new URL("http://ucfpathfinder.com/API/Login.php");
            if (getActionType().equals("register"))
                return new URL("http://ucfpathfinder.com/API/Register.php");
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String passwordHashMD5(String password)
    {
        // TODO Sanitize the password before hashing.
        // Put try catch below in a if else statement.
        // Condition would be boolean that check the password for sanitation.

        // Tutorial from https://mobikul.com/converting-string-md5-hashes-android/
        try
        {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexOfPassword = new StringBuilder();
            for(int i = 0; i < messageDigest.length; i++)
                hexOfPassword.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexOfPassword.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection getHTTPURLConnection(URL url)
    {
        // Connection to the server.
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return httpURLConnection;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String getPreHashPassword() {
        return preHashPassword;
    }

    private void setPreHashPassword(String preHashPassword) {
        this.preHashPassword = preHashPassword;
    }

    private String getActionType() {
        return actionType;
    }

    private void setActionType(String actionType) {
        this.actionType = actionType;
    }

    private void setRequestResult(boolean bool)
    {
        this.requestResult = bool;
    }

    private boolean getRequestResult()
    {
        return this.requestResult;
    }

    public boolean isRecordsFound() {
        return recordsFound;
    }

    public void setRecordsFound(boolean recordsFound) {
        this.recordsFound = recordsFound;
    }
}
