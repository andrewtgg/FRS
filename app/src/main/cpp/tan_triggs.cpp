/*
 * MODIFIED BY JOSHUA TAI
 * Copyright (c) 2012. Philipp Wagner <bytefish[at]gmx[dot]de>.
 * Released to public domain under terms of the BSD Simplified license.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the organization nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 *   See <http://www.opensource.org/licenses/bsd-license>
 */
#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>

#include <fstream>
#include <sstream>

using namespace cv;
using namespace std;




// Normalizes a given image into a value range between 0 and 255.
Mat norm_0_255(const Mat& src) {
    // Create and return normalized image:
    Mat dst;
    cout << src.channels();
    switch(src.channels()) {
        case 1:
            cv::normalize(src, dst, 0, 255, NORM_MINMAX, CV_8UC1);
            break;
        case 3:
            cv::normalize(src, dst, 0, 255, NORM_MINMAX, CV_8UC3);
            break;
        default:
            src.copyTo(dst);
            break;
    }
    return dst;
}

//
// Calculates the TanTriggs Preprocessing as described in:
//
//      Tan, X., and Triggs, B. "Enhanced local texture feature sets for face
//      recognition under difficult lighting conditions.". IEEE Transactions
//      on Image Processing 19 (2010), 1635–650.
//
// Default parameters are taken from the paper.
//
Mat tan_triggs_preprocessing(Mat src,
                             float alpha = 0.1, float tau = 10.0, float gamma = 0.2, int sigma0 = 1,
                             int sigma1 = 2) {

    // Convert to floating point:
    //Mat X = src.getMat();
    src.convertTo(src, CV_32FC1);
    // Start preprocessing:
    Mat I;
    pow(src, gamma, I);
    // Calculate the DOG Image:
    {
        Mat gaussian0, gaussian1;
        // Kernel Size:
        int kernel_sz0 = (3*sigma0);
        int kernel_sz1 = (3*sigma1);
        // Make them odd for OpenCV:
        kernel_sz0 += ((kernel_sz0 % 2) == 0) ? 1 : 0;
        kernel_sz1 += ((kernel_sz1 % 2) == 0) ? 1 : 0;
        GaussianBlur(I, gaussian0, Size(kernel_sz0,kernel_sz0), sigma0, sigma0, BORDER_REPLICATE);
        GaussianBlur(I, gaussian1, Size(kernel_sz1,kernel_sz1), sigma1, sigma1, BORDER_REPLICATE);
        subtract(gaussian0, gaussian1, I);
    }

    {
        double meanI = 0.0;
        {
            Mat tmp;
            pow(abs(I), alpha, tmp);
            meanI = mean(tmp).val[0];

        }
        I = I / pow(meanI, 1.0/alpha);
    }

    {
        double meanI = 0.0;
        {
            Mat tmp;
            pow(min(abs(I), tau), alpha, tmp);
            meanI = mean(tmp).val[0];
        }
        I = I / pow(meanI, 1.0/alpha);
    }

    // Squash into the tanh:
    {
        Mat exp_x, exp_negx;
        exp( I / tau, exp_x );
        exp( -I / tau, exp_negx );
        divide( exp_x - exp_negx, exp_x + exp_negx, I );
        I = tau * I;
    }
    return I;
}

int main(int argc, const char *argv[]) {
    // Get filename to the source image:
    if (argc != 2) {
        cout << "usage: " << argv[0] << " <image.ext>" << endl;
        exit(1);
    }
    // Load image & get skin proportions:
    //Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    // Calculate the TanTriggs Preprocessed image with default parameters:
    //Mat preprocessed = tan_triggs_preprocessing(image);
    // Draw it on screen:
    //imshow("Original Image", image);
    //imshow("TanTriggs Preprocessed Image", norm_0_255(preprocessed));
    // Show the images:
    waitKey(0);
    // Success!
    return 0;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_hw_frsecurity_SaveEmployeeThread_tan_1triggs(JNIEnv *env, jobject thiz, jlong src_addr) {
    Mat* input = (Mat*)src_addr;

    Mat *mat = new Mat();
    Mat triggs = tan_triggs_preprocessing(*input);
    cout << "triggs " << triggs.channels();

    Mat triggs2 = norm_0_255(triggs);
    cout << "triggs2" << triggs2.channels();
    *mat = triggs2.clone();
    return (jlong)mat;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_hw_frsecurity_MainCamActivity_tan_1triggs(JNIEnv *env, jobject thiz, jlong src_addr) {
    Mat* input = (Mat*)src_addr;

    Mat *mat = new Mat();
    Mat triggs = tan_triggs_preprocessing(*input);
    cout << "triggs " << triggs.channels();

    Mat triggs2 = norm_0_255(triggs);
    cout << "triggs2" << triggs2.channels();
    *mat = triggs2.clone();
    return (jlong)mat;
}