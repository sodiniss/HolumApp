
package com.example.holum.holum4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.NotificationManagerCompat;
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
            case actionConnected:
                extrastate = disconnected;
                if(context.getClass().getSimpleName()!="MainActivity"){
                    extrastate = connected;
                }
                break;
            case actionDisconnected:
                extrastate = disconnected;
                break;

            default:

        }

        checkState(context);
    }

    public void checkState(Context context){
        state = BluetoothAdapter.getDefaultAdapter().getState();

        if(extrastate == disconnecting){ extrastate = disconnected;};
        //if(extrastate == connecting){extrastate = connected;};
        //if(state == turningoff){ state = off;};                   per alertrequest
        if(state == turningon){ state = on;};
        if(state == off || state == turningoff){
            extrastate = disconnected;
        }
        if(extrastate == -1){extrastate = disconnected;};
        //
        //if(state == on){extrastate = disconnected;}        da risolvere
        if(extrastate == connected) {ss = "Connesso";}
        if(extrastate == disconnected) {ss = "Disconnesso";}
        if(state == on) {sss = "ON";}
        if(state == off) {sss = "OFF";}
        if(state == turningoff) {sss = "TURNING-OFF";}

        NotificationCompat.Builder builderNotifica = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.pyramid2)
                .setContentTitle("Servizio Holum attivo")
                .setContentText("Tocca per tornare all'app");

        int idNotifica = 001;
        Intent resultIntent = new Intent("com.example.holum.holum4.Controlli" );
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builderNotifica.setContentIntent(resultPendingIntent);
        NotificationManager managerNotifica = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(extrastate == connected){
            managerNotifica.notify(idNotifica, builderNotifica.build());
        }
        if(extrastate == disconnected){
            managerNotifica.cancel(idNotifica);
        }
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
                        }else{
                            cc.stateDisconnected();
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
                switch(extrastate){
                    case connected:
                        c.stateConnected();
                        break;
                    case disconnected:
                        c.stateDisconnected();
                        break;
                    default:

                }
                break;


        }



    }

}