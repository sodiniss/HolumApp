package com.example.holum.holum4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    BluetoothStateListener bsl;
    Button t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView)findViewById(R.id.t_holum);
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/msyi.ttf");
        t1 = (Button)findViewById(R.id.b_connessione);
        t2 = (Button)findViewById(R.id.b_controlli);
        t2.setVisibility(View.INVISIBLE);
        t.setTypeface(font);
        BluetoothAdapter.getDefaultAdapter().enable();
        String ss = String.valueOf(BluetoothAdapter.STATE_CONNECTED);
    }


    @Override
    protected void onResume() {
        bsl = new BluetoothStateListener();
        bsl.checkState(this);
        registerReceiver(bsl, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(bsl);
        super.onPause();
    }

    public void LaunchConnessione(View v){
        Intent intent = new Intent("com.example.holum.holum4.Connessione");
        startActivity(intent);
    }
    public void LaunchControlli(View v){
        Intent intent = new Intent("com.example.holum.holum4.Controlli");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        if(BTAdapter.isEnabled())
            BTAdapter.disable();
        finish();
        return;
    }

    @Override
    protected void onDestroy() {
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        super.onDestroy();

        BTAdapter.disable();
    }

    public void stateOn(){
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.INVISIBLE);

    }
    public void stateOff(){
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.INVISIBLE);
    }
    public void stateConnected(){
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.VISIBLE);

    }
}
