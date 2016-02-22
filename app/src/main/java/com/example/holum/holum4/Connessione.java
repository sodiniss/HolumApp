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
import java.util.UUID;


public class Connessione extends Activity {

    //grafica
    Button button; // bottone annulla o inizia ricerca
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
    boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connessione);
        //definizioni
        elencoDiscover = (ListView) findViewById(R.id.l_discover);
        text = (TextView) findViewById(R.id.t_c_1);
        button = (Button) findViewById(R.id.b_c_1);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();  //funzione che restituisce l'adapter del dispositivo
        BTArrayAdapter = new ArrayAdapter<>(Connessione.this, android.R.layout.simple_list_item_1);
        elencoDiscover.setAdapter(BTArrayAdapter);
        elencoDiscover.setOnItemClickListener(listener);
        registerReceiver(ActionFoundReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));


        hideUI();
        checkBluetooth(BTAdapter);


    }

    public void checkBluetooth(BluetoothAdapter BTAdapter){
        if (BTAdapter == null) {
            //non esiste bluetooth
            Toast.makeText(Connessione.this, "Non esiste il Bluetooth su questo dispositivo", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!BTAdapter.isEnabled()) {        //se non è attivo

            //richiedi permessi per accenderlo
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH); //inizia activity e attendi un result ---> onActivityResult
        } else {
            // se bt attivo allora inizia subito la ricerca
            displayUI();
            BTAdapter.startDiscovery();
        }
    }

    public void bottoneRicerca(View v) {

        if (BTAdapter.isDiscovering()) {   // al click stoppa discovery
            BTAdapter.cancelDiscovery();
            button.setText("INIZIA RICERCA");
        } else { //al click inizia discovery
            BTArrayAdapter.clear();
            BTAdapter.startDiscovery();
            button.setText("ANNULLA RICERCA");
        }

    }

    public void displayUI() {
        button.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        elencoDiscover.setVisibility(View.VISIBLE);
    }
    public void hideUI(){
        button.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        elencoDiscover.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //metodo autogenerato per gestire il risultato della startactivityforresult
        if (requestCode == REQUEST_BLUETOOTH) {    // controlla il tipo di richiesta
            if (resultCode != RESULT_OK) {       // se bluetooth non concesso
                //richiesta rifiutata
                new AlertDialog.Builder(this)    //allora crea dialogbox
                        .setTitle("Avviso")
                        .setMessage("Bluetooth negato.Vuoi uscire?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
                            }
                        })

                        .show();

            } else {        //se concesso bt
                displayUI();
                BTArrayAdapter.clear();  // funzione per svuotare l'array
                BTAdapter.startDiscovery(); // inizia la ricerca
            }
        }
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


    OnItemClickListener listener = new OnItemClickListener() {    //metodo della listview che permette di gestire il click su un elemento dell'array
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {


            String address = (String)parent.getItemAtPosition(position);                //salva string da arrayadapter
            address = address.substring(address.length() - 17);                           //salva solamente il mac address
            device = BTAdapter.getRemoteDevice(address);                                //seleziona device con quell'address
            BTAdapter.cancelDiscovery();
            startService(new Intent(Connessione.this,BluetoothService.class));
            bts.connectionSetup(device);


        }
    };


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(ActionFoundReceiver);
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


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            bts = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}