package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

public class AccessLogsActivity extends AppCompatActivity {

    Database db;

    ListView lView;
    ListAdapter lAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_logs);

        db = new Database(this, null, 1);

        Cursor dbCursor = db.getEmployees();


    }
}