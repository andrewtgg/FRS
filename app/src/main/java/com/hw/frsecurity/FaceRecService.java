package com.hw.frsecurity;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> load/save model to file
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.ImageReader;
<<<<<<< HEAD
=======
import android.app.Service;
import android.content.Context;
import android.content.Intent;
>>>>>>> save img, start on fr
=======
>>>>>>> load/save model to file
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> load/save model to file
import org.opencv.core.Mat;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import static org.opencv.core.CvType.CV_32SC1;
import static org.opencv.core.CvType.CV_8U;

<<<<<<< HEAD
=======
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
>>>>>>> save img, start on fr
=======
>>>>>>> load/save model to file

public class FaceRecService extends Service {

    private String TAG = "FaceRecService";
    private final String MODEL_NAME = "lbph.yml";
    //private final int NUM_SAMPLES = 5;

    private final IBinder binder = new LocalBinder();
    public static LBPHFaceRecognizer faceRecognizer;
<<<<<<< HEAD

=======
>>>>>>> load/save model to file


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

    @Override
    public void onDestroy() {
        System.out.println("onDestroy on service");

        //save_model();
        super.onDestroy();
    }

    private void initialize_model() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("modelDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,MODEL_NAME);

        Log.d(TAG, "Trying to read saved model from: " + mypath);

        if(mypath.exists()) {
            faceRecognizer = LBPHFaceRecognizer.create();
            faceRecognizer.read(mypath.toString());
            Log.d(TAG, "Loaded saved model!");
        }
        else {
            faceRecognizer = LBPHFaceRecognizer.create(1,8,8,8, 100);
            Log.d(TAG, "Initialized new model!");
        }
    }

    public void save_model() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("modelDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,MODEL_NAME);

        Log.d(TAG, "Trying to save model to: " + mypath);

        if(!mypath.exists()) {
            Log.d(TAG, "Mypath exists");
        }


        if(faceRecognizer != null) {
            Log.d(TAG, "Model not null");
            faceRecognizer.write(mypath.toString());
        }

    }

    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");
                //faceRecognizer = LBPHFaceRecognizer.create(1,8,8,8, 100);
                //Log.d(TAG, "faceRecognizer Initialized, threshold: " + faceRecognizer.getThreshold());
                initialize_model();
                /*try {
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }*/
                //update_model("12345");
                //Load native library after(!) OpenCV initialization
                System.loadLibrary("native-lib");
            } else {
                super.onManagerConnected(status);
            }
        }
        //TODO override finish to kill service
    };
    public void update_model(final String employee_id) {
        Log.d(TAG, "Updating model with employee id " + employee_id);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        UpdateModelTask mTask = new UpdateModelTask(cw);
        mTask.execute(employee_id);


        try {
            Thread.sleep(1000);
        } catch (Exception e) {

            e.printStackTrace();
        }

        //TODO save_model in asynctask
        save_model();
    }


    public int model_predict(Mat face) {
        Log.d(TAG, "Using model to predict!");
        int[] label = new int[2];
        double[] confidence = new double[2];
        faceRecognizer.predict(face,label,confidence);

        Log.d(TAG, "Label: " + label[0] + "Confidence: " + confidence[0]);
        return label[0];
    }

}
