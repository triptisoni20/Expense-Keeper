package com.project.tripti.ems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText edtusrName = (EditText)findViewById(R.id.userName);
        final EditText edtpassword = (EditText)findViewById(R.id.Password);
        final Button login = (Button)findViewById(R.id.sign_in);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String username = edtusrName.getText().toString();
                final String password = edtpassword.getText().toString();

                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                String name = jsonResponse.getString("name");
                                String role = jsonResponse.getString("role");
                                String username = jsonResponse.getString("username");

                                Intent load=new Intent(getApplicationContext(), UserArea.class);
                                load.putExtra("name", name);
                                load.putExtra("role", role);
                                startActivity(load);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                String token = FirebaseInstanceId.getInstance().getToken();
                Log.i("Token is: ", "FCM Registration Token: " + token);

                LoginActivity login = new LoginActivity(username, password,token, responseListner);

                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(login);
            }
        });
    }
}
