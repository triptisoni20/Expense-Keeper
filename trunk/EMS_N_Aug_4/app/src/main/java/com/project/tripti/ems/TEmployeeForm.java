package com.project.tripti.ems;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class TEmployeeForm extends AppCompatActivity  {

    private EditText fromDateEtxt;
    private EditText editTextAmount;
    private EditText editTextExpDes;
    private Spinner spinnerManager;
    private Spinner spinnerCategory;
    private Spinner spCurrency;
    Set<Currency> availableCurrenciesSet;
    List<Currency> availableCurrenciesList;


    private Button buttonSubmit;

    private Uri.Builder builder;

    int  formId = -100;

    private static final String GETAllAllEMPLOYEEDATA_URL = "http://nilotpal.netai.net/getAllUsers.php";


    private static final String REGISTER_URL = "http://nilotpal.netai.net/t_employeeForm.php";
    private static final String UPDATE_URL = "http://nilotpal.netai.net/t_EditEmployeeForm.php";

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    JSONArray a;
    ArrayList<String> currencys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temployee_form);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        Intent intentp = getIntent();

        if (intentp.getStringExtra("toEdit").equals("Yes")){
            setDataForEdit();
        }

        setDateTimeField();
    }

    public static Set<Currency> getAllCurrencies()
    {
        Set<Currency> toret = new HashSet<Currency>();
        Locale[] locs = Locale.getAvailableLocales();

        for(Locale loc : locs) {
            try {
                toret.add( Currency.getInstance( loc ) );
            } catch(Exception exc)
            {
                // Locale not found
            }
        }

        return toret;
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.editTextdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        editTextExpDes = (EditText) findViewById(R.id.editTextExpDes);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        spinnerManager = (Spinner) findViewById(R.id.spRoles);
        spinnerCategory =  (Spinner) findViewById(R.id.spinnerCategory);
        spCurrency =  (Spinner) findViewById(R.id.spCurrency);


        //available from API Level 19
        availableCurrenciesSet = getAllCurrencies();


        availableCurrenciesList = new ArrayList<Currency>(availableCurrenciesSet);


        currencys = new ArrayList<String>();
        for(Currency loc:availableCurrenciesList){
            try {
                String val=loc.getCurrencyCode();
                if(!currencys.contains(val))
                    currencys.add(val);
            } catch(Exception exc)
            {
                // Locale not found
            }

        }
        Collections.sort(currencys);




        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, currencys);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurrency.setAdapter(adp);





        //buttonFormHistory = (Button) findViewById(R.id.buttonFormHistory);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmitForm);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentp = getIntent();

                if (intentp.getStringExtra("toEdit").equals("Yes")){
                    submitEmployeeForm(UPDATE_URL);
                }else {
                    submitEmployeeForm(REGISTER_URL);
                }
            }
        });
        setDataForManagerSpinner();
    }

    void   setDataForManagerSpinner(){

        JsonObjectRequest jsonRequestPOST = new JsonObjectRequest(Request.Method.GET,
                GETAllAllEMPLOYEEDATA_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                     a = response.getJSONArray("info");
                     ArrayList<String> stringArrayList = new ArrayList<String>();


                    for (int i = 0; i < a.length(); i++) {

                        JSONObject jb = (JSONObject) a.getJSONObject(i);

                        if (jb.getString("role").toString().equals("Manager")){
                            stringArrayList.add(jb.getString("username").toString()); //add to arraylist

                        }

                    }

                    spinnerManager = (Spinner) findViewById(R.id.spRoles);

                    String [] roles = stringArrayList.toArray(new String[stringArrayList.size()]);

                    spinnerManager
                            .setAdapter(new ArrayAdapter<String>(TEmployeeForm.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    stringArrayList));
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,roles);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinnerManager.setAdapter(adapter);

                } catch (JSONException e) {
                   e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            e.getMessage(), Toast.LENGTH_LONG)
