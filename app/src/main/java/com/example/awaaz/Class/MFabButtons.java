package com.example.awaaz.Class;

import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.fluidslider.FluidSlider;

public class MFabButtons {
    private FloatingActionButton fab_button_1, fab_button_2, fab_button_3, fab_button_4;
    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    private int fabTranslationY = 100;
    private boolean isFabMenuOpen = false;
    private int speed = 250;
    private Context context;
    FluidSlider slider;

    public MFabButtons(Context context, FloatingActionButton fab_button_1, FloatingActionButton fab_button_2, FloatingActionButton fab_button_3, FloatingActionButton fab_button_4, FluidSlider slider) {
        this.fab_button_1 = fab_button_1;
        this.fab_button_2 = fab_button_2;
        this.fab_button_3 = fab_button_3;
        this.fab_button_4 = fab_button_4;
        this.slider = slider;
        this.context = context;
        setFabTranslationY();
        setFabClicks();
    }

    private void setFabTranslationY() {
        fab_button_2.setAlpha(0f);
        fab_button_3.setAlpha(0f);
        fab_button_4.setAlpha(0f);

        fab_button_2.setTranslationY(fabTranslationY);
        fab_button_3.setTranslationY(fabTranslationY + 50);
        fab_button_4.setTranslationY(fabTranslationY + 100);
    }

    Boolean fab1, fab2, fab3, fab4 = false;

    public void setFabClicks() {
        fab_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
//                new Activity
//                context.startActivity(new Intent(context, YourActivity));
            }
        });

        fab_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
            }
        });

        fab_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
            }
        });

        fab_button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
                if (fab4 == false) {
                    slider.setVisibility(View.VISIBLE);
                    fab4 = true;
                } else {
                    slider.setVisibility(View.INVISIBLE);
                    fab4 = false;
                }
            }
        });
    }

    private void fabMenuOpen() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(true);
        fabOpenAnimation(fab_button_2);
        fabOpenAnimation(fab_button_3);
        fabOpenAnimation(fab_button_4);
    }

    private void fabMenuClose() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(false);
        fabCloseAnimation(fab_button_2);
        fabCloseAnimation(fab_button_3);
        fabCloseAnimation(fab_button_4);
    }

    private void fabCloseAnimation(FloatingActionButton fab) {
        fab.animate().translationY(fabTranslationY).alpha(0f).setInterpolator(overshootInterpolator).setDuration(speed).start();
    }

    private void fabOpenAnimation(FloatingActionButton fab) {
        fab.animate().translationY(0f).alpha(1f).setInterpolator(overshootInterpolator).setDuration(speed + 200).start();
    }

    private void fabMainAnimation(boolean isOpen) {
        if (isOpen) {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(45f).setDuration(speed).start();
        } else {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(0f).setDuration(speed).start();
        }
    }

    private void menuCheck() {
        if (isFabMenuOpen) {
            fabMenuClose();
        } else {
            fabMenuOpen();
        }
    }
}
