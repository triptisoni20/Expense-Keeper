package com.project.tripti.ems;

/**
 * Created by Miss_T on 27/07/2016.
 */
import android.content.Intent;
import android.location.Address;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.Attributes;

public class Register extends AppCompatActivity {
    Spinner spinner;
    String role="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText userName = (EditText)findViewById(R.id.userName_register);
        final EditText etdPassword = (EditText)findViewById(R.id.Password_register);
        final EditText edtName = (EditText)findViewById(R.id.name_register);
        final EditText edtEmail = (EditText)findViewById(R.id.email_register);

        Button register = (Button)findViewById(R.id.Register);

        spinner = (Spinner) findViewById(R.id.select_roles);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                final String username, password, name, email;
                username = userName.getText().toString();
                password = etdPassword.getText().toString();
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                role = spinner.getSelectedItem().toString();


                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            Toast.makeText(getApplicationContext(), "Testing"+response, Toast.LENGTH_SHORT).show();

                            if (success) {
                                Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                Intent load = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(load);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Register Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                String token = FirebaseInstanceId.getInstance().getToken();
                Log.i("FireBase Message", "FCM Registration Token: " + token);

                RegisterActivity register = new RegisterActivity(username, password, name, email, role,token, responseListner);

                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(register);
            }
        });
    }
}

