package com.example.awaaz.Class;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class Preprocessor {
    private Frame frame;

    public Preprocessor(Frame frame) {
        this.frame = frame;
    }

    public Mat convert2gray(Mat src) {

        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY);
        return src;
    }

    public Mat Mirror(Mat src) {
        Core.flip(src, src, +1);
        return src;
    }

    public Mat cropMat(Mat src) {

        Rect rect = new Rect(250, 150, src.width() / 2, src.height() / 2 + 50);
        Mat cropped = new Mat(src, rect);
        return cropped;
    }
}







