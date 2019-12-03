package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
// import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class AccessLogsActivity extends AppCompatActivity {
    private String TAG = "AccessLogsActivity";

    Database db;

    ListView lView;
    ActivityLogAdapter lAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_access_logs);

        db = new Database(this, null, 3);

        Cursor dbCursor = db.getActivityLogs();


        final ArrayList<ActivityLogItem> allAcitivityLog = new ArrayList<>();

        while (dbCursor.moveToNext()) {
            // int newId, byte[] newImg, String newDateSeen, int newStatus
            SimpleDateFormat fmt=new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            SimpleDateFormat dateFmt = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat timeFmt = new SimpleDateFormat("kk:mm");
            Date date = null;

            try {
                String datestring = dbCursor.getString(3);
                Log.d(TAG, "datestring: " + datestring);
                date = fmt.parse(dbCursor.getString(3));
                Log.d(TAG, "success: " + date.toString());
            } catch (Exception e) {
                Log.d(TAG, "Parse error");
                e.printStackTrace();
            }

            String dateOnly = dateFmt.format(date);
            String timeOnly = timeFmt.format(date);
            ActivityLogItem activityLogItem = new ActivityLogItem(dbCursor.getInt(1), dbCursor.getBlob(2), dateOnly, timeOnly
                    , dbCursor.getInt(4), dbCursor.getFloat(5));

            Log.d(TAG, "ID: " + dbCursor.getInt(1) + "STAUTS:" + dbCursor.getInt(4));
            allAcitivityLog.add(activityLogItem);
        }

        dbCursor.close();

        lView = findViewById(R.id.access_log_list);

        // GenericListAdapter test = new GenericListAdapter(ViewEmployeesActivity.this, R.id.employee_list, allEmployees );

        lAdapter = new ActivityLogAdapter(AccessLogsActivity.this, allAcitivityLog);

        lView.setAdapter(lAdapter);


        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AccessLogsActivity.this);
                // convert the decimal to percentage
                alert.setMessage("Probability: " + allAcitivityLog.get(i).getProbability());
                alert.setPositiveButton("OK",null);
                alert.show();
            }
        });

    }

}