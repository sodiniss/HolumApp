/*package com.example.holum.holum4;

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
    private final int disconnected = BluetoothAdapter.STATE_DISCONNECTED;
    private final int connecting = BluetoothAdapter.STATE_CONNECTING;
    private final int disconnecting = BluetoothAdapter.STATE_DISCONNECTING;
    private final int turningon = BluetoothAdapter.STATE_TURNING_ON;
    private final int turningoff = BluetoothAdapter.STATE_TURNING_OFF;
    final String extraChanged = BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED;
    private static int state = 0;
    private static int extraState = 0;
    private String currentAction;
    Intent intent;


    @Override
    public void onReceive(Context context, Intent intent){
    currentAction = intent.getAction();
        if (currentAction == extraChanged){

            state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE,-1);

        }else{
            if(state==0)
                state = BluetoothAdapter.getDefaultAdapter().getState();
        }

        checkState(context);



    }

    public void checkState(Context context){
        if(state==0)
            state = BluetoothAdapter.getDefaultAdapter().getState();
        if(extraState == disconnecting){ extraState = disconnected;};
        if(extraState == connecting){extraState = connected;};
        if(state == turningoff){ state = off;};
        if(state == turningon){ state = on;};
        Toast.makeText(context,"extrastate = " +extraState, Toast.LENGTH_SHORT).show();
        Toast.makeText(context,"state = "+state, Toast.LENGTH_SHORT).show();
        String contextClass = context.getClass().getSimpleName();
        switch (contextClass){
            case "MainActivity":
                MainActivity m = (MainActivity)context;
                switch(extraState){
                    case connected:
                        m.stateConnected();
                        break;
                    case disconnected:
                        m.stateDisconnected();
                        break;

                    default:

                }
                break;
            case "Connessione":
                Connessione cc = (Connessione)context;
                switch(state+extraState){
                    case (off+disconnected):
                        cc.stateOff();
                        break;
                    case (on+connected):
                        cc.stateConnected();
                        break;
                    case (on+disconnected):
                        cc.stateDisconnected();
                        break;

                    default:

                }
                break;
            case "Controlli":
                Controlli c = (Controlli)context;
                switch(state){
                    case connected:
                        break;
                    case on:
                        break;
                    case off:
                        break;

                    default:

                }
                break;


        }



    }

    static void setConnectedState(){
        extraState = 2;
    }


}
*/
package com.example.holum.holum4;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

public class BluetoothStateListener extends BroadcastReceiver {
    private final int on = BluetoothAdapter.STATE_ON;
    private final int off = BluetoothAdapter.STATE_OFF;
    private final int connected = BluetoothAdapter.STATE_CONNECTED;
    private final int disconnected = BluetoothAdapter.STATE_DISCONNECTED;
    private final int connecting = BluetoothAdapter.STATE_CONNECTING;
    private final int disconnecting = BluetoothAdapter.STATE_DISCONNECTING;
    private final int turningon = BluetoothAdapter.STATE_TURNING_ON;
    private final int turningoff = BluetoothAdapter.STATE_TURNING_OFF;
    private final String actionState = BluetoothAdapter.ACTION_STATE_CHANGED;
    private final String actionConnected = BluetoothDevice.ACTION_ACL_CONNECTED;
    private final String actionDisconnected = BluetoothDevice.ACTION_ACL_DISCONNECTED;
    private final String actionDiscovering = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
    private final String actionNotDiscovering = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
    int state = -1;
    int extrastate = -1;
    String ss,sss;
    String currentAction;
    Intent intent;
    @Override
    public void onReceive(Context context, Intent intent) {
        currentAction = intent.getAction();
        switch (currentAction){
            case actionState:
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
                break;
            case actionConnected:
                extrastate = connected;
                state = on;
                break;
            case actionDisconnected:
                extrastate = disconnected;
                state = off;
                break;

            default:

        }

        checkState(context);
    }

    public void checkState(Context context){
        state = BluetoothAdapter.getDefaultAdapter().getState();


        if(extrastate == disconnecting){ extrastate = disconnected;};
        if(extrastate == connecting){extrastate = connected;};
        if(state == turningoff){ state = off;};
        if(state == turningon){ state = on;};
        if(state == off ){
            extrastate = disconnected;
        }
        if(extrastate == connected) {ss = "Connesso";}
        if(extrastate == disconnected) {ss = "Disconnesso";}
        if(state == on) {sss = "ON";}
        if(state == off) {sss = "OFF";}



        //Toast.makeText(context,"extrastate = " + ss+" / state = " + sss, Toast.LENGTH_SHORT).show();
        String contextClass = context.getClass().getSimpleName();
        switch (contextClass){
            case "MainActivity":
                MainActivity m = (MainActivity)context;
                switch(extrastate){
                    case connected:
                        m.stateConnected();
                        break;
                    case disconnected:
                        m.stateDisconnected();
                        break;

                    default:

                }
                break;
            case "Connessione":
                Connessione cc = (Connessione)context;
                //isdiscovering
                switch(state){
                    case on:
                        cc.stateOn();
                        if(extrastate==connected){
                            cc.stateConnected();
                        }
                        break;
                    case off:
                        cc.stateOff();
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