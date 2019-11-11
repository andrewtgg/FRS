package com.hw.frsecurity;

import android.util.Log;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.INTER_AREA;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import static org.opencv.imgproc.Imgproc.resize;

public class TrainCamActivity extends CamActivity {
    private final String TAG = "TrainCamActivity";



    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba_frame = inputFrame.rgba();
        Mat gray_frame = inputFrame.gray();
        MatOfRect faces = new MatOfRect();
        Mat resizeimage = new Mat();
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

            Size scaleSize = new Size(width,height);
            resize(crop, resizeimage, scaleSize , 0, 0, INTER_CUBIC);
            Log.i(TAG,"resize " + resizeimage.rows() + " " + resizeimage.cols());
            return resizeimage;

        }
        Log.i(TAG,"frame " + rgba_frame.rows() + " " + rgba_frame.cols());

        /*if (resizeimage != null) {
            return resizeimage;
        }*/
        return rgba_frame;
    }
}
