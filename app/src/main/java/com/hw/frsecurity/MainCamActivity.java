package com.hw.frsecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.core.CvType.CV_32SC1;
import static org.opencv.imgproc.Imgproc.INTER_AREA;
import static org.opencv.imgproc.Imgproc.resize;


public class MainCamActivity extends CamActivity {
    private final String TAG = "MainCamActivity";

    private int num_faces = 0;
    private int dummy_ctr = 0;

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
        super.onCreate(savedInstanceState, R.layout.activity_main_cam);


    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mBound) {
            unbindService(connection);
            mBound = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
        if(!mBound) {
            Intent intent = new Intent(this, FaceRecService.class);
            bindService(intent, connection, Context.BIND_IMPORTANT);
        }
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba_frame = inputFrame.rgba();
        Mat gray_frame = inputFrame.gray();
        MatOfRect faces = new MatOfRect();

        if (mAbsoluteFaceSize == 0) {
            int height = gray_frame.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        if (mJavaDetector != null)
            mJavaDetector.detectMultiScale(gray_frame, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());


        Rect[] facesArray = faces.toArray();
        int new_num = facesArray.length;
        int new_idx;
        if(new_num > num_faces) {
            dummy_ctr++;

            if(dummy_ctr > 2) {
                new_idx = new_num-1;
                num_faces = new_num;
                Log.d(TAG, "newface at: " + new_idx);
                Mat crop = new Mat(rgba_frame, facesArray[new_idx]);
                send_face_to_model(crop);
                dummy_ctr = 0;
            }
        }
        else {
            dummy_ctr = 0;
            num_faces = new_num;
        }


        for (Rect rect : facesArray) {
            Imgproc.rectangle(rgba_frame, rect.tl(), rect.br(), FACE_RECT_COLOR, 3);
        }

        return rgba_frame;
    }
    private void send_face_to_model(Mat img) {
        Size scaleSize = new Size(200,200);

        Mat resized_face = new Mat();
        resize(img, resized_face, scaleSize , 0, 0, INTER_AREA);

        Mat image_gray = new Mat();
        Imgproc.cvtColor(img,image_gray,Imgproc.COLOR_BGR2GRAY);
        if(mBound) {
            Log.d(TAG, "Sending face to model");
            mService.model_predict(image_gray);
        }

        //Bitmap img2 = Bitmap.createBitmap(resized_face.cols(), resized_face.rows(),Bitmap.Config.ARGB_8888);
        //Utils.matToBitmap(resized_face,img2);

    }
}
