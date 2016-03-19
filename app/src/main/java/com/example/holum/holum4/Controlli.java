package com.example.holum.holum4;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class Controlli extends Activity {

    int message;
    BluetoothService bts;
    boolean mBound = false;
    Touchpad tp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlli2);
        tp = (Touchpad)findViewById(R.id.t_pad);

        tp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(tp.getMessage()){

                    case 2:
                        Up();
                        break;
                    case 1:
                        Left();
                        break;
                    case 4:
                        Down();
                        break;
                    case 3:
                        Right();
                        break;
                }
                return false;
            }
        });



    }


    //metodi onclick dei bottoni
    //passano alla funzione writeint il valore che identifica la direzione (specificata nel server)
    public void Left() {
        message = 1;
        bts.write(message);
    }
    public void Up() {
        message = 2;
        bts.write(message);
    }
    public void Right() {
        message = 3;
        bts.write(message);
    }
    public void Down() {
        message = 4;
        bts.write(message);
    }
    public void Enter() {
        message = 5;
        bts.write(message);
    }

    @Override
    protected void onStart() {

        super.onStart();
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);



    }
    @Override
    protected void onStop() {
       // bts.closeStream();
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }



    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            bts = binder.getService();
            mBound = true;
            bts.streamSetup();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));

    }
}
