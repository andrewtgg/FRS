package com.hw.frsecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewEmployeesActivity extends AppCompatActivity {

    Database db;

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

        lView = findViewById(R.id.employee_list);

        // GenericListAdapter test = new GenericListAdapter(ViewEmployeesActivity.this, R.id.employee_list, allEmployees );

        lAdapter = new ListAdapter(ViewEmployeesActivity.this, allEmployees);

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
                startActivity(new Intent(ViewEmployeesActivity.this, AddEmployeeActivity.class));
            }
        });

    }
}