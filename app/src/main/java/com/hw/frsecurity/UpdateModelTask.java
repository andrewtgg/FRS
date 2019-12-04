package com.hw.frsecurity;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import static org.opencv.core.CvType.CV_32SC1;

public class UpdateModelTask extends AsyncTask<String, Void, Void> {

    private final String TAG = "UpdateModelTask";

    ContextWrapper cw;

    public UpdateModelTask(ContextWrapper c) { cw = c; }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //System.loadLibrary("native-lib");
    }

    @Override
    protected Void doInBackground(String... strings) {
        File directory = cw.getDir("train_images", Context.MODE_PRIVATE);
        final String employee_id = strings[0];
        FilenameFilter employeefilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains(employee_id);
            };
        };

        File[] face_files = directory.listFiles(employeefilter);
        Mat labels = new Mat(face_files.length, 1, CV_32SC1);
        //int[] labels = new int[face_files.length];
        Vector<Mat> train_images = new Vector(face_files.length);


        for(int i =0; i < face_files.length;i++) {
            Log.d(TAG, "Filename: " + face_files[i].toString() + " ID:" + employee_id + " index: " + i);


            Mat image = Imgcodecs.imread(face_files[i].toString());
            Mat image_gray = new Mat();
            Imgproc.cvtColor(image,image_gray,Imgproc.COLOR_BGR2GRAY);
            train_images.add(image_gray);
            labels.put(i,0,Integer.parseInt(employee_id));
        }
        Log.d(TAG, "Labels: " + labels.dump());



        if(FaceRecService.faceRecognizer != null) {
            Log.d(TAG, "Training model with employee id " + employee_id);
            FaceRecService.faceRecognizer.update(train_images, labels);

            //String s = FaceRecService.faceRecognizer.getLabelInfo(12345);
            //Log.d(TAG, "label info: " + s);

        }


        return null;
    }
}
