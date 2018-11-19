package com.ucfpathfinder.ucfpathfinder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

class BackgroundWorker extends AsyncTask<Void, Void, String> {
    //private AlertDialog alertDialog;
    private Context context;
    private String username;
    private String password;
    private String actionType;


    BackgroundWorker(String username, String password, String actionType, Context context)
    {
        this.context = context.getApplicationContext();
        this.username = username;
        this.password = password;
        this.actionType = actionType;
    }

    @Override
    protected String doInBackground(Void... params) {
        if(actionType.equals("login"))
        {
            try
            {
                //TODO Need to sanitize the login information.
                //TODO Hashing of the password.

                // URL Object.
                URL url = new URL("http://ucfpathfinder.com/API/Login.php");

                // Creation of the JSON object.
                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("password", password);

                // Connection to the server.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                //httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                httpURLConnection.connect();

                // For sending json.
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(json.toString());
                writer.flush();
                writer.close();

                // For receiving json.
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                // Response message, OK if successful.
                String loginStatus = httpURLConnection.getResponseMessage();

                // Reading the response json from server.
                String line = "";
                String result = "";
                while((line = bufferedReader.readLine())!=null)
                    result = result + line;
                Log.d("RESULT", result);

                bufferedReader.close();
                outputStream.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
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
        return null;
    }

    @Override
    protected void onPreExecute() {
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Login Result");
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(!(result.equals(""))) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
        else
        {
            Toast.makeText(context, "testing", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
