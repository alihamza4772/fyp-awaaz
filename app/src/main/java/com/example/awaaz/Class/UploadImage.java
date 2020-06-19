package com.example.awaaz.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import spencerstudios.com.fab_toast.FabToast;

public class UploadImage {
    private List<Frame> frameList;
    private Context context;
    TedImagePicker tedImagePicker;
    private Narrator narrator;
    Bitmap bitmap;
    private static final String MODEL_PATH = "masked_alphabets.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 50;
    private Classifier classifier;
    private ImageView imageView;

    public List<Frame> getFrameList() {
        return frameList;
    }

    public void setFrameList(List<Frame> frameList) {
        this.frameList = frameList;
    }

    public void addFrame(Frame frame) {
        frameList.add(frame);
    }

    public UploadImage(Context context, ImageView imageView) {
        frameList = new ArrayList<Frame>();
        this.context = context;
        this.imageView = imageView;
        try {
            classifier = ImageClassifier.create(
                    context.getAssets(),
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE,
                    QUANT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (OpenCVLoader.initDebug()) {
            Log.e("Opencv", "Loaded");
        } else {
            FabToast.makeText(context, "Unable to attach OpenCV", FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
            try {
                Thread.sleep(2000);

                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        narrator = new Narrator(this.context);

    }

    String text = "";

    protected void openGallery() {
        TedImagePicker.with(context)
                .startMultiImage(new OnMultiSelectedListener() {
                    @Override
                    public void onSelected(@NotNull List<? extends Uri> uriList) {
                        try {
                            for (int i = 0; i < uriList.size(); i++) {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriList.get(i));
                                //Mat src = new Mat();
                                //Utils.bitmapToMat(bitmap,src);
                                Frame frame = new Frame(i, bitmap);
                                addFrame(frame);
                            }
                            FabToast.makeText(context, getFrameList().size() + "Pictures Uploaded.!", FabToast.LENGTH_LONG, FabToast.SUCCESS, FabToast.POSITION_CENTER).show();
                            String text = RecognizeImage();
                            narrator.playAudio(text);
                        } catch (IOException e) {
                            e.printStackTrace();

                        }

                    }
                });

    }


//    void DetectSkin(){
//        //H,S,V area
//        Mat hsv=new Mat();
//        Scalar lower = new Scalar(0, 135, 85);
//        Scalar upper = new Scalar(255,180, 135);
//        for (Frame frame : frameList) {
//            Bitmap bmp32 = frame.getImage().copy(Bitmap.Config.ARGB_8888, true);
//            Utils.bitmapToMat(bmp32,hsv);
//        Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_RGB2YCrCb);
//
//        Mat result = new Mat(hsv.rows(), hsv.cols(), CvType.CV_8U);
//        Core.inRange(hsv, lower, upper, result);
//        // Perform and decrease noise
//        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10));
//        Imgproc.filter2D(kernel,kernel,-1,kernel);
//        Imgproc.erode(result, result, kernel);
//        Imgproc.dilate(result, result, kernel);
//        Imgproc.GaussianBlur(result, result, new Size(11,11), 0);
//        Imgproc.medianBlur(result,result,15);
//        // Output
////        Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2RGB);
////        Imgproc.cvtColor(result, result, Imgproc.COLOR_RGB2RGBA);
//        Bitmap bitmap=Bitmap.createBitmap(bmp32);
//        Utils.matToBitmap(result,bitmap);
//        imageView.setImageBitmap(bitmap);
//        }
//
//    }

    String word = "";

    String RecognizeImage() {
        for (Frame frame : frameList) {
            frame.getSignAnalyzer().setPreprocessor(frame);
            //Mat gray=frame.getImage();
            //bitmap=Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
            //Utils.matToBitmap(gray,bitmap);
            bitmap = frame.getImage();
            bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
            final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
            Log.e("TAG", "Classified");
            Log.e("TAG", word);
            if (results.get(0).getTitle() == "SPACE") {
                word += " ";
            } else if (results.get(0).getTitle() == "DEL") {
                word = word.substring(0, word.length() - 1);
            } else if (results.get(0).getTitle() == "NOTHING") {
                Toast.makeText(context, "No hand detected", Toast.LENGTH_SHORT).show();
            } else {
                word += results.get(0).getTitle();

            }


            if (results == null) {
                Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
            } else {
//                        imageViewResult.setImageBitmap(bitmap);
                Toast.makeText(context, word, Toast.LENGTH_LONG).show();
            }

        }
        return word.toLowerCase();

    }


}
