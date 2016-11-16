package com.project.tripti.ems;

/**
 * Created by Miss_T on 27/07/2016.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = (Button) findViewById(R.id.login);
        TextView register = (TextView) findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), Login.class);
                startActivity(login);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent register = new Intent(getApplicationContext(), Register.class);
                    startActivity(register);
                    finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

