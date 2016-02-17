package com.example.holum.holum4;

import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.app.Activity;
import android.content.*;
import android.view.*;

public class Splash extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secDelay = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        }, secDelay * 1000);
    }
}