package com.example.awaaz.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.camerakit.CameraKitView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.List;


public class LiveVideo {
    private int id;
    private boolean endLiveVideo;
    private Frame frame;
    private Context context;
    private CameraKitView cameraKitView;
    private int interval;
    private ImageView imageView;
    private Mode facing;
    private Classifier classifier;
    private static final String MODEL_PATH = "masked_alphabets.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 50;

    public enum Mode {
        back,
        front
    }

    public Mode getFacing() {
        return facing;
    }

    public void setFacing(Mode facing) {
        this.facing = facing;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getEndLiveVideo() {
        return endLiveVideo;
    }

    public void setEndLiveVideo(boolean endLiveVideo) {
        this.endLiveVideo = endLiveVideo;
    }

    public CameraKitView getCameraKitView() {
        return cameraKitView;
    }

    public void setCameraKitView(CameraKitView cameraKitView) {
        this.cameraKitView = cameraKitView;
    }

    public LiveVideo(int id, boolean endLiveVideo, Context context, CameraKitView cameraKitView, int interval, ImageView imageView) throws IOException {
        this.id = id;
        this.endLiveVideo = endLiveVideo;
        this.context = context;
        this.cameraKitView = cameraKitView;
        this.interval = interval;
        this.imageView = imageView;
        facing = Mode.back;
        getFrames();

        classifier = ImageClassifier.create(
                context.getAssets(),
                MODEL_PATH,
                LABEL_PATH,
                INPUT_SIZE,
                QUANT);
    }

    public void getFrames() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {

                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Mat src = new Mat();
                        Utils.bitmapToMat(bitmap, src);
                        frame = new Frame(1, bitmap);
                        frame.getSignAnalyzer().setPreprocessor(frame);
                        if (facing == Mode.front) {
                            src = frame.getSignAnalyzer().getPreprocessor().Mirror(src);
                        }
                        //src=frame.getSignAnalyzer().getPreprocessor().cropMat(src);
                        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2YCrCb);
                        Mat dst = new Mat();
                        Core.inRange(src, new Scalar(0, 138, 67), new Scalar(255, 173, 133), dst);
                        Mat dst1 = new Mat();
                        Mat strel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));
                        Imgproc.morphologyEx(dst, dst1, Imgproc.MORPH_OPEN, strel);
                        Imgproc.morphologyEx(dst, dst1, Imgproc.MORPH_CLOSE, strel);
                        Bitmap b = Bitmap.createBitmap(dst1.cols(), dst1.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dst1, b);
                        imageView.setImageBitmap(b);
                        Bitmap bit = Bitmap.createScaledBitmap(b, 50, 50, true);
                        final List<Classifier.Recognition> results = classifier.recognizeImage(bit);
                        Toast.makeText(context, "" + results.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
                handler.postDelayed(this, getInterval());
            }
        };
        handler.postDelayed(r, getInterval());
    }
}
