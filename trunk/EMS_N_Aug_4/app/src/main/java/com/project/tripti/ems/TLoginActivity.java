package com.project.tripti.ems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TLoginActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://simplifiedcoding.16mb.com/UserRegistration/volleyRegister.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERROLE = "userrole";

    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Spinner  spinnerRole;
//date
    private Button buttonRegister;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlogin);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        spinnerRole = (Spinner) findViewById(R.id.spRoles);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSignUp();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openEmployeeForm();
            }
        });
    }

    private void openSignUp(){
        Intent intent = new Intent(this, TSignupActivity.class);
        Intent i=new Intent(getApplicationContext(),TSignupActivity.class);
        startActivity(i);

    }
    private void openEmployeeForm(){

        if(editTextUsername.getText() != null || !editTextUsername.getText().equals(""))
        {
            String role = spinnerRole.getSelectedItem().toString();

            if (role.equalsIgnoreCase("Employee")){
                Intent intent = new Intent(getApplicationContext(), TEmployeeForm.class);
                intent.putExtra(KEY_USERROLE, role);
                intent.putExtra(KEY_USERNAME, editTextUsername.getText());
                startActivity(intent);

            }else if(role.equalsIgnoreCase("Manager")){
                Intent intent = new Intent(getApplicationContext(), TEmployeeListActivity.class);
                intent.putExtra(KEY_USERROLE, role);
                intent.putExtra(KEY_USERNAME, editTextUsername.getText());
                startActivity(intent);

            }else if(role.equalsIgnoreCase("Admin")) {
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "You are Admin!!!" , Toast.LENGTH_LONG)
                        .show();
            }
        }else {

        Toast.makeText(getApplicationContext(),"Email-id is mandatory",Toast.LENGTH_LONG).show();
            return;
        }



    }
}
