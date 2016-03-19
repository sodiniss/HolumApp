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

import java.io.IOException;
import java.util.UUID;


public class Connessione extends Activity {

    //grafica
    Button b_annullaScansione,b_iniziaScansione; // bottone annulla o inizia ricerca
    TextView text;
    ListView elencoDiscover; // dove vengono elencati i dispositivi nelle vicinanze
    ArrayAdapter<String> BTArrayAdapter; //array di string ---> per listview
    //bt
    BluetoothAdapter BTAdapter; //identifica l'hardware bluetooth del dispositivo
    BluetoothDevice device; //identifica il dispositivo con bluetooth attivo
    //static
    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // è un identificativo del servizio attivo. identifica una certa applicazione bluetooth
    public static int REQUEST_BLUETOOTH = 1; // richiesta per la startactivityforresult
    //service binding
    BluetoothService bts;

    BluetoothSocket tmp, socket;
    boolean mBound = false;
    IntentFilter state = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    IntentFilter extrastate = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
    IntentFilter discoveringstate = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    IntentFilter notdiscoveringstate = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    IntentFilter connected = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
    IntentFilter disconnected = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connessione);
        //definizioni
        elencoDiscover = (ListView) findViewById(R.id.l_discover);
        text = (TextView) findViewById(R.id.t_c_1);
        b_annullaScansione = (Button) findViewById(R.id.b_annullaScansione);
        b_iniziaScansione = (Button) findViewById(R.id.b_iniziaScansione);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();  //funzione che restituisce l'adapter del dispositivo
        BTArrayAdapter = new ArrayAdapter<>(Connessione.this, android.R.layout.simple_list_item_1);
        elencoDiscover.setAdapter(BTArrayAdapter);
        elencoDiscover.setOnItemClickListener(listener);
        registerReceiver(ActionFoundReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(isDiscoveringReceiver,
                new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(isNotDiscoveringReceiver,
                new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));



    }


    public void alertRequest(){
        hideUI();
        new AlertDialog.Builder(Connessione.this)    //allora crea dialogbox
                .setTitle("Avviso")
                .setMessage("L'applicazione richiede i permessi per l'utilizzo del Bluetooth")
                .setCancelable(false)
                .setPositiveButton("Concedi", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        BTAdapter.enable();

                        displayUI();
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
                                        finish();
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
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
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
            /*Bundle b = new Bundle();
            b.putParcelable("myBTdevice", device);
            Intent intent =  new Intent(Connessione.this, BluetoothService.class);
            intent.putExtras(b);
            startService(intent);*/
            bts.setNewDevice(device);




        }
    };


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        BTArrayAdapter.clear();
        unregisterReceiver(ActionFoundReceiver);
        unregisterReceiver(isDiscoveringReceiver);
        unregisterReceiver(isNotDiscoveringReceiver);

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
        if(mBound){
            bts.getBSL().checkState(Connessione.this);
            registerReceiver(bts.getBSL(), state);
            registerReceiver(bts.getBSL(), connected);
            registerReceiver(bts.getBSL(), disconnected);
        }
        //registerReceiver(bsl, discoveringstate);
       // registerReceiver(bsl, notdiscoveringstate);


        super.onResume();
    }

    @Override
    protected void onPause() {
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
            registerReceiver(bts.getBSL(), state);
            registerReceiver(bts.getBSL(), connected);
            registerReceiver(bts.getBSL(), disconnected);
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
        displayUI();
    }
    public void stateConnected(){
        startActivity(new Intent("com.example.holum.holum4.Controlli"));
    }
}