//                            .show();

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

    void setDataForEdit(){

        try {
            JSONObject jb = new JSONObject(getIntent().getStringExtra("EMPLOYEE"));
            Toast.makeText(getApplicationContext().getApplicationContext(), "Data: " + jb.toString(), Toast.LENGTH_SHORT).show();
            buttonSubmit.setText("Update");
            fromDateEtxt.setText(jb.getString("expenseDate"));
            editTextExpDes.setText(jb.getString("expense_discription"));
            editTextAmount.setText(jb.getString("amount"));
            formId  = jb.getInt("formId");
            String curr = jb.getString("currencyType");
            Log.d("curr",jb.getString("currencyType"));




            Context context=getApplicationContext();
           // String[] manager_array = context.getResources().getStringArray(R.array.selectM_list);
           // int indexM = Arrays.asList(manager_array).indexOf(jb.getString("manager").toString());

            ArrayList<String> stringArrayList = new ArrayList<String>();


            for (int i = 0; i < a.length(); i++) {

                JSONObject jbNew = (JSONObject) a.getJSONObject(i);

                if (jbNew.getString("role").toString().equals("Manager")){
                    stringArrayList.add(jbNew.getString("username").toString()); //add to arraylist

                }

            }

            String [] manager_array = stringArrayList.toArray(new String[stringArrayList.size()]);

            int i = 0;
            for (String s : manager_array) {
                if (s.equals(jb.getString("manager").toString().trim())){
                    spinnerManager.setSelection(i);
                    break;
                }

                i++;
            }


            String[] category_array = context.getResources().getStringArray(R.array.category_list);


            int j = 0;
            for (String s : category_array) {
                if (s.equals(jb.getString("category").toString().trim())){
                    spinnerCategory.setSelection(j);
                    break;
                }

                j++;
            }

           // Collections.sort(currencys);

            String [] curr_array = currencys.toArray(new String[currencys.size()]);
            Log.v("this is my array", "arr: " + Arrays.toString(curr_array));

            int p = 0;
            for (String s : curr_array) {
                if (s.equals(curr.toString().trim())){
                    spCurrency.setSelection(p);
                    break;
                }

                p++;
            }



            int status = jb.getInt("status");

         //jb.getInt("formId")

        String stat;
        if (status == 2) {
            stat = "Denied";
        } else if (status == 1) {
            stat = "Approved";
        } else {
            stat = "Waiting";
        }
      }catch (Exception e){

        }
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    fromDatePickerDialog.show();
                }
            }
            
        });
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDatePickerDialog.getDatePicker().setMaxDate(newDate.getTimeInMillis());
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());


    }

    private void  submitEmployeeForm(String url){

        Intent intent= getIntent();



        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userName =  pref.getString("username", null);

        String restUrl = url+"?expense_discription="+editTextExpDes.getText().toString()
                +"&amount="+editTextAmount.getText().toString()
                +"&manager="+spinnerManager.getSelectedItem().toString()
                +"&cdate="+fromDateEtxt.getText().toString()
                +"&category="+spinnerCategory.getSelectedItem().toString()
                +"&emp_email="+userName
                +"&formId="+formId
                +"&currencyType="+spCurrency.getSelectedItem().toString()
                ;
Log.d("%@",restUrl);
        Toast.makeText(getApplicationContext(),
                restUrl.toString() , Toast.LENGTH_LONG)
                .show();
        restUrl = restUrl.replace(" ","%20");


try {

    RequestQueue queue = Volley.newRequestQueue(this);  // this = context

// prepare the Request
    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, restUrl, null,
            new Response.Listener<JSONObject>()
            {

                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getApplicationContext(),
                            "Form submitted"+response , Toast.LENGTH_LONG)
                            .show();
                    sendPushToManager();
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
}
    catch(Exception e ){
        // TODO Auto-generated catch block
        e.printStackTrace();
    };


    }

    private  void sendPushToManager(){

        ArrayList<String> stringArrayList = new ArrayList<String>();

        try {
            for (int i = 0; i < a.length(); i++) {

                JSONObject jbNew = (JSONObject) a.getJSONObject(i);

                if (jbNew.getString("username").toString().equals(spinnerManager.getSelectedItem().toString())) {
                    stringArrayList.add(jbNew.getString("firebaseid").toString()); //add to arraylist

                }

            }

        }catch (Exception e){

        }


        String [] manager_array = stringArrayList.toArray(new String[stringArrayList.size()]);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userName =  pref.getString("username", null);


        String restUrl = "http://nilotpal.netai.net/send.php"+"?id="+manager_array[0]
                +"&title= New Expense Form submitted By "+userName
                +"&body= To Manager "+spinnerManager.getSelectedItem().toString()
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
