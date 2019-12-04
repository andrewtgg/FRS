package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
// import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class AccessLogsActivity extends AppCompatActivity {

    Database db;

    ListView lView;
    ActivityLogAdapter lAdapter;

    int [] randImgStock = { R.drawable.stock_avatar_img, R.drawable.stock_avatar_img2, R.drawable.stock_avatar_img3, R.drawable.stock_avatar_img4};
    int [] randEmployeeId = { 134556, 367764, 395395, 130102};


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
            SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm");
            String dateOnly = dateFmt.format(dbCursor.getString(3));
            String timeOnly = timeFmt.format(dbCursor.getString(3));
            ActivityLogItem activityLogItem = new ActivityLogItem(dbCursor.getInt(1), dbCursor.getBlob(2), dateOnly, timeOnly
                    , dbCursor.getInt(4), dbCursor.getFloat(5));
            allAcitivityLog.add(activityLogItem);
        }

        dbCursor.close();

        /* MOCK DATA */
        for (int i = 0; i < 4; i++) {

            Random rand = new Random();

            Bitmap bm = BitmapFactory.decodeResource(getResources(), randImgStock[rand.nextInt(3)]);

            byte[] imgData = getBitmapAsByteArray(bm);

            SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm");

            Date date = new Date();

            String dateOnly = dateFmt.format(date);
            String timeOnly = timeFmt.format(date);

            allAcitivityLog.add(new ActivityLogItem(randEmployeeId[rand.nextInt(3)], imgData, dateOnly, timeOnly, rand.nextInt(2), 0.4532));
        }
        /*          */

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

    /* Note: This is just to create mock data */
    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}