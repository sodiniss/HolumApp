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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class Controlli extends Activity {
    TextView t;
    int message;
    BluetoothService bts;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlli);
        t = (TextView)findViewById(R.id.t_prova);

    }


    //metodi onclick dei bottoni
    //passano alla funzione writeint il valore che identifica la direzione (specificata nel server)
    public void Left(View v) {
        message = 1;
        bts.write(message);
    }
    public void Up(View v) {
        message = 2;
        bts.write(message);
    }
    public void Right(View v) {
        message = 3;
        bts.write(message);
    }
    public void Down(View v) {
        message = 4;
        bts.write(message);
    }
    public void Enter(View v) {
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


}
