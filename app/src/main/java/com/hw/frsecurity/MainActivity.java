package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_main_cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View  v) {
                startActivity(new Intent(MainActivity.this, TrainCamActivity.class));
            }
        });

        findViewById(R.id.button_access_logs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AccessLogsActivity.class));
            }
        });

        findViewById(R.id.button_view_employees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewEmployeesActivity.class));
            }
        });

    }
}