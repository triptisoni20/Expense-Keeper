package com.project.tripti.ems;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TEmployeeListActivity extends AppCompatActivity {

    public static final String KEY_EMPLOYEESELECTED = "EMPLOYEE";
    public static final String KEY_EMPLOYEEPOSITION = "POSITION";

    private static final String GETEMployeeLIST_URL = "http://nilotpal.netai.net/t_getEmployeForm.php";
    private static final String CATEGORIES_WISE_DATA_URL = "http://nilotpal.netai.net/t_getEmployeDataByCategory.php";

    ListView listView;

    String temp;
    JSONArray array=new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temployee_list);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        Intent intent = getIntent();




        final String activityFrom = intent.getStringExtra("ActivityFrom");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userName = pref.getString("username", null);


        if (activityFrom.equals("AllCategory")) {
            final String categoryIs = intent.getStringExtra("categoryIs");
            String getURl = CATEGORIES_WISE_DATA_URL + "?category=" + categoryIs
                    + "&emp_email=" + userName;
            getURl = getURl.replace(" ", "%20");
            Toast.makeText(getApplicationContext(),
                    getURl, Toast.LENGTH_LONG)
                    .show();
            callgetEmployeeList(getURl);
        } else {
            String status = intent.getStringExtra("forStatus");

            if (status.equals("All")) {
                String getURl = GETEMployeeLIST_URL + "?status=100";
                getURl = getURl.replace(" ", "%20");
                callgetEmployeeList(getURl);
            } else {
                String getURl = GETEMployeeLIST_URL + "?status=0";
                getURl = getURl.replace(" ", "%20");
                callgetEmployeeList(getURl);
            }
        }
    }


    private void callgetEmployeeList(final String strURL) {

        Log.e("bebo url is ", " " + strURL);

        Intent intent = getIntent();
        Map<String, String> jbForPost = new HashMap();
        JSONObject jo = new JSONObject(jbForPost);
        Log.e("JSON ", "to go" + jbForPost.toString());

        JsonObjectRequest jsonRequestPOST = new JsonObjectRequest(Request.Method.GET,
                strURL, jo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray a = response.getJSONArray("info");
                    array=a;
                    setDataOnListView(a);

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

    void setDataOnListView(final JSONArray arr) {

        //String[] arrAllFormData = new String[arr.length()];
        int legth = arr.length();

        /*ArrayList<String> status_in_words = new ArrayList<>();
        ArrayList<String> amount = new ArrayList<>();
        ArrayList<String> category = new ArrayList<>();
        ArrayList<String> day = new ArrayList<>();
        ArrayList<String> month = new ArrayList<>();*/

        String status_in_words[] = new String[legth];
        String amount[] = new String[legth];
        String category[] = new String[legth];
        String day[] = new String[legth];
        String month[] = new String[legth];
        String year[] = new String[legth];

        String manager[] = new String[legth];
        int formIds[] = new int[legth];
        String expD[] = new String[legth];
        String currency[] = new String[legth];

        try {

            for (int i = 0; i < arr.length(); i++) {
                JSONObject jb = (JSONObject) arr.getJSONObject(i);
                int status = jb.getInt("status");

                String stat;

                if (status == 2) {
                    stat = "Denied";
                } else if (status == 1) {
                    stat = "Approved";
                } else {
                    stat = "Waiting";
                }

                //arrAllFormData[i] =  "Form: " + jb.getInt("formId") +" status:  " +stat + " Date: " + jb.getString("expenseDate") +   " Category: " + jb.getString("category");            //
                status_in_words[i] = stat;
                category[i] = jb.getString("category");
                amount[i] = jb.getString("amount");
                expD[i] = jb.getString("expense_discription");
                String receivedDate = jb.getString("expenseDate");

                String Day = receivedDate.substring(0, 2);
                String Month = receivedDate.substring(4, 5);
                String yr = receivedDate.substring(7, 8);

                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

                day[i] = Day;
                month[i] = months[Integer.parseInt(Month.toString())];
                year[i] = year[Integer.parseInt(yr.toString())];

                formIds[i] = jb.getInt("formId");
                manager[i] = jb.getString("manager");
                category[i] = jb.getString("category");
                 currency[i] = jb.getString("currencyType");


            }

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrAllFormData);
        // Assign adapter to ListView
        listView.setAdapter(adapter);*/


            listView.setAdapter(new ViewAdapter(this, legth, day, month, category, amount, status_in_words,manager,formIds,year,expD,currency));

            // ListView Item Click Listener
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SingleRow {

    String date;
    String month;
    String amount;
    String status;
    String manager;
    String category;
    String year;
    String expense_discription;
    String currencyT;

    int formId;



    SingleRow(String date, String month, String category, String amount, String status,String manager,int formId,String year,String expense_discription,String currency){
        this.date = date;
        this.month =month;
        this.year = year;
        this.manager = manager;
        this.category = category;
        this.amount = amount;
        this.status = status;
        this.expense_discription = expense_discription;
        this.formId = formId;
        this.currencyT = currency;


    }
}


class ViewAdapter extends BaseAdapter {

    Context context;
    int len;
    ArrayList<SingleRow> data;

    String[] date;
    String[] month;
    String[] category;
    String[] amount;
    String[] status;
    int[] formids;

    String[] manager;
    String year[];
    String []expense_discription;
    String []currency;

    LayoutInflater inflater=null;

    ViewAdapter(TEmployeeListActivity context, int len, String[] date, String[] month, String[] category, String[] amount, String[] status,String[] manager,int[] formIds,String[] year,String expDes[],String currency[]) {
        this.len = len;
        this.context = context;
        this.date = date;
        this.month = month;
        this.category = category;
        this.amount = amount;
        this.status = status;
        this.manager = manager;
        this.year = year;
        this.formids = formIds;
        this.expense_discription = expDes;
        this.currency = currency;

        data = new ArrayList<>();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < len; i++) {
            data.add(new SingleRow(date[i], month[i], category[i], amount[i], status[i],manager[i],formIds[i],year[i],expDes[i],currency[i]));
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //Holder holder=new Holder();
        View inflateRow;
        inflateRow = inflater.inflate(R.layout.list_view, null);

        try {
            TextView date = (TextView) inflateRow.findViewById(R.id.date);
            TextView month = (TextView) inflateRow.findViewById(R.id.month);
            TextView category = (TextView) inflateRow.findViewById(R.id.category);
            TextView amount = (TextView) inflateRow.findViewById(R.id.amount);
            TextView status = (TextView) inflateRow.findViewById(R.id.status);

            ImageButton edit = (ImageButton) inflateRow.findViewById(R.id.edit);

            LinearLayout colourChanger = (LinearLayout)inflateRow.findViewById(R.id.layout_colour);

            final SingleRow tempData = data.get(position);

            final String strDate = tempData.date;
            final String strMonth = tempData.month;
            final String strCategory = tempData.category;
            final String strAmount = tempData.amount;
            final String strStatus = tempData.status;
            final int fid = tempData.formId;
            final String strmanager = tempData.manager;
            final String strexpDes = tempData.expense_discription;
            final String currency = tempData.currencyT;

            if (strStatus.equals("Denied")) {
                edit.setBackgroundResource(R.drawable.shape_red);
                colourChanger.setBackgroundResource(R.drawable.bg_red);
            } else if (strStatus.equals("Approved")) {
                edit.setBackgroundResource(R.drawable.shape_green);
                colourChanger.setBackgroundResource(R.drawable.bg_green);
            } else {
                edit.setBackgroundResource(R.drawable.shape_yellow);
                colourChanger.setBackgroundResource(R.drawable.bg_yellow);
            }


            date.setText(strDate);
            month.setText(strMonth);
            category.setText("Category: "+strCategory);
            amount.setText("Amount: "+strAmount);
            status.setText("Status: "+strStatus);


            edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    Intent intent = ((Activity) context).getIntent();
                    final String activityFrom = intent.getStringExtra("ActivityFrom");

                    if (activityFrom.equals("Manager")){
                        final String forStatus = intent.getStringExtra("forStatus");
//                        if (strStatus.equals("Denied")) {
//                            return;
//                        }else if(strStatus.equals("Approved")){
//                            return;
//                        }
                        // ListView Clicked item index
                        Intent intentnew = new Intent(context.getApplicationContext(), TEmployeeDataActivity.class);
                        JSONObject eachDetail = new JSONObject();

                        try {
                            eachDetail.put("expenseDate", strDate +"-"+ strMonth);
                            eachDetail.put("expense_discription", strexpDes);
                            eachDetail.put("amount", strAmount);
                            eachDetail.put("manager", strmanager);
                            eachDetail.put("category", strCategory);
                            eachDetail.put("status", strStatus);
                            eachDetail.put("formId", fid);
                            eachDetail.put("currencyType",currency);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        intentnew.putExtra("EMPLOYEE",eachDetail.toString());
                        intentnew.putExtra("toEdit","No");
                        context.startActivity(intentnew); //This line raises error !


                    }else {
                        if (strStatus.equals("Denied")) {
                            return;
                        } else if (strStatus.equals("Approved")) {
                            return;
                        }

                        Intent intentnew = new Intent(context.getApplicationContext(), TEmployeeForm.class);
                        JSONObject eachDetail = new JSONObject();

                        try {
                            eachDetail.put("expenseDate", strDate + "-" + strMonth);
                            eachDetail.put("expense_discription", strexpDes);
                            eachDetail.put("amount", strAmount);
                            eachDetail.put("manager", strmanager);
                            eachDetail.put("category", strCategory);
                            eachDetail.put("status", strStatus);
                            eachDetail.put("formId", fid);
                            eachDetail.put("currencyType",currency);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        intentnew.putExtra("EMPLOYEE", eachDetail.toString());
                        intentnew.putExtra("toEdit", "Yes");
                        context.startActivity(intentnew); //This line raises error !
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(context.getApplicationContext(), "Adapter: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return inflateRow;
    }
}


