package com.example.holum.holum4;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Vittorio on 19/02/2016.
 */
public class BluetoothService extends Service {
    DataThread dt;
    ConnectThread ct;
    BluetoothDevice device;
    BluetoothSocket BTSocket;

    private final IBinder mBinder = new LocalBinder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getExtras();
        device = b.getParcelable("myBTdevice");
        connectionSetup(device);


        Toast.makeText(this,"CONNESSOOOOOO"+BluetoothAdapter.getDefaultAdapter().,Toast.LENGTH_SHORT).show();



        return START_STICKY;
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
        Toast.makeText(BluetoothService.this, "STOPPED", Toast.LENGTH_SHORT).show();
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

        ct = new ConnectThread(device);
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

