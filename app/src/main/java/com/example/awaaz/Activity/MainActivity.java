package com.example.awaaz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.example.awaaz.Class.LiveVideo;
import com.example.awaaz.Class.MFabButtons;
import com.example.awaaz.Class.MySurface;
import com.example.awaaz.Class.SLR;
import com.example.awaaz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.fluidslider.FluidSlider;

import org.opencv.android.OpenCVLoader;
import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.math.BigDecimal;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;
import spencerstudios.com.fab_toast.FabToast;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab1;
    Guideline guideline;
    private CameraKitView cameraKitView;
    MFabButtons mFabButtons;
    FloatingActionButton fab_button_1;
    public FluidSlider slider = null;
    FrameLayout frameLayout;
    ImageView imageView;
    MySurface mySurface;
    SLR slr;
    boolean check = true;
    GuideView mGuideView;
    GuideView.Builder builder;
    ImageView back;
    Interpreter tflite;

    public void back_click(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    enum mode {
        back,
        front
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySurface = new MySurface(this);
        frameLayout = findViewById(R.id.frameLayout);
        frameLayout.addView(mySurface);
        guideline = findViewById(R.id.guideline);
        imageView = findViewById(R.id.imgView);
        cameraKitView = findViewById(R.id.camera);
        slider = findViewById(R.id.fluidSlider);
        back = findViewById(R.id.backimgview);
        fab1 = findViewById(R.id.fab_button_1);
        slr = getIntent().getParcelableExtra("SLR");
        try {
            slr.setLiveVideo(new LiveVideo(1, true, this, cameraKitView, 2000, imageView));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setType();
        if (OpenCVLoader.initDebug()) {
            Log.e("Opencv", "Loaded");
        } else {
            FabToast.makeText(MainActivity.this, "Unable to attach OpenCV", FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
            try {
                Thread.sleep(2000);
                finish();
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setfluidSlider();

    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    void setfluidSlider() {
        final float max = 5;
        final float min = 2;
        final float total = max - min;
        slider.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        });

        slider.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        });
        slider.setPositionListener(pos -> {
            String value = String.valueOf(round(min + total * pos, 3));
            double temp = Double.parseDouble(value);
            temp = temp * 1000;
            slr.setInterval((int) temp);
            slider.setBubbleText(value);
            return Unit.INSTANCE;
        });


        slider.setPosition(0.3f);
        slider.setStartText(String.valueOf(min));
        slider.setEndText(String.valueOf(max));
        slider.setVisibility(View.INVISIBLE);

    }

    private void setType() {
        fab_button_1 = findViewById(R.id.fab_button_1);
        FloatingActionButton fab_button_2 = findViewById(R.id.fab_button_2);
        FloatingActionButton fab_button_3 = findViewById(R.id.fab_button_3);
        FloatingActionButton fab_button_4 = findViewById(R.id.fab_button_4);
        fab_button_1.hide();
        mFabButtons = new MFabButtons(MainActivity.this, fab_button_1, fab_button_2, fab_button_3, fab_button_4, slider);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
        cameraKitView.setGestureListener(new CameraKitView.GestureListener() {
            @Override
            public void onTap(CameraKitView ckV, float v, float v1) {

            }

            @Override
            public void onLongTap(CameraKitView cameraKitView, float v, float v1) {
                if (check) {
                    fab_button_1.show();
                    SharedPreferences pref = getSharedPreferences("users", MODE_PRIVATE);
                    String ee = pref.getString("login", "notdone");
                    if (ee.equals("done")) {
                        builder = new GuideView.Builder(MainActivity.this)
                                .setTitle("Step 1")
                                .setContentText("You should make sign in the Red box\n for accurate interpretation of Sign")
                                .setGravity(Gravity.center)
                                .setDismissType(DismissType.outside)
                                .setTargetView(frameLayout)
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {
                                        switch (view.getId()) {
                                            case R.id.frameLayout:
                                                builder.setTitle("Step 2").setContentText("Long press on screen to Enable Interval Options.!").setGravity(Gravity.center).setDismissType(DismissType.outside).setTargetView(guideline).build();
                                                break;
                                            case R.id.guideline:
                                                builder.setTitle("Step 3").setContentText("Click here for Further configuration.!").setGravity(Gravity.center).setDismissType(DismissType.outside).setTargetView(fab1).build();
                                                break;
                                            case R.id.fab_button_1:
                                                builder.setTitle("Step 4").setContentText("Return to the HomePage.!").setGravity(Gravity.center).setDismissType(DismissType.outside).setTargetView(back).build();
                                                break;
                                            case R.id.backimgview:
                                                return;
                                        }
                                        mGuideView = builder.build();
                                        mGuideView.show();
                                    }
                                });

                        mGuideView = builder.build();
                        mGuideView.show();

                    }
                    check = false;
                } else {
                    fab_button_1.hide();
                    check = true;
                }
            }

            @Override
            public void onDoubleTap(CameraKitView cameraKitView, float v, float v1) {
                if (cameraKitView.getFacing() == CameraKit.FACING_FRONT) {
                    cameraKitView.setFacing(CameraKit.FACING_BACK);
                    slr.getLiveVideo().setFacing(LiveVideo.Mode.back);
                } else {
                    cameraKitView.setFacing(CameraKit.FACING_FRONT);
                    slr.getLiveVideo().setFacing(LiveVideo.Mode.front);
                }
            }

            @Override
            public void onPinch(CameraKitView cameraKitView, float v, float v1, float v2) {

            }
        });
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
