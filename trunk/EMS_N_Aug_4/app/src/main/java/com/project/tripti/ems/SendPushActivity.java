package com.project.tripti.ems;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Exchanger;

public class SendPushActivity extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker,btnSendPushAt;
    EditText txtDate, txtTime,txtMessage;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    DatePickerDialog datePicker;
    TimePickerDialog myTimePicker;
    String GotTime = " ";
    String GotDate = " ";
    JSONArray arr;
    Spinner type_spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_push);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnSendPushAt=(Button)findViewById(R.id.btnSendPushAt);
        type_spinner = (Spinner)findViewById(R.id.type_spinner);


        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        txtMessage=(EditText)findViewById(R.id.txtMessage);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        btnDatePicker.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View view) {

                // Get Current Date
                Calendar cal = Calendar.getInstance();
                datePicker = new DatePickerDialog(SendPushActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth ) {


                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        GotDate = dateFormatter.format(newDate.getTime()).toString();
                        txtDate.setText(GotDate);



                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_WEEK));

                datePicker.setTitle("Select Date");
                datePicker.show();




            }
        });
        btnTimePicker.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                myTimePicker = new TimePickerDialog(SendPushActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Calendar newTime = Calendar.getInstance();
                        //Add these two line
                        newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newTime.set(Calendar.MINUTE, minute);

                        GotTime =  String.valueOf(hourOfDay) + " :" + String.valueOf(minute + 1);
                        txtTime.setText(GotTime);
                    }
                }, calender.get((Calendar.HOUR_OF_DAY)), calender.get(Calendar.MINUTE), true);


                myTimePicker.setTitle("Select Time");
                myTimePicker.show();



            }

        });

        btnSendPushAt.setOnClickListener(new  View.OnClickListener(){
            public  void onClick(View view){
                sendPushToAll();
            }
        });


        getAllUser();

    }

    private  void getAllUser(){
      String url =  "http://nilotpal.netai.net/getAllUsers.php";

        try{
            RequestQueue queue = Volley.newRequestQueue(this);  // this = context
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {

                        @Override
                        public void onResponse(JSONObject response) {

                          try {
                              arr = response.getJSONArray("info");
                              Log.d("All users",arr.toString());


                          }catch (Exception e){

                          }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ;
                        }
                    }
            );
            queue.add(getRequest);

        }catch (Exception e){

        }

    }
    private  void sendPushToAll(){

        ArrayList<String> stringArrayList = new ArrayList<String>();


        String whomToSend = type_spinner.getSelectedItem().toString();

        try {
            for (int i = 0; i < arr.length(); i++) {

                JSONObject jbNew = (JSONObject) arr.getJSONObject(i);

                if (jbNew.getString("role").toString().equals(whomToSend)) {

                    stringArrayList.add(jbNew.getString("firebaseid").toString()); //add to arraylist
                }
            }

        }catch (Exception e){

        }


        String listOfFCMIds = Arrays.toString(stringArrayList.toArray());
        Log.d("list of ids",listOfFCMIds);

        listOfFCMIds = listOfFCMIds.replaceAll("\\[", "");
        listOfFCMIds = listOfFCMIds.replaceAll("\\]", "");




        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userName =  pref.getString("username", null);

        String restUrl = "http://nilotpal.netai.net/send.php"+"?id="+listOfFCMIds
                +"&title= From Admin "+userName
                +"&body= "+txtMessage.getText().toString()
                ;


        Log.d("URL",restUrl);

        Toast.makeText(getApplicationContext(),
                restUrl.toString() , Toast.LENGTH_LONG)
                .show();
        restUrl = restUrl.replace(" ","%20");


        try{
            RequestQueue queue = Volley.newRequestQueue(this);  // this = context

// prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, restUrl, null,
                    new Response.Listener<JSONObject>()
                    {

                        @Override
                        public void onResponse(JSONObject response) {

                            Toast.makeText(getApplicationContext(),
                                    "Notification send"+response , Toast.LENGTH_LONG)
                                    .show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ;
                        }
                    }
            );
            queue.add(getRequest);

        }catch (Exception e){

        }
        // http://nilotpal.netai.net/send.php?id=dPMYW8DjVmQ:APA91bFP8-bq0Aa3VA7wfC94IzY9mOoCO6SOAdonLTdS4UmG3mYVFyZSrE8qZZiVeZ9bRJuCfbx5taM1EUvLokn4ILcXO2VVfpIiF8ohI1Dg6_eLQZYiVmd86l8oLg0D5Py0osOoIEtP&title=Hey!&body=How%20are%20you

    }

}
