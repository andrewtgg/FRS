package com.hw.frsecurity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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



import java.util.ArrayList;

import static org.opencv.imgproc.Imgproc.INTER_AREA;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import static org.opencv.imgproc.Imgproc.resize;

public class TrainCamActivity extends CamActivity {

    public static String EMPLOYEE_PIC = "EMPLOYEE PIC";
    private final String TAG = "TrainCamActivity";


    private int numfaces = 0;
    private Rect[] facesArray;

    Mat detected_face;

    ImageView preview_face;

    private ArrayList<Mat> train_faces = new ArrayList<Mat>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_train_cam);
        preview_face = findViewById(R.id.preview_face);
    }



    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba_frame = inputFrame.rgba();
        Mat gray_frame = inputFrame.gray();
        MatOfRect faces = new MatOfRect();
        Mat resizeimage = new Mat();
        Mat crop = null;


        if (mAbsoluteFaceSize == 0) {
            int height = gray_frame.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        if (mJavaDetector != null)
            mJavaDetector.detectMultiScale(gray_frame, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());


        facesArray = faces.toArray();
        numfaces = facesArray.length;
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

            Size scaleSize = new Size(width,height);
            resize(crop, resizeimage, scaleSize , 0, 0, INTER_CUBIC);
            Log.i(TAG,"resize " + resizeimage.rows() + " " + resizeimage.cols());
            //return resizeimage;

        }

        if (numfaces == 1) {
            detected_face = crop;
        }

        /*if (resizeimage != null) {
            return resizeimage;
        }*/
        return rgba_frame;
    }

    public void take_picture(View view) {
        Log.i(TAG, "Take a screenshot");
        System.out.println("faces: " + numfaces);

        if(numfaces == 0) {
            Toast.makeText(this, "No face detected!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(numfaces > 1) {
            Toast.makeText(this, "Multiple faces detected. Please try again.", Toast.LENGTH_SHORT).show();
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

            train_faces.add(dface);
        }

        int num_pictures = train_faces.size();
        Toast.makeText(this, "There are " + num_pictures + " faces", Toast.LENGTH_SHORT).show();

        if(num_pictures >= 3) {
            Intent resultIntent = new Intent();

            Mat returned_face = train_faces.get(0); //return the first face picture

            Bitmap img = Bitmap.createBitmap(returned_face.cols(), returned_face.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(returned_face,img);

            resultIntent.putExtra(EMPLOYEE_PIC, img);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }

    }
}
