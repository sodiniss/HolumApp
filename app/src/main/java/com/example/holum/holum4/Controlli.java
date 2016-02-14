package com.example.holum.holum4;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Controlli extends Activity {
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlli);
        t = (TextView)findViewById(R.id.t_prova);
    }

    public void Up(View v) { t.setText("Up");  }
    public void Down(View v){
        t.setText("Down");
    }
    public void Left(View v){
        t.setText("Left");
    }
    public void Right(View v){
        t.setText("Right");
    }
    public void Enter(View v){
        t.setText("");
    }

}
