package com.example.awaaz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.awaaz.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {
    TextView welcometext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PaperOnboardingPage page = new PaperOnboardingPage("\n\nLIVE PREVIEW", "\nNow you Know the meaning of the signs with a live preview", Color.parseColor("#F7F9F9"), R.drawable.onboard1, R.drawable.onboard1);
        PaperOnboardingPage page1 = new PaperOnboardingPage("HELP WHO NEEDS", "A life may depend on a gesture from you, a bottle of Blood.!", Color.parseColor("#F7F9F9"), R.drawable.onboard2, R.drawable.onboard2);
        PaperOnboardingPage page2 = new PaperOnboardingPage("FRONT FACING", "Now You can record your own signs by Double tapping on camera view.!", Color.parseColor("#F7F9F9"), R.drawable.frontface_1, R.drawable.frontface_1);
        ArrayList<PaperOnboardingPage> arrayList = new ArrayList<>();
        arrayList.add(page);
        arrayList.add(page1);
        arrayList.add(page2);
        PaperOnboardingFragment onboardingFragment = PaperOnboardingFragment.newInstance(arrayList);
        FragmentTransaction ft;
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.con, onboardingFragment);
        ft.commit();
        onboardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {

                setContentView(R.layout.welcome_layout);
                welcometext = findViewById(R.id.Welcome_text);
                SharedPreferences pref = getSharedPreferences("users", MODE_PRIVATE);
                String ee = pref.getString("name", "");
                if (ee.equals("")) {
                    welcometext.setText("Welcome");
                } else {
                    welcometext.setText("Welcome " + ee);
                }
            }
        });

    }

    public void continue_click(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
