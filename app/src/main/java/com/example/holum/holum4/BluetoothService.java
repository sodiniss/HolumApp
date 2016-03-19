package com.example.holum.holum4;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by Vittorio on 19/02/2016.
 **/
public class BluetoothService extends Service {
    DataThread dt;
    ConnectThread ct;
    BluetoothStateListener bsl;
    BluetoothDevice device;
    BluetoothSocket BTSocket;

    private final IBinder mBinder = new LocalBinder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*Bundle b = intent.getExtras();
        device = b.getParcelable("myBTdevice");
        connectionSetup(device);*/
        //Toast.makeText(BluetoothService.this,"STARTED", Toast.LENGTH_SHORT).show();
        bsl = new BluetoothStateListener();
        return START_STICKY;
    }
    public void setNewDevice(BluetoothDevice selectedDevice){
        device = selectedDevice;
        connectionSetup(device);

    }


    public BluetoothStateListener getBSL(){
        return this.bsl;
    }

    @Override
    public void onCreate() {

        super.onCreate();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {

        //Toast.makeText(BluetoothService.this, "STOPPED", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        BluetoothService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BluetoothService.this;
        }
    }

    //funzioni del service

    public void connectionSetup(BluetoothDevice device){
        /*
        ct = new ConnectThread("ConnectThread");
        ct.setConnectThread(device);
        ct.start();
        Looper looper = ct.getLooper();
        handler = new Handler(looper);
        registerReceiver (connectionReceiver,  new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED), null, handler);
        try {
            ct.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        ct = new ConnectThread(device,this);
        Thread t1 = new Thread(ct);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void streamSetup(){
        if(ct.getBTSocket().isConnected()){
            dt = new DataThread(ct.getBTSocket());
            new Thread(dt).start();
        }

    }
    public void write(int message){
        dt.writeInt(message);
    }
    public void closeStream(){
        dt.cancel();
    }
    public void closeConnect(){
        ct.cancel();
    }


    public void stateOn(){}
    public void stateOff(){}
    public void stateConnected(){}
}

