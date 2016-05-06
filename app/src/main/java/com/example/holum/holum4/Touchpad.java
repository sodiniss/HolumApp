package com.example.holum.holum4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Vittorio on 23/02/2016.
 */
public class Touchpad extends View {
    Paint paintColor;
    TypedArray values;
    int padColor;
    Float padWidth, padHeight;
    int message = 0;
    float currentX;
    float currentY;

    public Touchpad(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintColor = new Paint();
        values = context.getTheme().obtainStyledAttributes(attrs,R.styleable.Touchpad,0,0);
        try {
            padColor = values.getInteger(R.styleable.Touchpad_padColor,0);
            padWidth = values.getDimension(R.styleable.Touchpad_padWidth, 0);
            padHeight = values.getDimension(R.styleable.Touchpad_padHeight,0);
        } finally {
            values.recycle();
        }



    }


    @Override
    protected void onDraw(Canvas canvas) {
        paintColor.setStyle(Paint.Style.FILL);
        paintColor.setAntiAlias(true);
        paintColor.setColor(padColor);
        canvas.drawRect(padHeight, padWidth, padHeight, padWidth, paintColor);
    }




    public int getMessage(){
        return message;
    }


    public Paint getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(Paint paintColor) {
        this.paintColor = paintColor;
        invalidate();
        requestLayout();
    }

    public int getPadColor() {
        return padColor;
    }

    public void setPadColor(int padColor) {
        this.padColor = padColor;
        invalidate();
        requestLayout();
    }

    public Float getPadWidth() {
        return padWidth;
    }

    public void setPadWidth(Float padWidth) {
        this.padWidth = padWidth;
        invalidate();
        requestLayout();
    }

    public Float getPadHeight() {
        return padHeight;
    }

    public void setPadHeight(Float padHeight) {
        this.padHeight = padHeight;
        invalidate();
        requestLayout();
    }

}
