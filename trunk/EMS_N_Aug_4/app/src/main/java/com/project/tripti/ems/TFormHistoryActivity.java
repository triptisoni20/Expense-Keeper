package com.project.tripti.ems;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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

public class TFormHistoryActivity extends AppCompatActivity {

    private static final String GETEMPLOYEEFORM_URL = "http://nilotpal.netai.net/t_getEmployeForm.php";

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tform_history);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listForm);


        callgetEmployeeList();

    }

    private void callgetEmployeeList() {

        Intent intent = getIntent();
        Map<String, String> jbForPost = new HashMap();


        JSONObject jo = new JSONObject(jbForPost);

        Log.e("JSON ", "to go" + jbForPost.toString());
        String getURl = GETEMPLOYEEFORM_URL +"?status=101"
                ;
        getURl = getURl.replace(" ","%20");

        JsonObjectRequest jsonRequestPOST = new JsonObjectRequest(Request.Method.GET,
                getURl, jo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // the response is already constructed as a JSONObject!
                try {

//                    Toast.makeText(getApplicationContext(),
//                            response.toString(), Toast.LENGTH_LONG)
//                            .show();
                    //response = response.getJSONObject("info");
                    JSONArray a = response.getJSONArray("info");

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
                //arrAllFormData[i] = "Form: " + jb.getInt("formId") + " status:  " + stat + " Date: " + jb.getString("expenseDate");

                status_in_words[i] = stat;
                category[i] = jb.getString("category");
                amount[i] = jb.getString("amount");

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
                expD[i] = jb.getString("expense_discription");
                currency[i] = jb.getString("currencyType");


            }

            listView.setAdapter(new ViewAdapter(this, legth, day, month, category, amount, status_in_words,manager,formIds,year,expD,currency));


        } catch (Exception e) {
            e.printStackTrace();
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
        int formId;
        String currencyT;



        SingleRow(String date, String month, String category, String amount, String status,String manager,int formId,String year,String expense_discription,String currency){
            this.date = date;
            this.month =month;
            this.category = category;
            this.amount = amount;
            this.status = status;
            this.expense_discription = expense_discription;
            this.manager = manager;
            this.formId = formId;
            this.year = year;
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

        ViewAdapter(TFormHistoryActivity context, int len, String[] date, String[] month, String[] category, String[] amount, String[] status,String[] manager,int[] formIds,String[] year,String expDes[],String currency[]) {
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
                category.setText("Category: " + strCategory);
                amount.setText("Amount: " + strAmount);
                status.setText("Status: " + strStatus);


                edit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (strStatus.equals("Denied")) {
                            return;
                        }else if(strStatus.equals("Approved")){
                            return;
                        }

                        // ListView Clicked item index
                        int itemPosition     = position;

                        Intent intentnew = new Intent(context.getApplicationContext(), TEmployeeForm.class);
                        JSONObject eachDetail = new JSONObject();

                        try {
                            eachDetail.put("expenseDate", strDate +"-"+ strMonth);
                            eachDetail.put("expense_discription", strexpDes);
                            eachDetail.put("amount", strAmount);
                            eachDetail.put("manager", strmanager);
                            eachDetail.put("category", strCategory);
                            eachDetail.put("status", strStatus);
                            eachDetail.put("formId", fid);
                            eachDetail.put("currencyType", currency);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        intentnew.putExtra("EMPLOYEE",eachDetail.toString());
                        intentnew.putExtra("toEdit","Yes");
                        context.startActivity(intentnew); //This line raises e

                        //TO-DO
                    }
                });
            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), "Adapter: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
            return inflateRow;
        }
    }
}
