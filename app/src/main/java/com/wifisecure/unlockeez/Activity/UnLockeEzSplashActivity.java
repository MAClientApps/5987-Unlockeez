package com.wifisecure.unlockeez.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.wifisecure.unlockeez.R;
import com.wifisecure.unlockeez.UnLockeEzMainPageActivity;

@SuppressLint("CustomSplashScreen")
public class UnLockeEzSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_un_locke_ez);
        // Intent is used to switch from one activity to another.
        new Handler().postDelayed((Runnable) this::NextActivityFunction, 2000);
    }

    private void NextActivityFunction() {
        Intent i = new Intent(UnLockeEzSplashActivity.this, UnLockeEzMainPageActivity.class);
        startActivity(i);// invoke the SecondActivity.
        finish();// the current activity will get finished.
    }
}