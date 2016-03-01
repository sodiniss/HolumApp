package com.example.holum.holum4;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

public class BluetoothStateListener extends BroadcastReceiver {
    private final int on = BluetoothAdapter.STATE_ON;
    private final int off = BluetoothAdapter.STATE_OFF;
    private final int connected = BluetoothAdapter.STATE_CONNECTED;
    Intent intent;
    @Override
    public void onReceive(Context context, Intent intent) {
        /*int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                -1);*/
        checkState(context);
    }

    public void checkState(Context context){
        int state = BluetoothAdapter.getDefaultAdapter().getState();

        String contextClass = context.getClass().getSimpleName();
        switch (contextClass){
            case "MainActivity":
                MainActivity m = (MainActivity)context;
                switch(state){
                    case on:
                        m.stateOn();

                        break;
                    case off:
                        m.stateOff();
                        break;
                    case connected:
                        m.stateConnected();
                        break;
                    default:

                }
                break;
            case "Connessione":
                Connessione cc = (Connessione)context;
                switch(state){
                    case on:
                        cc.stateOn();
                        break;
                    case off:
                        cc.stateOff();
                        break;
                    case connected:
                        cc.stateConnected();
                        break;
                    default:

                }
                break;
            case "Controlli":
                Controlli c = (Controlli)context;
                switch(state){
                    case on:
                        break;
                    case off:
                        break;
                    case connected:
                        break;
                    default:

                }
                break;


        }



    }

}
