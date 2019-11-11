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
import android.view.Window;
import android.view.WindowManager;
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




public class MainCamActivity extends CamActivity {
    private final String TAG = "MainCamActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main_cam);

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
            Log.i(TAG,"crop " + crop.rows() + " " + crop.cols());

            ;
            int width = rgba_frame.cols();
            int height = rgba_frame.rows();
            //Mat largerImage = new Mat(width, height,crop.type());
            //largerImage =
            //Mat mask = new Mat(crop.rows(), crop.cols(), CvType.CV_8U, Scalar.all(0));
            //crop.copyTo(largerImage, mask);
            //return largerImage;
            //Log.i(TAG,"LargerImage " + largerImage.rows() + " " + largerImage.cols());
           // Rect roi = new Rect(0, 0, crop.cols(), 10);
            //Mat sub =image.submat(roi);

           // crop.copyTo(rgba_frame, )


        }

        return rgba_frame;
    }
}
