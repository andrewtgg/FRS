package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class ViewEmployeesActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    Database db;

    ListView lView;
    ListAdapter lAdapter;
    private FaceRecService mService;
    private boolean mBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FaceRecService.LocalBinder binder = (FaceRecService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_employees);

        db = new Database(this, null, 3);

        Cursor dbCursor = db.getEmployees();

        final ArrayList<Employee> allEmployees = new ArrayList<>();

        while (dbCursor.moveToNext()) {

            Employee employee = new Employee(dbCursor.getInt(1), dbCursor.getBlob(2), dbCursor.getString(3), dbCursor.getString(4), dbCursor.getString(5));

            allEmployees.add(employee);
        }

        dbCursor.close();

        final SwipeMenuListView lView = findViewById(R.id.employee_list);

        // GenericListAdapter test = new GenericListAdapter(ViewEmployeesActivity.this, R.id.employee_list, allEmployees );

        lAdapter = new ListAdapter(ViewEmployeesActivity.this, allEmployees);

        Log.d(TAG, "adapter set");

        lView.setAdapter(lAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(170);
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

        lView.setMenuCreator(creator);

        lView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //BIG JOE DATABASE
                        int id = allEmployees.get(position).getId();
                        db.deleteEmployee(allEmployees.get(position));
                        allEmployees.remove(position);
                        lAdapter.notifyDataSetChanged();
                        recreate();

                        delete_employee_refresh(""+id);

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        findViewById(R.id.add_employee_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ViewEmployeesActivity.this, AddEmployeeActivity.class), 0);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Log.d(TAG, "onActivityResult: ");
            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBound == false) {
            Intent intent = new Intent(this, FaceRecService.class);
            bindService(intent, connection, Context.BIND_IMPORTANT);
            mBound = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mBound == true) {
            unbindService(connection);
            mBound = false;
        }
    }

    private void delete_employee_refresh(String id) {
        mService.delete_employee("" + id);
        mService.create_new_model();

        db = new Database(this, null, 3);

        Cursor dbCursor = db.getEmployees();

        while (dbCursor.moveToNext()) {

            String employee_id = "" + dbCursor.getInt(1);
            mService.update_model(employee_id);
        }
        mService.save_model();

        dbCursor.close();
    }




}