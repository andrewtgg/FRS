package com.hw.frsecurity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;

// date related stuff
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class AddEmployeeActivity extends AppCompatActivity {

    Spinner newEmployeeDepartmentSpinner;
    ImageView newEmployeeImage;
    Database db;
    private Bitmap employee_img;

    static int CODE_RETURN_PIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);


        // database init
        db = new Database(this, null, 3);

        // mock photo (remove when done)
        newEmployeeImage = findViewById(R.id.new_employee_img);

        newEmployeeImage.setImageResource(R.drawable.stock_avatar_img2);

        // Adding values to spinner
        newEmployeeDepartmentSpinner = findViewById(R.id.add_employee_department_spinner);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.departments_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        newEmployeeDepartmentSpinner.setAdapter(adapter);

        findViewById(R.id.add_new_employee_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newEmployeeName = findViewById(R.id.new_employee_name);
                EditText newEmployeeID = findViewById(R.id.new_employee_id);

                String newEmployeeNameText = newEmployeeName.getText().toString();
                String newEmployeeIDText = newEmployeeID.getText().toString();

                // error handling
                if (newEmployeeNameText.matches("") || newEmployeeIDText.matches("")) {
                    Toast.makeText(AddEmployeeActivity.this, "Please fill all information correctly", Toast.LENGTH_LONG).show();
                }
                else {

                    String newEmployeeDept = newEmployeeDepartmentSpinner.getSelectedItem().toString();
                    // prep data to be saved to database
                    BitmapDrawable employeeImgDrawable = (BitmapDrawable) newEmployeeImage.getDrawable();

                    byte[] imgData = getBitmapAsByteArray(employeeImgDrawable.getBitmap());

                    String currentDateAndTime = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());

                    Employee newEmployee = new Employee(Integer.parseInt(newEmployeeIDText), imgData, newEmployeeNameText, newEmployeeDept, currentDateAndTime);

                    db.insertEmployee(newEmployee);

                    adapter.notifyDataSetChanged();

                    Toast.makeText(AddEmployeeActivity.this, "Inserted into Database!", Toast.LENGTH_LONG).show();
                    Intent i=new Intent();
                    setResult(RESULT_OK,i);
                    finish();
                    /*
                    startActivity(new Intent(AddEmployeeActivity.this, ViewEmployeesActivity.class));
                    finish();
                    */
                }
            }
        });

        //if employee img exists, set it again
        if (savedInstanceState != null){
            employee_img = savedInstanceState.getParcelable("employee_img");
            newEmployeeImage.setImageBitmap(employee_img);
        }
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void open_train_Cam(View view) {
        Intent i = new Intent(this, TrainCamActivity.class);
        startActivityForResult(i, CODE_RETURN_PIC);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_RETURN_PIC){
            if (resultCode == Activity.RESULT_OK) {
                    employee_img = data.getParcelableExtra(TrainCamActivity.EMPLOYEE_PIC);
                    newEmployeeImage.setImageBitmap(employee_img);

            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("employee_img",employee_img);
    }
}
