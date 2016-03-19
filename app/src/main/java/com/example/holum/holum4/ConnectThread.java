package com.example.holum.holum4;

import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import java.io.*;
import java.util.UUID;




public class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private Context secondContext;



    public ConnectThread(BluetoothDevice device,Context context) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        secondContext = context;
        try {
            tmp = device.createRfcommSocketToServiceRecord(Connessione.uuid);



        } catch (IOException e) { }
        mmSocket = tmp;

    }


    public void run() {

        try {

            mmSocket.connect();




        } catch (IOException connectException) {

            try {
                mmSocket.close();
            } catch (IOException closeException) { }

        }


    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    public BluetoothSocket getBTSocket() {
        return mmSocket;
    }



}