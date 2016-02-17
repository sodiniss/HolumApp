package com.example.holum.holum4;

import android.bluetooth.*;
import android.content.Context;
import android.widget.Toast;
import java.io.*;
import java.util.UUID;




public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final  BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public DataThread dati;

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(Connessione.uuid);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // stop discovery
        mBluetoothAdapter.cancelDiscovery();

        try {
            mmSocket.connect();

        } catch (IOException connectException) {
            // ?? errore di connessione
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // la connessione si gestisce in un altro thread
       // dati = new DataThread(mmSocket);
        //dati.start();
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}