package com.example.holum.holum4;

import android.bluetooth.*;
import android.content.Context;
import android.widget.Toast;
import java.io.*;
import java.util.UUID;




public class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private final  BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;


        try {
            tmp = device.createRfcommSocketToServiceRecord(Connessione.uuid);
            //STATE_CONNECTED

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