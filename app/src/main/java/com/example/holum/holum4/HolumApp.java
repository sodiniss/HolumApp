package com.example.holum.holum4;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

/**
 * Created by Vittorio on 17/02/2016.
 */
//classe con var globale btsocket
    //sarà da sostituire con qualcosa di più efficente
public class HolumApp extends Application{
    private BluetoothSocket BTSocket;

    public BluetoothSocket getBTSocket() {
        return BTSocket;
    }

    public void setBTSocket(BluetoothSocket BTSocket) {
        this.BTSocket = BTSocket;
    }

}
