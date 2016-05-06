package com.example.holum.holum4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
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
    GestureDetector gt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlli2);

        tp = (Touchpad)findViewById(R.id.t_pad);
        tp.setOnTouchListener(new OnSwipeListener() {
            public void onUpSwipe(float value) {

                bts.write(2);
            }

            public void onDownSwipe(float value) {
                bts.write(4);
            }

            public void onRightSwipe(float value) {
                bts.write(3);
            }

            public void onLeftSwipe(float value) {
                bts.write(1);
            }

        });


    }

    //metodi onclick dei bottoni
    //passano alla funzione writeint il valore che identifica la direzione (specificata nel server)

    @Override
    protected void onStart() {

        super.onStart();
        bindService(new Intent(this, BluetoothService.class), mConnection, Context.BIND_AUTO_CREATE);



    }
    @Override
    protected void onStop() {

        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    @Override
    protected void onResume() {
        //indietro o startActivity
        if (mBound) {
            bts.getBSL().checkState(this);
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(bts.getBSL());
        super.onPause();
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            bts = binder.getService();
            mBound = true;
            bts.streamSetup();
            bts.getBSL().checkState(Controlli.this);

            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));

    }

    public void stateConnected(){
        //Toast.makeText(this,"CONNESSO",Toast.LENGTH_SHORT).show();
    }
    public void stateDisconnected(){
        Toast.makeText(this,"DISCONNESSO",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,Connessione.class));
    }


}
