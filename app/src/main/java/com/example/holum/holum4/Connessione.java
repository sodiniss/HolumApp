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
import android.util.Log;
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
import java.io.Serializable;
import java.util.UUID;


public class Connessione extends Activity{
    BluetoothAdapter BTAdapter; //identifica l'hardware bluetooth del dispositivo
    public static int REQUEST_BLUETOOTH = 1; // richiesta per la startactivityforresult
    ArrayAdapter<BluetoothDevice> BTArrayAdapter; //array di bluetoothdevice di tipo adapter ---> per listview
    TextView text;
    ListView elencoDiscover; // dove vengono elencati i dispositivi nelle vicinanze
    Button button; // bottone annulla o inizia ricerca
    BluetoothDevice device; //identifica il dispositivo con bluetooth attivo
    BluetoothSocket BTSocket; // è il socket con il quale il bluetoothdevice si connette a un altro device
    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // è un identificativo del servizio attivo. identifica una certa applicazione bluetooth
    HolumApp g;                 //var globale                                                                            //come se fosse un numero di porta. sia il client che il server devono averlo uguale

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connessione);
        elencoDiscover = (ListView) findViewById(R.id.l_discover);
        text = (TextView) findViewById(R.id.t_c_1);
        button = (Button) findViewById(R.id.b_c_1);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();  //funzione che restituisce l'adapter del dispositivo
        BTArrayAdapter = new ArrayAdapter<>(Connessione.this, android.R.layout.simple_list_item_1);
        elencoDiscover.setAdapter(BTArrayAdapter);
        elencoDiscover.setOnItemClickListener(listener);
        g = (HolumApp)getApplication();   //classe var globale


        registerReceiver(ActionFoundReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));

        if (BTAdapter == null) {
            //non esiste bluetooth
            Toast.makeText(Connessione.this,"Non esiste il Bluetooth su questo dispositivo",  Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!BTAdapter.isEnabled()) {        //se non è attivo
            //nascondi la ui
            button.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            elencoDiscover.setVisibility(View.INVISIBLE);
            //richiedi permessi per accenderlo
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH); //inizia activity e attendi un result ---> onActivityResult
        } else {
            // se bt attivo allora inizia subito la ricerca
            BTAdapter.startDiscovery();
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
                BTArrayAdapter.add(device);
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };


    OnItemClickListener listener = new OnItemClickListener() {    //metodo della listview che permette di gestire il click su un elemento dell'array
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            //connectWithServer((BluetoothDevice) parent.getItemAtPosition(position));        //prende elemento cliccato e lo passa al metodo
            ConnectThread connessione = new ConnectThread((BluetoothDevice) parent.getItemAtPosition(position), uuid);
            connessione.start();
        }
    };





    public void connectWithServer(BluetoothDevice device){         //metodo per creare la connessione con il server

       /* BTAdapter.cancelDiscovery();       // selezionato il device dall'array, è necessario chiudere la discovery in quanto il device è già stato identificato
        try{

            BTSocket = device.createRfcommSocketToServiceRecord(uuid);  //metodo che assegna a btsocket un canale con il quale comunicare con il servizio UUID
            g.setBTSocket(BTSocket);   //copia btsocket in var globale
            BTSocket.connect();          //blocking call che attende una connessione con il server .... (da mettere su thread)
            if(BTSocket.isConnected()){    //appena riceve una connessione, visualizza messaggio
                Toast.makeText(Connessione.this,"CONNESSO",  Toast.LENGTH_SHORT).show();

            }


        }catch(IOException e){
            Toast.makeText(Connessione.this,"IOException",  Toast.LENGTH_SHORT).show();
        }*/






    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(ActionFoundReceiver);
    }
}
