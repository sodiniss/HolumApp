package com.example.holum.holum4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    BluetoothService bts;
    Button b_connessione,b_controlli;
    boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, BluetoothService.class));
        //UI
        TextView t = (TextView)findViewById(R.id.t_holum);
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/msyi.ttf");
        t.setTypeface(font);
        b_connessione = (Button)findViewById(R.id.b_connessione);
        b_controlli = (Button)findViewById(R.id.b_controlli);



    }
    //Metodi dei bottoni
    public void LaunchConnessione(View v){
        startActivity(new Intent("com.example.holum.holum4.Connessione"));
    }

    public void LaunchControlli(View v){
        startActivity(new Intent("com.example.holum.holum4.Controlli"));
    }

    //Metodi Listener
    public void stateConnected(){
        b_connessione.setVisibility(View.INVISIBLE);
        b_controlli.setVisibility(View.VISIBLE);

    }
    public void stateDisconnected(){
        b_controlli.setVisibility(View.INVISIBLE);
        b_connessione.setVisibility(View.VISIBLE);

    }
    //Metodi della activity
    @Override
    protected void onStart() {
        //startActivity
        super.onStart();
        bindService(new Intent(MainActivity.this, BluetoothService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        //indietro o startActivity
        if (mBound) {
            bts.getBSL().checkState(MainActivity.this);
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        //quando viene lanciata una nuova attività
        unregisterReceiver(bts.getBSL());
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        BluetoothAdapter.getDefaultAdapter().disable();
        stopService(new Intent(MainActivity.this, BluetoothService.class));
        System.exit(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothAdapter.getDefaultAdapter().disable();
    }

    //Metodi del service
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            bts = binder.getService();
            mBound = true;
            bts.getBSL().checkState(MainActivity.this);
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



}
