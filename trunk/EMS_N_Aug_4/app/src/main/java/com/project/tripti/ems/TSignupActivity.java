package com.project.tripti.ems;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TSignupActivity extends AppCompatActivity  {



    private static final String REGISTER_URL = "http://simplifiedcoding.16mb.com/UserRegistration/volleyRegister.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button buttonRegister;
    private Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsignup);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String city = editTextUsername.getText().toString().trim().toLowerCase();
            String cdate = editTextPassword.getText().toString().trim().toLowerCase();
            String imageurl = editTextEmail.getText().toString().trim().toLowerCase();

            }
        });

    }

}
