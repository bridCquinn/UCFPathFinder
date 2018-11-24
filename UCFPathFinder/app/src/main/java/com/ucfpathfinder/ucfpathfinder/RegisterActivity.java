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
                EditText editTextEmail = findViewById(R.id.editText_email_registerActivity);
                EditText editTextPassword = findViewById(R.id.editText_password_registerActivity);
                EditText editTextRetypePassword = findViewById(R.id.editText_retypePassword_registerActivity);

                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String retypePassword = editTextRetypePassword.getText().toString();

                boolean result = requestUserAccount(firstName, lastName, username, email, password, retypePassword);
                if(result)
                {
                    // Clear the text entered.
                    editTextFirstName.getText().clear();
                    editTextLastName.getText().clear();
                    editTextUsername.getText().clear();
                    editTextEmail.getText().clear();
                    editTextPassword.getText().clear();
                    editTextRetypePassword.getText().clear();
                    finish();
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

    private boolean requestUserAccount(String firstName, String lastName, String username, String email, String password, String retypePassword) {
        if(!(password.equals(retypePassword)))
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(email.contains("@") && email.contains(".")))
        {
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_LONG).show();
            return false;
        }

        BackgroundWorker backgroundWorker = new BackgroundWorker(username, password, firstName, lastName, email, "register", this);
        backgroundWorker.execute();
        return true;
    }


}
