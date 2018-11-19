package com.ucfpathfinder.ucfpathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button registerButton = findViewById(R.id.button_register_registerActivity);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextFirstName = findViewById(R.id.editText_firstName_registerActivity);
                EditText editTextLastName = findViewById(R.id.editText_lastName_registerActivity);
                EditText editTextUsername = findViewById(R.id.editText_username_registerActivity);
                EditText editTextPassword = findViewById(R.id.editText_password_registerActivity);
                EditText editTextRetypePassword = findViewById(R.id.editText_retypePassword_registerActivity);

                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String retypePassword = editTextRetypePassword.getText().toString();

                boolean result = requestUserAccount(firstName, lastName, username, password, retypePassword);
                if(result)
                {
                    // Clear the text entered.
                    editTextFirstName.getText().clear();;
                    editTextUsername.getText().clear();
                    editTextPassword.getText().clear();
                    editTextPassword.getText().clear();
                    editTextRetypePassword.getText().clear();
                }

            }
        });

        final Button cancelButton = findViewById(R.id.button_cancel_registerActivity);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean requestUserAccount(String firstName, String lastName, String username, String password, String retypePassword) {
        if(!(password.equals(retypePassword)))
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return false;
        }

        String login_url = "http://192.168.37.5/api/register.php";
        try {
            URL url = new URL(login_url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String postData = URLEncoder.encode("first_name_key", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8") + "&" +
                    URLEncoder.encode("last_name_key", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8") + "&" +
                    URLEncoder.encode("username_key", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                    URLEncoder.encode("password_key", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += (line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();


            Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


}
