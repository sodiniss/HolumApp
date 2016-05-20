package com.example.holum.holum4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class Controlli extends Activity {

    int message;
    BluetoothService bts;
    boolean mBound = false;
    Touchpad tp;
    GestureDetector gt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlli2);

        tp = (Touchpad)findViewById(R.id.t_pad);
        gt = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                message = 5;
                bts.write(message);
                return true;
            }
        });
        tp.setOnTouchListener(new OnSwipeListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gt.onTouchEvent(event);
                boolean result ;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Register the first touch on TouchDown and this should not change unless finger goes up.
                        firstTouchX = event.getX();
                        firstTouchY = event.getY();
                        maxValX = 0.0f;
                        maxValY = 0.0f;

                        //As the event is consumed, return true
                        result = true;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //CurrentX/Y are the continues changing values of one single touch session. Change
                        //when finger slides on view
                        currentX = event.getX();
                        currentY = event.getY();
                        //setting the maximum value of X or Y so far. Any deviation in this means a  change of direction so reset the firstTouch value to last known max value i.e MaxVal X/Y.
                        if(maxValX <currentX){
                            maxValX = currentX;
                        }else{
                            firstTouchX= maxValX;
                            maxValX =0.0f;
                        }

                        if(maxValY <currentY){
                            maxValY = currentY;
                        }else{
                            firstTouchY= maxValY;
                            maxValY =0.0f;
                        }
                        //DeltaX/Y are the difference between current touch and the value when finger first touched screen.
                        //If its negative that means current value is on left side of first touchdown value i.e Going left and
                        //vice versa.
                        deltaX = currentX - firstTouchX;
                        deltaY = currentY - firstTouchY;

                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            //Horizontal swipe
                            if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                                if (deltaX > 0) {
                                    //means we are going right
                                    onRightSwipe(currentX);
                                } else {
                                    //means we are going left
                                    onLeftSwipe(currentX);
                                }
                            }
                        } else {
                            //It's a vertical swipe
                            if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                                if (deltaY > 0) {
                                    //means we are going down
                                    onDownSwipe(currentY);
                                } else {
                                    //means we are going up
                                    onUpSwipe(currentY);
                                }
                            }
                        }

                        result = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        //Clean UP
                        firstTouchX = 0.0f;
                        firstTouchY = 0.0f;

                        result = true;
                        break;

                    default:
                        result = false;
                        break;
                }

                return result;

            }
            public void onUpSwipe(float value) {
                message = 2;
                bts.write(message);

            }

            public void onDownSwipe(float value) {

                message = 4;
                bts.write(message);
            }

            public void onRightSwipe(float value) {
                message = 3;
                bts.write(message);
            }

            public void onLeftSwipe(float value) {
                message = 1;
                bts.write(message);
            }
        });



    }

    //metodi onclick dei bottoni
    //passano alla funzione writeint il valore che identifica la direzione (specificata nel server)

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onStart() {

        super.onStart();
        bindService(new Intent(this, BluetoothService.class), mConnection, Context.BIND_AUTO_CREATE);



    }
    @Override
    protected void onStop() {

        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    @Override
    protected void onResume() {
        //indietro o startActivity
        if (mBound) {
            bts.getBSL().checkState(this);
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        }
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
            bts.streamSetup();
            bts.getBSL().checkState(Controlli.this);

            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
            registerReceiver(bts.getBSL(), new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));

    }

    public void stateConnected(){
        //Toast.makeText(this,"CONNESSO",Toast.LENGTH_SHORT).show();
    }
    public void stateDisconnected(){
        Toast.makeText(this,"DISCONNESSO",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,Connessione.class));
    }


}
