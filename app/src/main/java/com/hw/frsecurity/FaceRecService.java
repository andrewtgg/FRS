package com.hw.frsecurity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FaceRecService extends Service {

    private String TAG = "FaceRecService";

    private final IBinder binder = new LocalBinder();
    private LBPHFaceRecognizer faceRecognizer;




    public class LocalBinder extends Binder {
        FaceRecService getService() {
            // Return this instance so clients can call public methods
            System.out.println("FaceRecService getservice called");
            return FaceRecService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("service onBind called");
        return binder;
    }
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        System.out.println("startcommand on service");
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");
                faceRecognizer = LBPHFaceRecognizer.create(1,8,8,8, 100);
                Log.d(TAG, "faceRecognizer Initialized, threshold: " + faceRecognizer.getThreshold());

                //Load native library after(!) OpenCV initialization
                System.loadLibrary("native-lib");
            } else {
                super.onManagerConnected(status);
            }
        }
        //TODO override finish to kill service
    };


    public void update_model(String employee_id) {
        Log.d(TAG, "Updating model with employee id " + employee_id);
    }

}
