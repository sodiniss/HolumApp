package com.example.holum.holum4;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class Connessione extends Activity {
    BluetoothAdapter BTAdapter;
    public static int REQUEST_BLUETOOTH = 1; // richiesta per la startactivityforresult
    ArrayAdapter<BluetoothDevice> BTArrayAdapter;
    TextView text;
    ListView elencoDiscover;
    Button button;

    BluetoothSocket BTSocket;
    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public Context context = getApplicationContext();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connessione);
        elencoDiscover = (ListView) findViewById(R.id.l_discover);
        text = (TextView) findViewById(R.id.t_c_1);
        button = (Button) findViewById(R.id.b_c_1);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        BTArrayAdapter = new ArrayAdapter<BluetoothDevice>(Connessione.this, android.R.layout.simple_list_item_1);
        elencoDiscover.setAdapter(BTArrayAdapter);
        elencoDiscover.setOnItemClickListener(listener);


        if (!BTAdapter.isEnabled()) {
            button.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            elencoDiscover.setVisibility(View.INVISIBLE);
        } else {
            BTAdapter.startDiscovery();
        }


        registerReceiver(ActionFoundReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));

        if (BTAdapter == null) {
            //non esiste bluetooth
        }
        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }


    }

    public void gestisciRicerca(View v) {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BLUETOOTH) {
            if (resultCode != RESULT_OK) {
                //richiesta rifiutata
                new AlertDialog.Builder(this)
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

            } else {
                displayUI();
                BTArrayAdapter.clear();
                BTAdapter.startDiscovery();
            }
        }
    }

    // ricerca
    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTArrayAdapter.add(device);
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };


    OnItemClickListener listener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            //connectWithServer((BluetoothDevice) parent.getItemAtPosition(position));
            ConnectThread connessione = new ConnectThread((BluetoothDevice) parent.getItemAtPosition(position));
            connessione.start();
        }
    };

    /*
    public void connectWithServer(BluetoothDevice device){

        BTAdapter.cancelDiscovery();
        try{
            BTSocket = device.createRfcommSocketToServiceRecord(uuid);
            BTSocket.connect();
            if(BTSocket.isConnected())
                Toast.makeText(Connessione.this,"preso",  Toast.LENGTH_SHORT).show();


        }catch(IOException e){}

    }
*/


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(ActionFoundReceiver);
    }
}
