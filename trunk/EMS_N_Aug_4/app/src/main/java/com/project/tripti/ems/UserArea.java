package com.project.tripti.ems;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UserArea extends AppCompatActivity {

    private static final String FILE_FOLDER = "EMS";
    private static File file;
    private static final String filepath = Environment.getExternalStorageDirectory().getPath();

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    private static int gargi = 0;

    private static String userNamo=null;
    private static String format=null;
    private static String name=null;
    private static String role=null;

    private static ArrayList<String> userData = new ArrayList<>();
    private static ArrayList<String> reportData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("FireBase Message", "FCM Registration Token: " + token);



        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        role = intent.getStringExtra("role");

        final String userN = intent.getStringExtra("username");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username",name);
        editor.putString("role",role);

        // getting String
        editor.commit();


        //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

        final TextView display = (TextView)findViewById(R.id.diplay_name);
        final TextView roleDisplay = (TextView)findViewById(R.id.role);
        display.setText(name);
        roleDisplay.setText(role);

        ImageButton formatSelector = (ImageButton)findViewById(R.id.select_format);

        Button newForm = (Button)findViewById(R.id.new_form);
        newForm.setVisibility(View.INVISIBLE);
        Button oldForm = (Button)findViewById(R.id.previous_forms);
        oldForm.setVisibility(View.INVISIBLE);
        Button lists = (Button)findViewById(R.id.for_manager);
        lists.setVisibility(View.INVISIBLE);

        Button btnAllCategories = (Button)findViewById(R.id.btnAllCategories);
        btnAllCategories.setVisibility(View.INVISIBLE);

        Button btnFor_manager_status_waiting = (Button)findViewById(R.id.for_manager_status_waiting);
        btnFor_manager_status_waiting.setVisibility(View.INVISIBLE);


        Button btnReport = (Button)findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), ReportActivity.class);
                login.putExtra("userrole", role);
                login.putExtra("username", name);

                startActivity(login);
            }
        });



        Button btnSendPush = (Button)findViewById(R.id.btnSendPush);
        btnSendPush.setVisibility(View.VISIBLE);

        btnSendPush.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), SendPushActivity.class);

                startActivity(login);
            }
        });





        if(role.equals("Employee")){
            newForm.setVisibility(View.VISIBLE);
            oldForm.setVisibility(View.VISIBLE);
            btnAllCategories.setVisibility(View.VISIBLE);
            btnSendPush.setVisibility(View.INVISIBLE);


        }
        if(role.equals("Manager")){
            lists.setVisibility(View.VISIBLE);
            btnFor_manager_status_waiting.setVisibility(View.VISIBLE);
            btnSendPush.setVisibility(View.INVISIBLE);

        }



        btnAllCategories.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), TAllCategoriesActivity.class);
                login.putExtra("userrole", role);
                login.putExtra("username", name);
                login.putExtra("ActivityFrom", "UserArea");

                startActivity(login);
            }
        });
        newForm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), TEmployeeForm.class);
                login.putExtra("userrole", role);
                login.putExtra("username", userN);
                login.putExtra("toEdit", "No");
                startActivity(login);
            }
        });

        oldForm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), TFormHistoryActivity.class);
                startActivity(login);
            }
        });

        lists.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), TEmployeeListActivity.class);
                login.putExtra("userrole", role);
                login.putExtra("username", userN);
                login.putExtra("forStatus", "All");
                login.putExtra("ActivityFrom", "Manager");

                startActivity(login);
            }
        });

        btnFor_manager_status_waiting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(), TEmployeeListActivity.class);
                login.putExtra("userrole", role);
                login.putExtra("username", name);
                login.putExtra("forStatus", "waiting");
                login.putExtra("ActivityFrom", "Manager");

                startActivity(login);
            }
        });

        formatSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final Dialog select = new Dialog(UserArea.this);
                select.setContentView(R.layout.format_selector);
                select.setTitle("Select Format");

                final Button pdf = (Button) select.findViewById(R.id.pdf);
                final Button xls = (Button) select.findViewById(R.id.xls);

                pdf.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        format="PDF";

                        getFromIDs(UserArea.this, name);

                        select.dismiss();

                        Toast.makeText(getApplicationContext(), "Alpha: "+format, Toast.LENGTH_SHORT).show();
                    }
                });

                xls.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        format="XLS";

                        getFromIDs(UserArea.this, name);

                        select.dismiss();

                        Toast.makeText(getApplicationContext(), "Beta: "+format, Toast.LENGTH_SHORT).show();
                    }
                });
                select.show();
            }
        });
    }

    private static void addMetaData(Document document) {
        document.addTitle("Report");
        document.addSubject("Using iText");
        document.addKeywords("Android, PDF, iText");
        document.addAuthor("Tripti Soni");
        document.addCreator("Tripti Soni");
    }

    private static void addTitlePage(final Context context, Document document)
            throws DocumentException {


        Paragraph preface = new Paragraph();

        // We add one empty line
        addEmptyLine(preface, 1);

        // Lets write a big header
        preface.add(new Paragraph("Report", catFont));

        addEmptyLine(preface, 3);

        preface.add(new Paragraph("Report Generated By: " + userNamo,
                smallBold));

        addEmptyLine(preface, 5);


        document.add(preface);

        final PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Description"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        //Toast.makeText(context, "Creating Table", Toast.LENGTH_SHORT).show();

        c1 = new PdfPCell(new Phrase("Amount"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Category"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Status"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        //Toast.makeText(context.getApplicationContext(), "Data: " + reportData, Toast.LENGTH_SHORT).show();

        for(int j=0; j<reportData.size(); j++) {
            table.addCell(reportData.get(j));
        }

        document.add(table);
        userData.clear();
        reportData.clear();
        //gargi=0;
        //format=null;

        // Start a new page
        //document.newPage();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void getFromIDs(final Context context, String usernmae){
        Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(context.getApplicationContext(), "Testing: "+response, Toast.LENGTH_SHORT).show();

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        String counter = jsonResponse.getString("counter");
                        int count = Integer.parseInt(counter);

                        for(int i=1; i<=count; i++) {
                            String name = jsonResponse.getString(i+"");
                            userData.add(name);
                        }

                        setReportData(context);
                        //Toast.makeText(context.getApplicationContext(), "Names: " + userData, Toast.LENGTH_SHORT).show();

                                        /*Intent load=new Intent(getApplicationContext(), DisplayUsers.class);
                                        load.putExtra("userdata", userData);
                                        startActivity(load);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetData getFormID = new GetData(usernmae, responseListner);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getFormID);
        /*try {
            Thread.sleep(5000);
            setReportData(context);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    private static void getFile() {
        file = new File(filepath, FILE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    private static void docInitializer(Context contexts){
        try {
                getFile();

                //Create time stamp
                Date date = new Date() ;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);

                File myFile = new File(file.getAbsolutePath()+ File.separator + timeStamp + ".pdf");
                myFile.createNewFile();
                OutputStream output = new FileOutputStream(myFile);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setLinearPageMode();
            writer.setFullCompression();

            document.open();

            addMetaData(document);
            addTitlePage(contexts, document);

            document.close();
            Toast.makeText(contexts.getApplicationContext(), "PDF created successfully.", Toast.LENGTH_LONG).show();

            //UserArea omega = new UserArea();
            //omega.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setReportData(final Context context)
    {
        //Toast.makeText(context.getApplicationContext(), "Gargi", Toast.LENGTH_SHORT).show();
        for(int i=0; i<userData.size(); i++) {
            //Toast.makeText(context.getApplicationContext(), "Gargi"+i, Toast.LENGTH_SHORT).show();
            Response.Listener<String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //Toast.makeText(context.getApplicationContext(), "Gargi: "+ response, Toast.LENGTH_SHORT).show();

                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            String description=jsonResponse.getString("expense_discription");
                            String amount=jsonResponse.getString("amount");
                            String category=jsonResponse.getString("category");
                            String ststus=jsonResponse.getString("status");

                            int sts = Integer.parseInt(ststus);

                            reportData.add(description);
                            reportData.add(amount);
                            reportData.add(category);

                            if (sts == 2) {
                                ststus = "Denied";
                            } else if (sts == 1) {
                                ststus = "Approved";
                            } else {
                                ststus = "Waiting";
                            }

                            reportData.add(ststus);

                            //Toast.makeText(context.getApplicationContext(), "Size: "+(userData.size()-1), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context.getApplicationContext(), "NIL: "+gargi, Toast.LENGTH_SHORT).show();

                            if(gargi==(userData.size()-1)){
                                //Toast.makeText(context.getApplicationContext(), "Pre Data: "+userData, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context.getApplicationContext(), "Data: "+reportData, Toast.LENGTH_SHORT).show();

                                Toast.makeText(context.getApplicationContext(), "Gama: "+format, Toast.LENGTH_SHORT).show();

                                if(format.equals("PDF")) {
                                    Toast.makeText(context.getApplicationContext(), "PDF", Toast.LENGTH_SHORT).show();
                                    docInitializer(context);
                                }
                                if(format.equals("XLS")){
                                    Toast.makeText(context.getApplicationContext(), "XLS", Toast.LENGTH_SHORT).show();
                                    saveExcelFile(context);
                                }
                            }
                            gargi=gargi+1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            GetCellData search = new GetCellData(userData.get(i), responseListner);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(search);

            /*Toast.makeText(context.getApplicationContext(), "Gargi: "+i, Toast.LENGTH_SHORT).show();
            if(i==(userData.size()-1)){
                flag=true;
                break;
            }
            else{
                flag=false;
            }*/
        }
        gargi=0;

        /*try {
            Thread.sleep(5000);
            Toast.makeText(context.getApplicationContext(), "Pre Data: "+userData, Toast.LENGTH_SHORT).show();
            Toast.makeText(context.getApplicationContext(), "Data: "+reportData, Toast.LENGTH_SHORT).show();
            docInitializer(context);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    private static boolean saveExcelFile(final Context context) {

        Toast.makeText(context.getApplicationContext(), "Gargi", Toast.LENGTH_SHORT).show();

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            //Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Report");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Description");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Amount");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Category");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Status");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        int m=0;

        for(int n=1; n<=(reportData.size()/4); n++) {
            row = sheet1.createRow(n);
            for (int j = 0; j < 4; j++) {
                //Toast.makeText(context.getApplicationContext(), reportData.get(j+m), Toast.LENGTH_SHORT).show();

                c = row.createCell(j);
                c.setCellValue(reportData.get(j+m));
                c.setCellStyle(cs);
            }
            m=m+4;
        }

        // Create a path where we will place our List of objects on external storage
        getFile();

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);

        File myFile = new File(file.getAbsolutePath()+ File.separator + timeStamp + ".xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(myFile);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + myFile);
            Toast.makeText(context.getApplicationContext(), "Writing file", Toast.LENGTH_SHORT).show();
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + myFile, e);
            Toast.makeText(context.getApplicationContext(), "Error writing", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
            Toast.makeText(context.getApplicationContext(), "Failed to save file", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        userData.clear();
        reportData.clear();
        //gargi=0;
        //format=null;

        //UserArea omega = new UserArea();
        //omega.reload();

        return success;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    /*public void reload(){
        Intent load=new Intent(getApplicationContext(), UserArea.class);
        load.putExtra("name", name);
        load.putExtra("role", role);
        startActivity(load);
        finish();
    }*/
}
