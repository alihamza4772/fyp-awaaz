package com.example.awaaz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.awaaz.R;

import spencerstudios.com.fab_toast.FabToast;

public class SplashScreen extends AppCompatActivity {
    EditText edtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 3000);
    }

    void next() {
        SharedPreferences pref = getSharedPreferences("users", MODE_PRIVATE);
        String ee = pref.getString("login", "notdone");
        if (ee.equals("done")) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            setContentView(R.layout.enter_name_layout);
            edtname = findViewById(R.id.edt_enter_name);
        }
    }

    public void name_click_next(View view) {
        if (edtname.getText().toString().equals("")) {
            FabToast.makeText(SplashScreen.this, "Kindly Enter your First Name.!", FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
        } else if (edtname.length() < 3) {
            FabToast.makeText(SplashScreen.this, "Enter your First Name.!", FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
        } else if (edtname.length() > 20) {
            FabToast.makeText(SplashScreen.this, "Enter your First Name Only.!", FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
        } else {
            SharedPreferences.Editor preferences = getSharedPreferences("users", MODE_PRIVATE).edit();
            preferences.putString("login", "done");
            preferences.putString("name", edtname.getText().toString());
            preferences.apply();
            Intent intent = new Intent(this, OnBoardingActivity.class); // To move to on Boarding activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
