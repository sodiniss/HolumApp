package com.example.holum.holum4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
