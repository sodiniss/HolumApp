package com.example.holum.holum4;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


public class Connessione extends Activity {

    public static final UUID uuid = UUID.fromString("00001105-0000-1000-8000-00805f9b34fb");

    //grafica
    Button b_annullaScansione,b_iniziaScansione;
    TextView text;
    ListView elencoDiscover;
    ArrayAdapter<String> BTArrayAdapter;
    AlertDialog ab;
    BluetoothAdapter BTAdapter;
    BluetoothDevice device;
    BluetoothService bts;
    boolean mBound = false;
    boolean alertIsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connessione);

        elencoDiscover = (ListView) findViewById(R.id.l_discover);
        text = (TextView) findViewById(R.id.t_c_1);
        b_annullaScansione = (Button) findViewById(R.id.b_annullaScansione);
        b_iniziaScansione = (Button) findViewById(R.id.b_iniziaScansione);

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        BTArrayAdapter = new ArrayAdapter<>(Connessione.this, android.R.layout.simple_list_item_1);
        elencoDiscover.setAdapter(BTArrayAdapter);
        elencoDiscover.setOnItemClickListener(listener);




    }


    public void alertRequest(){
        hideUI();
        alertIsOn = true;
        ab = new AlertDialog.Builder(Connessione.this)    //allora crea dialogbox
                .setTitle("Avviso")
                .setMessage("L'applicazione richiede i permessi per l'utilizzo del Bluetooth")
                .setCancelable(false)
                .setPositiveButton("Concedi", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        BTAdapter.enable();
                        alertIsOn = false;
                        //displayUI();
                    }

                })
                .setNegativeButton("Nega", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(Connessione.this)    //allora crea dialogbox
                                .setTitle("Avviso")
                                .setMessage("Bluetooth negato.Vuoi uscire?")
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertIsOn = false;
                                        startActivity(new Intent(Connessione.this,MainActivity.class));
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertRequest();
                                    }
                                })

                                .show();
                    }
                })
                .show();
    }

    public void iniziaScansione(View v) {

        BTAdapter.startDiscovery();

    }
    public void annullaScansione(View v){

        BTAdapter.cancelDiscovery();
    }

    public void displayUI() {
        if(BTAdapter.isEnabled()){
            BTAdapter.startDiscovery();
        }
        if(BTAdapter.isDiscovering()){
            b_annullaScansione.setVisibility(View.VISIBLE);
            b_iniziaScansione.setVisibility(View.INVISIBLE);
        }else{
            b_iniziaScansione.setVisibility(View.VISIBLE);
            b_annullaScansione.setVisibility(View.INVISIBLE);
        }
        text.setVisibility(View.VISIBLE);
        elencoDiscover.setVisibility(View.VISIBLE);
        ;
    }
    public void hideUI(){
        b_iniziaScansione.setVisibility(View.INVISIBLE);
        b_annullaScansione.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        elencoDiscover.setVisibility(View.INVISIBLE);
    }




    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {       //metodo brutto che aggiunge i dispositivi trovati all'array e gli visualizza

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };
    private final BroadcastReceiver isDiscoveringReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {       //metodo brutto che aggiunge i dispositivi trovati all'array e gli visualizza

            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                BTArrayAdapter.clear();

                    b_annullaScansione.setVisibility(View.VISIBLE);
                    b_iniziaScansione.setVisibility(View.INVISIBLE);


            }
        }
    };
    private final BroadcastReceiver isNotDiscoveringReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {       //metodo brutto che aggiunge i dispositivi trovati all'array e gli visualizza

            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                b_iniziaScansione.setVisibility(View.VISIBLE);
                b_annullaScansione.setVisibility(View.INVISIBLE);
            }
        }
    };

    OnItemClickListener listener = new OnItemClickListener() {    //metodo della listview che permette di gestire il click su un elemento dell'array
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {


            String address = (String)parent.getItemAtPosition(position);                //salva string da arrayadapter
            address = address.substring(address.length() - 17);                           //salva solamente il mac address
            device = BTAdapter.getRemoteDevice(address);                                //seleziona device con quell'address

            BTAdapter.cancelDiscovery();
            bts.setNewDevice(device);




        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    @Override
    protected void onResume() {

        registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(isDiscoveringReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(isNotDiscoveringReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        if(mBound){
            bts.getBSL().checkState(Connessione.this);
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        BTArrayAdapter.clear();
        BTAdapter.cancelDiscovery();
        unregisterReceiver(ActionFoundReceiver);
        unregisterReceiver(isDiscoveringReceiver);
        unregisterReceiver(isNotDiscoveringReceiver);
        unregisterReceiver(bts.getBSL());
        super.onPause();
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            bts = binder.getService();
            mBound = true;
            bts.getBSL().checkState(Connessione.this);
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    public void stateOff(){

        alertRequest();

    }
    public void stateOn(){
        if(alertIsOn)
        ab.cancel();    //elimina alert
        displayUI();
    }

    public void stateConnected(){
        startActivity(new Intent("com.example.holum.holum4.Controlli"));
    }
    public void stateDisconnected(){
        if(!BTAdapter.isDiscovering()){
            BTAdapter.startDiscovery();
        }
    }
}