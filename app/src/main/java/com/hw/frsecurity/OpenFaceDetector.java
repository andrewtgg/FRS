package com.hw.frsecurity;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;


public class OpenFaceDetector {
    private String filenameFaceCascade = "hi";
    private CascadeClassifier      mJavaDetector;
    private String TAG = "OpenFaceDetector";

    public OpenFaceDetector()
    {

    }
    public Mat detectFace(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        MatOfRect faces = new MatOfRect();
        return faces;
    }
}
