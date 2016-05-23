package com.example.holum.holum4;

import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector;
import android.view.*;
public class OnSwipeListener  extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{

    float deltaX ;
    float deltaY ;
    float maxValX;
    float maxValY;
    float firstTouchX;
    float firstTouchY;
    float dist;
    float currentX ;
    float currentY ;
    float SWIPE_THRESHOLD = 20.0f;
    float MIN_DISTANCE = 100.0f;

    int n;

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }
    public float distance(MotionEvent event, int first, int second) {
        if (event.getPointerCount() >= 2) {
            final float x = event.getX(first) - event.getX(second);
            final float y = event.getY(first) - event.getY(second);

            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
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

    }

    public void onDownSwipe(float value) {

    }

    public void onRightSwipe(float value) {

    }

    public void onLeftSwipe(float value) {

    }




}