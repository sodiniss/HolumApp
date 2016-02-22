package com.example.holum.holum4;

import android.bluetooth.*;
import java.io.*;


public class DataThread implements Runnable {
    private final BluetoothSocket mmSocket;
    private final OutputStream mmOutStream;

    public DataThread(BluetoothSocket socket) {
        mmSocket = socket;
        OutputStream tmpOut = null;

        try {

            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }


        mmOutStream = tmpOut;
    }

    public void run() {

    }


    public void writeInt(int i){
        try {
            mmOutStream.write(i);
        } catch (IOException e) { }

    }
    // da chiamare per chiudere la connessione
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
