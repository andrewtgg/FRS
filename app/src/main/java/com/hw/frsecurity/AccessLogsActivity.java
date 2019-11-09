package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class AccessLogsActivity extends AppCompatActivity {

    Database db;

    ListView lView;
    ListAdapter lAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_logs);

        db = new Database(this, null, 1);

        Cursor dbCursor = db.getActivityLogs();


        ArrayList<ActivityLogItem> allAcitivityLog = new ArrayList<>();

        while (dbCursor.moveToNext()) {
            // int newId, byte[] newImg, String newDateSeen, int newStatus
            ActivityLogItem activityLogItem = new ActivityLogItem(dbCursor.getInt(1), dbCursor.getBlob(2), dbCursor.getString(3), dbCursor.getInt(4));
            allAcitivityLog.add(activityLogItem);
        }

        dbCursor.close();

        // TODO create list adapter and finish activity log
    }
}