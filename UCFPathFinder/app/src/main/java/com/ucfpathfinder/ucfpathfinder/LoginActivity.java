package com.ucfpathfinder.ucfpathfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.button_login_loginActivity);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextUsername = findViewById(R.id.editText_username_loginActivity);
                EditText editTextPassword = findViewById(R.id.editText_password_loginActivity);
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                // Clear the text entered.
                editTextUsername.getText().clear();
                editTextPassword.getText().clear();

                attemptLogin(username, password);
            }
        });

        final Button registerButton = findViewById(R.id.button_register_loginActivity);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
    }

    private void attemptLogin(String username, String password){
        BackgroundWorker backgroundWorker = new BackgroundWorker(username, password, "login", this);
        backgroundWorker.execute();
    }

    private void registration()
    {
        startActivity(new Intent(this, RegisterActivity.class));
    }



}
