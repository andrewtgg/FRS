package com.hw.frsecurity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SaveEmployeeThread extends Thread {

    private final String TAG = "SaveEmployeeThread";
    private ContextWrapper cw;
    private ArrayList<Mat> train_faces;
    private String employee_id;
    public SaveEmployeeThread(ContextWrapper c, ArrayList<Mat> tf, String id) {
        cw = c;
        train_faces = tf;
        employee_id = id;

    }
    public native long tan_triggs(long src_addr);
    @Override
    public void run() {
        System.loadLibrary("native-lib");
        File directory = cw.getDir("train_images", Context.MODE_PRIVATE);
        for (int i=0;i<train_faces.size();i++) {

            Mat face = train_faces.get(i);
            Mat face_gray = new Mat();
            Imgproc.cvtColor(face,face_gray,Imgproc.COLOR_BGR2GRAY);

            Log.d(TAG, "ADR: " + face_gray.getNativeObjAddr());
            long res_addr = tan_triggs(face_gray.getNativeObjAddr());
            Log.d(TAG, "new adr: " + res_addr);

            Mat face2 = new Mat(res_addr);
            Log.d(TAG, "new : " + face2.channels());

            Bitmap img = Bitmap.createBitmap(face2.cols(), face2.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(face2,img);
            String filename = employee_id + "_" + i + ".jpg";
            File mypath=new File(directory,filename);
            FileOutputStream out;
            try {
                out = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                img.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
