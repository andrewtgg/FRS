package com.hw.frsecurity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    static String TAG = "AddEmployeeActivity";

    Spinner newEmployeeDepartmentSpinner;
    ImageView newEmployeeImage;
    Database db;
    private Bitmap employee_img;
    private static boolean real_image = false;
    private String newEmployeeIDText;

    static int CODE_RETURN_PIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);


        Log.d(TAG, "onCreate add employee view");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                newEmployeeIDText = newEmployeeID.getText().toString();

                // error handling
                if (newEmployeeNameText.matches("") || newEmployeeIDText.matches("")) {
                    Toast.makeText(AddEmployeeActivity.this, "Please fill all information correctly", Toast.LENGTH_LONG).show();
                }
                else {

                    String newEmployeeDept = newEmployeeDepartmentSpinner.getSelectedItem().toString();
                    // prep data to be saved to database
                    BitmapDrawable employeeImgDrawable = (BitmapDrawable) newEmployeeImage.getDrawable();

                    byte[] imgData = getBitmapAsByteArray(employeeImgDrawable.getBitmap());

                    //String currentDateAndTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss");
                    String currentDateAndTime = sdf.format(new Date());

                    Employee newEmployee = new Employee(Integer.parseInt(newEmployeeIDText), imgData, newEmployeeNameText, newEmployeeDept, currentDateAndTime);

                    long j = db.insertEmployee(newEmployee);

                    if (j == -1) {
                        Toast.makeText(AddEmployeeActivity.this, "Unable to add: Employee ID already exists!", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    adapter.notifyDataSetChanged();

                    Toast.makeText(AddEmployeeActivity.this, "Inserted into Database!", Toast.LENGTH_LONG).show();
                    Intent i=new Intent();
                    setResult(RESULT_OK,i);
                    real_image = false;
                    finish();
                    /*
                    startActivity(new Intent(AddEmployeeActivity.this, ViewEmployeesActivity.class));
                    finish();
                    */
                }
            }
        });

        findViewById(R.id.new_employee_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newEmployeeName = findViewById(R.id.new_employee_name);
                EditText newEmployeeID = findViewById(R.id.new_employee_id);

                String newEmployeeNameText = newEmployeeName.getText().toString();
                String newEmployeeIDText = newEmployeeID.getText().toString();

                if (newEmployeeNameText.matches("") || newEmployeeIDText.matches("")) {
                    Toast.makeText(AddEmployeeActivity.this, "Please fill out all information before taking a picture", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(AddEmployeeActivity.this, TrainCamActivity.class);
                    i.putExtra("employee_id", newEmployeeIDText);
                    startActivityForResult(i, CODE_RETURN_PIC);
                }
            }
        });


        //if employee img exists, set it again
        if (savedInstanceState != null){
            if(real_image) {
                employee_img = savedInstanceState.getParcelable("employee_img");
                newEmployeeImage.setImageBitmap(employee_img);
            }
        }
    }
    

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    /*
    public void open_train_Cam(View view) {
        Intent i = new Intent(this, TrainCamActivity.class);
        startActivityForResult(i, CODE_RETURN_PIC);
    }
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_RETURN_PIC){
            if (resultCode == Activity.RESULT_OK) {
                    employee_img = data.getParcelableExtra(TrainCamActivity.EMPLOYEE_PIC);
                    newEmployeeImage.setImageBitmap(employee_img);
                    real_image = true;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("employee_img",employee_img);
    }
}
