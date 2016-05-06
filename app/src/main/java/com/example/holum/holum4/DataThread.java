package com.example.holum.holum4;

import android.bluetooth.*;
import java.io.*;


public class DataThread extends Thread {
    private  BluetoothSocket mmSocket;
    private  OutputStream mmOutStream;

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

    private void resetConnection() {


        if (mmOutStream != null) {
            try {mmOutStream.close();} catch (Exception e) {}
            mmOutStream = null;
        }

        if (mmSocket != null) {
            try {mmSocket.close();} catch (Exception e) {}
            mmSocket = null;
        }

    }
    public void writeInt(int i){
        try {

            mmOutStream.write(i);
        } catch (IOException e) {
        }

    }
    // da chiamare per chiudere la connessione
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
