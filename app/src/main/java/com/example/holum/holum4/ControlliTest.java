package com.example.holum.holum4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ControlliTest extends Activity {

    int message;
    boolean mBound = false;
    Touchpad tp;
    static TextView t;
    GestureDetector gt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlli2test);
        t = (TextView)findViewById(R.id.display);
        tp = (Touchpad)findViewById(R.id.t_pad);
        gt = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                t.append("ciao");
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
                t.append("UP" + "\n");

            }

            public void onDownSwipe(float value) {

                message = 4;
                t.append("DOWN" + "\n");
            }

            public void onRightSwipe(float value) {
                message = 3;
                t.append("RIGHT" + "\n");
            }

            public void onLeftSwipe(float value) {
                message = 1;
                t.append("LEFT" + "\n");
            }
        });




        /*
        tp.setOnTouchListener(new OnSwipeListener() {

            public void onUpSwipe(float value) {
                message = 2;
                t.append("UP" + "\n");

            }

            public void onDownSwipe(float value) {

                message = 4;
                t.append("DOWN" + "\n");
            }

            public void onRightSwipe(float value) {
                message = 3;
                t.append("RIGHT" + "\n");
            }

            public void onLeftSwipe(float value) {
                message = 1;
                t.append("LEFT" + "\n");
            }

        });

*/

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));

    }

    public void appendi(CharSequence c){
        t.append(c);
    }
}
