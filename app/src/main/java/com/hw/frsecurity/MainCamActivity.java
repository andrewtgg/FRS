package com.hw.frsecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainCamActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "MainCamActivity";
    private static final int CAMERA_PERMISSION_REQUEST = 1;


    private CameraBridgeViewBase openCvCameraView;

    private OpenFaceDetector mFaceDetector;


    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private float mRelativeFaceSize = 0.2f;
    private int mAbsoluteFaceSize = 0;
    private static final Scalar FACE_RECT_COLOR         = new Scalar(255, 0, 0, 255);

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");

                //Load native library after(!) OpenCV initialization
                System.loadLibrary("native-lib");

                try{
                    // load cascade file from application resources
                    InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                    mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
                    FileOutputStream os = new FileOutputStream(mCascadeFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    os.close();

                    mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                    if (mJavaDetector.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier");
                        mJavaDetector = null;
                    } else
                        Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());


                    cascadeDir.delete();
                }catch(IOException e){
                    e.printStackTrace();
                    Log.i(TAG, "Failed to load cascade. Exception thrown: " + e);
                }

                openCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);



        setContentView(R.layout.activity_main_cam);


        openCvCameraView = findViewById(R.id.main_surface);
        openCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);

        openCvCameraView.setVisibility(SurfaceView.VISIBLE);

        openCvCameraView.setCvCameraViewListener(this);

        mFaceDetector = new OpenFaceDetector();

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
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult");
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission Granted");
                openCvCameraView.setCameraPermissionGranted();
            } else {
                String message = "Camera permission was not granted";
                Log.e(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(TAG, "Unexpected permission request");
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }
    @Override
    public void onCameraViewStopped() {
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (openCvCameraView != null)
            openCvCameraView.disableView();
    }
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba_frame = inputFrame.rgba();
        Mat gray_frame = inputFrame.gray();
        MatOfRect faces = new MatOfRect();
        Mat crop;

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
        for (Rect rect : facesArray) {
            Imgproc.rectangle(rgba_frame, rect.tl(), rect.br(), FACE_RECT_COLOR, 3);
            crop = new Mat(rgba_frame, rect);
;
            int width = rgba_frame.cols();
            int height = rgba_frame.rows();
            Mat largerImage = new Mat(width, height,crop.type());
            //largerImage =
            Mat mask = new Mat(crop.rows(), crop.cols(), CvType.CV_8U, Scalar.all(0));
            crop.copyTo(largerImage, mask);
            //return largerImage;
            //Log.i(TAG,"LargerImage " + largerImage.rows() + " " + largerImage.cols());
           // Rect roi = new Rect(0, 0, crop.cols(), 10);
            //Mat sub =image.submat(roi);

           // crop.copyTo(rgba_frame, )


        }
        Log.i(TAG,"frame " + rgba_frame.rows() + " " + rgba_frame.cols());

        return rgba_frame;
    }
}
