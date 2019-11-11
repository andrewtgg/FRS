package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class ViewEmployeesActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    Database db;

    int [] randImgStock = { R.drawable.stock_avatar_img, R.drawable.stock_avatar_img2, R.drawable.stock_avatar_img3, R.drawable.stock_avatar_img4};
    int [] randEmployeeId = { 134556, 367764, 395395, 130102};
    String[] randName = { "Bob Smith", "Jake Ma", "Forrester Guilo", "Rand Ofo"};

    ListView lView;
    ListAdapter lAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        db = new Database(this, null, 3);

        Cursor dbCursor = db.getEmployees();

        ArrayList<Employee> allEmployees = new ArrayList<>();

        while (dbCursor.moveToNext()) {

            Employee employee = new Employee(dbCursor.getInt(1), dbCursor.getBlob(2), dbCursor.getString(3), dbCursor.getString(4), dbCursor.getString(5));

            allEmployees.add(employee);
        }

        dbCursor.close();

        /* MOCK DATA */
        for (int i = 0; i < 4; i++) {

            Random rand = new Random();

            Bitmap bm = BitmapFactory.decodeResource(getResources(), randImgStock[rand.nextInt(3)]);

            byte[] imgData = getBitmapAsByteArray(bm);

            allEmployees.add(new Employee(randEmployeeId[rand.nextInt(3)], imgData, randName[rand.nextInt(3)], "Engineering", "09/01/1996"));
        }
        /*          */

        lView = findViewById(R.id.employee_list);

        // GenericListAdapter test = new GenericListAdapter(ViewEmployeesActivity.this, R.id.employee_list, allEmployees );

        lAdapter = new ListAdapter(ViewEmployeesActivity.this, allEmployees);

        Log.d(TAG, "adapter set");

        lView.setAdapter(lAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ViewEmployeesActivity.this, "Made it!", Toast.LENGTH_SHORT).show();
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
    public void onResume(){
        super.onResume();
    }

    /* Note: This is just to create mock data */
    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}