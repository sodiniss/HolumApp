package com.example.holum.holum4;

import android.bluetooth.*;
import java.io.*;


public class DataThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public DataThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {

                bytes = mmInStream.read(buffer);

            } catch (IOException e) {
                break;
            }
        }
    }

    // da chiamare per inviare dati
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    // da chiamare per chiudere la connessione
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
