package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private long mLastClickTime = 0;


    private Button button_access_logs;
    private Intent intent;
    private FaceRecService mService;
    private boolean mBound = false;
    private boolean mStarted = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FaceRecService.LocalBinder binder = (FaceRecService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mStarted = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        button_access_logs = findViewById(R.id.button_access_logs);

        findViewById(R.id.button_main_cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View  v) {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                findViewById(R.id.button_main_cam).startAnimation(myAnim);
                startActivity(new Intent(MainActivity.this, MainCamActivity.class));
            }
        });

        button_access_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                findViewById(R.id.button_access_logs).startAnimation(myAnim);
                button_access_logs.setEnabled(false);
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(MainActivity.this, AccessLogsActivity.class));
            }
        });

        findViewById(R.id.button_view_employees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                findViewById(R.id.button_view_employees).startAnimation(myAnim);
                startActivity(new Intent(MainActivity.this, ViewEmployeesActivity.class));
            }
        });
        if(mStarted == false) {
            intent = new Intent(this, FaceRecService.class);
            bindService(intent, connection, Context.BIND_IMPORTANT);
            startService(intent);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        button_access_logs.setEnabled(true);
        if(mBound == false) {
            intent = new Intent(this, FaceRecService.class);
            bindService(intent, connection, Context.BIND_IMPORTANT);
        }

    }

    @Override
    protected void onPause() {
        System.out.println("mainActivity onPause called");
        if(mBound == true) {
            unbindService(connection);
            mBound = false;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        System.out.println("mainActivity onDestroy called");
        super.onDestroy();
    }
}