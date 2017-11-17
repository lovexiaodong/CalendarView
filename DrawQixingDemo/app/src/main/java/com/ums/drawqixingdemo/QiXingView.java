package com.ums.drawqixingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张玉栋 on 2017/11/14.
 */

public class QiXingView extends View {

    private static final String TAG = "QiXingView";
    private int count;

    private int interVal;

    private int crirleR;

    private float anagle;

    private Paint linePaint;

    private int mWidth;
    private int mHeight;

    private Context mContext;

    private ArrayList<ArrayList<Point>> mPoint = new ArrayList<>();

    public QiXingView(Context context) {
        this(context, null);
    }

    public QiXingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QiXingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        initPoint();
        initPint();
    }

    private void initPint() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dip2px(mContext, 1));
    }

    private void initPoint() {
        for (int i = 0; i < interVal; i++){
            ArrayList<Point> list = new ArrayList<>();
            for (int j = 0; j < count; j++){

                float r = (float) ((crirleR * 1.0) / interVal * (interVal -i));

                int x = (int) (r * Math.sin(j * anagle - Math.PI /2 ));
                int y = (int) (r * Math.cos(j * anagle - Math.PI /2));

                Log.i(TAG, "initPoint: x = " + x + "; y = " + y + "; r = " + r);
                Point point = new Point();
                point.x = x;
                point.y = y;
                list.add(point);
            }
            mPoint.add(list);
        }
    }

    private void init() {
        count = 7;
        interVal = 4;
        crirleR = dip2px(mContext, 100);
        anagle = (float) (Math.PI / count * 2);
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = mWidth;
        linePaint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.translate(mWidth/2, mHeight/2);
        drawPolygon(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {

        canvas.save();
        linePaint.setColor(Color.parseColor("#99DCE2"));
        linePaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        for(int i = 0; i < count; i++){
            Point point = mPoint.get(0).get(i);
            if(i == 0){

                path.moveTo(point.x, point.y);
            }else{
                path.lineTo(point.x, point.y);
            }
        }
        path.close();
        canvas.drawPath(path, linePaint);

        path.reset();
        for(int i = 0; i < count; i++){
            Point point = mPoint.get(0).get(i);
            path.moveTo(0, 0);
            path.lineTo(point.x, point.y);
            canvas.drawPath(path, linePaint);
            path.reset();
        }

        canvas.restore();

    }

    private void drawPolygon(Canvas canvas) {
        //保存画布当前状态(平移、放缩、旋转、裁剪等),和canvas.restore()配合使用
        canvas.save();
        //设置为填充且描边
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();  //路径
        for (int i = 0; i < interVal; i++) {  //循环、一层一层的绘制
            //每一层的颜色都都不同
            switch (i) {
                case 0:
                    linePaint.setColor(Color.parseColor("#D4F0F3"));
                    break;
                case 1:
                    linePaint.setColor(Color.parseColor("#99DCE2"));
                    break;
                case 2:
                    linePaint.setColor(Color.parseColor("#56C1C7"));
                    break;
                case 3:
                    linePaint.setColor(Color.parseColor("#278891"));
                    break;
            }
            for (int j = 0; j < count; j++) {   //每一层有n个点
                float x = mPoint.get(i).get(j).x;
                float y = mPoint.get(i).get(j).y;
                Log.i(TAG, "drawPolygon:  x = " + x + "; y = " + y);
                if (j == 0) {
                    //如果是每层的第一个点就把path的起点设置为这个点
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            path.close();  //设置为闭合的
            canvas.drawPath(path, linePaint);
            path.reset();   //清除path存储的路径
        }
        canvas.restore();
    }

    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
