package com.project.tripti.ems;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private static final String GETEMployeeLIST_URL = "http://nilotpal.netai.net/t_getEmployeForm.php";
    JSONArray array = new JSONArray();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        spinner = (Spinner)findViewById(R.id.type_spinner);
        getEmployeeData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                getEmployeeData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    private void getEmployeeData() {

        String getURl = GETEMployeeLIST_URL + "?status=100";
        getURl = getURl.replace(" ", "%20");

        Log.e(" url is ", " " + getURl);

        Intent intent = getIntent();
        Map<String, String> jbForPost = new HashMap();
        JSONObject jo = new JSONObject(jbForPost);
        Log.e("JSON ", "to go" + jbForPost.toString());

        JsonObjectRequest jsonRequestPOST = new JsonObjectRequest(Request.Method.GET,
                getURl, jo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    array = response.getJSONArray("info");
                    filterData();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG)
                            .show();

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

    private void filterData() {

    int legth = array.length();


    String amount[] = new String[legth];
    String day[] = new String[legth];
    String month[] = new String[legth];
    String year[] = new String[legth];
    String emp_email[] = new String[legth];




        for(int i = 0; i<array.length();i++)
    {

        try {
            JSONObject jb = (JSONObject) array.getJSONObject(i);


        amount[i] = jb.getString("amount");
        String receivedDate = jb.getString("expenseDate");

        String Day = receivedDate.substring(0, 2);
        String Month = receivedDate.substring(4, 5);
        String yr = receivedDate.substring(6,10 );
            Log.d("yr",receivedDate);


            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        day[i] = Day.toString();
        month[i] = months[Integer.parseInt(Month.toString()) - 1];

           // year[i] = year[Integer.parseInt(yr.toString())];

            year[i] = yr.toString();
            emp_email[i] = jb.getString("emp_email");



        } catch (Exception e) {

        }


    }
        setDataOnGraph(amount,day,month,year,emp_email);

    }


    void setDataOnGraph(String []amount,String []day,String []month,String []year,String []email){

        BarChart chart = (BarChart) findViewById(R.id.chart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        String selectedType = spinner.getSelectedItem().toString();
        Log.d("type",selectedType);
        Log.d("amount", Arrays.toString(amount));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userNameRole =  pref.getString("role", null);
        String username =  pref.getString("username", null);
        Log.d("username",username);
        Log.d("userNameRole",userNameRole);

        Log.d("email", Arrays.toString(email));




        if(userNameRole.equals("Admin")){
            if (selectedType.equals("Weekly")){

                for (int i = 0;i< amount.length;i++){


                    Float amt = Float.parseFloat(amount[i]);
                    entries.add(new BarEntry(amt, i));
                    labels.add(day[i]);

                }

            }else if(selectedType.equals("Monthly"))
            {
                for (int i = 0;i< amount.length;i++){
                    Float amt = Float.parseFloat(amount[i]);
                    entries.add(new BarEntry(amt, i));
                    labels.add(month[i]);

                }

            }else{
                for (int i = 0;i< amount.length;i++){
                    Float amt = Float.parseFloat(amount[i]);
                    entries.add(new BarEntry(amt, i));
                    labels.add(year[i]);

                }
            }
        }else if(userNameRole.equals("Employee")){

            if (selectedType.equals("Weekly")){
                for (int i = 0;i< amount.length;i++){
                    String emailIs = email[i];
                    if (emailIs.equals(username)) {
                        Float amt = Float.parseFloat(amount[i]);
                        entries.add(new BarEntry(amt, i));
                        labels.add(day[i]);
                    }

                }

            }else if(selectedType.equals("Monthly"))
            {
                for (int i = 0;i< amount.length;i++){
                    String emailIs = email[i];
                    if (emailIs.equals(username)) {
                        Float amt = Float.parseFloat(amount[i]);
                        entries.add(new BarEntry(amt, i));
                        labels.add(month[i]);
                    }

                }

            }else{
                for (int i = 0;i< amount.length;i++){
                    String emailIs = email[i];
                    if (emailIs.equals(username)) {
                        Float amt = Float.parseFloat(amount[i]);
                        entries.add(new BarEntry(amt, i));
                        labels.add(year[i]);
                    }

                }
            }
        }





        BarDataSet dataset = new BarDataSet(entries, "#Expense Report");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);


        BarData data = new BarData(labels, dataset);
        chart.setData(data);

        chart.setDescription("Expense Report");
        chart.setDescription("# Nice One");
        chart.animateXY(2000, 2000);
       // chart.saveToGallery("mychart.jpg", 85);
        chart.invalidate();
    }

}




