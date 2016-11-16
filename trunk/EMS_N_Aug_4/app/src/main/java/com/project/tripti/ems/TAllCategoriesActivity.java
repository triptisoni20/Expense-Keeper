package com.project.tripti.ems;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TAllCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tall_categories);
         Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(0);

            }
        });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(1);

            }
        });

        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(2);

            }
        });

        Button button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(3);

            }
        });

        Button button5 = (Button)findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(4);

            }
        });

        Button button6 = (Button)findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(5);

            }
        });


        Button button7 = (Button)findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(6);

            }
        });

        Button button8 = (Button)findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(7);

            }
        });

        Button button9 = (Button)findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(8);

            }
        });

        Button button10 = (Button)findViewById(R.id.button10);
        button10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openListForCategory(9);

            }
        });


    }
    public void openListForCategory(int index) {

        Intent intent = getIntent();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userName =  pref.getString("username", null);          // getting String
        Intent login=new Intent(getApplicationContext(), TEmployeeListActivity.class);
        login.putExtra("ActivityFrom", "AllCategory");
        String[] arrAllCat = getResources().getStringArray(R.array.category_list);
        login.putExtra("categoryIs", arrAllCat[index]);

        startActivity(login);

    }
}
