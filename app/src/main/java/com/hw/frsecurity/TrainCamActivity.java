package com.hw.frsecurity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static org.opencv.imgproc.Imgproc.INTER_AREA;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import static org.opencv.imgproc.Imgproc.resize;

public class TrainCamActivity extends CamActivity {

    public final int NUM_TRAIN_PICS = 5;

    public static String EMPLOYEE_PIC = "EMPLOYEE PIC";
    private final String TAG = "TrainCamActivity";


    private int numfaces = 0;
    private Rect[] facesArray;

    Mat detected_face;

    ImageView preview_face;

    private ArrayList<Bitmap> train_faces = new ArrayList<Bitmap>();

    static int semaphore_cam = 0;
    private long mLastClickTime = 0;
    private String employee_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_train_cam);
        preview_face = findViewById(R.id.preview_face);

        Intent i = getIntent();
        employee_id = i.getExtras().getString("employee_id","none");
        Log.d(TAG, "Received employee_id:" + employee_id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "pause camera view");


    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba_frame = inputFrame.rgba();
        Mat gray_frame = inputFrame.gray();
        MatOfRect faces = new MatOfRect();
        //Mat resizeimage = new Mat();
        Mat crop = null;


        if (mAbsoluteFaceSize == 0) {
            int height = gray_frame.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        if (mJavaDetector != null)
            mJavaDetector.detectMultiScale(gray_frame, faces, 1.1, 2, 2,
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());


        facesArray = faces.toArray();
        numfaces = facesArray.length;
        for (Rect rect : facesArray) {
            crop = new Mat(rgba_frame, rect);
            if (numfaces == 1) {
                detected_face = crop;
            }
            Imgproc.rectangle(rgba_frame, rect.tl(), rect.br(), FACE_RECT_COLOR, 1);

        }



        /*if (resizeimage != null) {
            return resizeimage;
        }*/
        return rgba_frame;
    }

    public void take_picture(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 400){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(semaphore_cam > 0) {
            Log.e(TAG, "Semaphore is too high, not doing anything");
            return;
        }
        semaphore_cam++;


        Log.i(TAG, "Take a screenshot");
        System.out.println("faces: " + numfaces);

        if(numfaces == 0) {
            Toast.makeText(this, "No face detected!", Toast.LENGTH_SHORT).show();
            semaphore_cam--;
            return;
        }

        if(numfaces > 1) {
            Toast.makeText(this, "Multiple faces detected. Please try again.", Toast.LENGTH_SHORT).show();
            semaphore_cam--;
            return;
        }
        if(detected_face != null) {

            Mat dface = mirror(detected_face);

            int w = preview_face.getWidth();
            int h = preview_face.getHeight();
            System.out.println(w + " " + h);
            Size scaleSize = new Size(w,h);

            Mat resized_face = new Mat();
            resize(dface, resized_face, scaleSize , 0, 0, INTER_CUBIC);

            Bitmap img = Bitmap.createBitmap(resized_face.cols(), resized_face.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(resized_face,img);
            preview_face.setImageBitmap(img);

            Size scaleSize2 = new Size(200,200);

            Mat resized_face2 = new Mat();
            resize(dface, resized_face2, scaleSize2 , 0, 0, INTER_AREA);

            Bitmap img2 = Bitmap.createBitmap(resized_face2.cols(), resized_face2.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(resized_face2,img2);

            train_faces.add(img2);
        }

        int num_pictures = train_faces.size();
        Toast.makeText(this, "Total pics: " + num_pictures, Toast.LENGTH_SHORT).show();

        if(num_pictures >= NUM_TRAIN_PICS) {
            Intent resultIntent = new Intent();

            Bitmap returned_face = train_faces.get(0); //return the first face picture

            //Bitmap img = Bitmap.createBitmap(returned_face.cols(), returned_face.rows(),Bitmap.Config.ARGB_8888);
            //Utils.matToBitmap(returned_face,img);

            resultIntent.putExtra(EMPLOYEE_PIC, returned_face);
            setResult(Activity.RESULT_OK, resultIntent);
            semaphore_cam--;

            save_employee();
            this.finish();
        }
        else {
            semaphore_cam--;
        }

    }

    private void save_employee() {
        Log.d(TAG, "Saving images to local storage");
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        for (int i=0;i<train_faces.size();i++) {

            Bitmap face = train_faces.get(i);
            String filename = employee_id + "_" + i + ".jpg";
            Log.d(TAG, filename);
            File mypath=new File(directory,filename);
            FileOutputStream out;
            try {
                out = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                face.compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
