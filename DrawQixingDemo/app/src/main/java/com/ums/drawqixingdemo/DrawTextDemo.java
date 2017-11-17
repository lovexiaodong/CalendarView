package com.ums.drawqixingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 张玉栋 on 2017/11/16.
 */

public class DrawTextDemo extends View {

    private static final String TAG = "DrawTextDemo";
    private int width;
    private int height;

    public DrawTextDemo(Context context) {
        this(context, null);
    }

    public DrawTextDemo(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DrawTextDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
    }

    private void initData() {
        width = getResources().getDisplayMetrics().widthPixels;
        height = 500;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        String text = "Hellow world, fg";
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        Log.i(TAG, "onDraw1: " + paint.getFontMetricsInt());
        Path path = new Path();
        path.moveTo(0, 100);
        path.lineTo(width, 100);
        path.moveTo(0, 300);
        path.lineTo(width, 300);
        canvas.drawPath(path, paint);

        Rect targetRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), targetRect);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        Log.i(TAG, "onDraw2: " + fontMetrics.toString());

        float textWidth = paint.measureText(text);


        int baseline = (200 - (fontMetrics.bottom - fontMetrics.top))/2 - fontMetrics.top + 100;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
//        canvas.drawText(text, (width - textWidth)/2, baseline, paint);

        Log.i(TAG, "onDraw: baseline = " + baseline);
        canvas.drawText(text, (width - textWidth)/2, baseline, paint);
//        canvas.drawRect(rect, paint);
    }
}
