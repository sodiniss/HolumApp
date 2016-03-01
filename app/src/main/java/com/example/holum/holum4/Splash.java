package com.example.holum.holum4;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.app.Activity;
import android.content.*;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Splash extends Activity {
    boolean done = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secDelay = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(!done){
                    startActivity(new Intent(Splash.this, MainActivity.class));

                }
                finish();
            }
        }, secDelay * 1000);
    }

    @Override
    public void onBackPressed() {
        done = true;
        finish();
    }
}