package com.project.tripti.ems;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TEmployeeDataActivity extends AppCompatActivity {

    private TextView txtExpence;
    private TextView txtAmount;
    private TextView txtManger;
    private TextView txtDate;
    private  EditText edittxtDes;

    private TextView txtStatus;
    private TextView txtFormId;



    private Button buttonApprove;
    private Button buttonDeny;
    int formId;

    JSONObject jsonObj;
    JSONArray arr;

    private static final String UPDATE_STATUS_URL = "http://nilotpal.netai.net/t_updateEmployeeForm.php";
    private static final String GETFBID_URL = "http://nilotpal.netai.net/getFireBaseIdFromFormId.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temployee_data);

        findViewsById();
    }
    private void findViewsById() {
        try {
             jsonObj = new JSONObject(getIntent().getStringExtra("EMPLOYEE"));

            Toast.makeText(getApplicationContext(),
                    jsonObj.toString() , Toast.LENGTH_LONG)
                                .show();

            txtExpence = (TextView) findViewById(R.id.txtExpence);
            txtManger = (TextView) findViewById(R.id.txtManager);
            txtAmount = (TextView) findViewById(R.id.txtAmount);
            txtDate = (TextView) findViewById(R.id.txtDate);
            edittxtDes = (EditText) findViewById(R.id.edittxtDes);

            //    txtStatus =  (TextView) findViewById(R.id.txtStatus);
            txtFormId =  (TextView) findViewById(R.id.txtFormId);


            txtExpence.setText("Expense discription :    " + jsonObj.getString("expense_discription"));
            txtManger.setText("Amount:    " + jsonObj.getString("amount") + " GBP");
            txtAmount.setText("Manager:   "+ jsonObj.getString("manager"));
            txtDate.setText("Expense Date:    " + jsonObj.getString("expenseDate"));

           // txtStatus.setText("Status: "+ jsonObj.getInt("status"));
            Log.d("myTag", jsonObj.getString("currencyType"));

            setCurrencyToPound(jsonObj.getString("amount"),jsonObj.getString("currencyType"),"GBP");

            txtFormId.setText("FormId:   " + jsonObj.getInt("formId"));

            formId = jsonObj.getInt("formId");

            buttonApprove = (Button) findViewById(R.id.buttonApprove);
            buttonDeny = (Button) findViewById(R.id.buttonDeny);

            buttonApprove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    updateStatusForEmployeeForm(2,formId);
                }
            });
            buttonDeny.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    updateStatusForEmployeeForm(1,formId);
                }
            });

        }catch (Exception e){

        }
    }
    private void setCurrencyToPound(String amount,String from,String to){
        //   http://rate-exchange-1.appspot.com/currency?from=INR&to=USD
        String getURl = "http://rate-exchange-1.appspot.com/currency?from="+from
                +"&to="+to;
     //   getURl = getURl.replace(" ","%20");
        Log.d("url",getURl);

        RequestQueue queue = Volley.newRequestQueue(this);

        final int value=Integer.parseInt(amount);

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getURl, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                                               try {
                                                 //  int rate=Integer.parseInt(response.getString("rate"));
                                                   float rate = response.getInt("rate");
                                                   Log.d("rate",Float.toString(rate) );

                                                   float GBPAmt = rate * value;
                                                   Log.d("GBPAmt",Float.toString(GBPAmt) );

                                                   txtManger.setText("Amount:    " + Float.toString(GBPAmt) + " GBP");


                                                   Toast.makeText(getApplicationContext(),
                                                           Float.toString(GBPAmt), Toast.LENGTH_LONG)
                                    .show();
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Log.d("Error.Response",error.getMessage());
//                        Toast.makeText(getApplicationContext(),
//                                error.getMessage() , Toast.LENGTH_LONG)
//                                .show();
                    }
                }
        );
        queue.add(getRequest);
    }

    private void  updateStatusForEmployeeForm(int status,int formId){

        final int S = status;
        String getURl = UPDATE_STATUS_URL +"?status="+status
                +"&reasonForDeny="+edittxtDes.getText().toString()
                +"&formId="+formId;
        getURl = getURl.replace(" ","%20");

        RequestQueue queue = Volley.newRequestQueue(this);


// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getURl, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(),
                                "status Updated" , Toast.LENGTH_LONG)
                                .show();
                        String stat;

                        if (S == 2) {
                            stat = "Denied";
                        } else if (S == 1) {
                            stat = "Approved";
                        } else {
                            stat = "Waiting";
                        }
                        setgetFBID(stat);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Log.d("Error.Response",error.getMessage());
//                        Toast.makeText(getApplicationContext(),
//                                error.getMessage() , Toast.LENGTH_LONG)
//                                .show();
                    }
                }
        );
        queue.add(getRequest);

    }

    void   setgetFBID(final String status){


        String getURl = GETFBID_URL +"?fid="+formId;

        getURl = getURl.replace(" ","%20");
        JsonObjectRequest jsonRequestPOST = new JsonObjectRequest(Request.Method.GET,
                getURl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    arr = response.getJSONArray("info");
                    JSONObject jb = (JSONObject) arr.getJSONObject(0);

                    String fbid = jb.getString("firebaseid");

                    sendPushToEmployee(fbid,status);


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("JSON Parser", "Error parsing data " + error.toString());
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonRequestPOST);

    }
    private  void sendPushToEmployee(String fbid,String status){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userName =  pref.getString("username", null);


        String restUrl = "http://nilotpal.netai.net/send.php"+"?id="+fbid
                +"&title= Form of  "+userName
                +"&body= To Manager " + status
                ;

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
                                    "Notification send by Manager"+response , Toast.LENGTH_LONG)
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
