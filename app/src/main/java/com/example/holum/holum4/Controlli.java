package com.example.holum.holum4;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class Controlli extends Activity {
    TextView t;
    int message;
    BluetoothSocket BTSocket;
    OutputStream outStream;
    HolumApp g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlli);
        t = (TextView)findViewById(R.id.t_prova);
        g = (HolumApp)getApplication();
        BTSocket = g.getBTSocket();
        if(BTSocket==null)
            Toast.makeText(Controlli.this,"null value",  Toast.LENGTH_SHORT).show();
    }

    //metodi onclick dei bottoni
    //passano alla funzione writeint il valore che identifica la direzione (specificata nel server)
    public void Left(View v) {
        message = 1;
        this.writeInt(message);
    }
    public void Up(View v) {
        message = 2;
        this.writeInt(message);
    }
    public void Right(View v) {
        message = 3;
        this.writeInt(message);
    }
    public void Down(View v) {
        message = 4;
        this.writeInt(message);
    }
    public void Enter(View v) {
        message = 5;
        this.writeInt(message);
    }

    public void writeInt(int message){

        try {
            outStream = this.BTSocket.getOutputStream();   //ottiene l'output stream del btsocket


            outStream.write(message);         //ci scrive la direzione scelta dall'onclick
        } catch (IOException e) {

        }

    }

}
