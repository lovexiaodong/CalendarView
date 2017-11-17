package com.ums.drawqixingdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * Created by 张玉栋 on 2017/11/15.
 */

public class CalenderView extends View {

    private static final String TAG = "CalenderView";


    /**
     * <declare-styleable name="CalenderView">
     <attr name="lineColor" format="color"/>
     <attr name="weekColor" format="color"/>
     <attr name="weekSize" format="dimension"/>
     <attr name="weekBackground" format="color"/>
     <attr name="dateColor" format="color"/>
     <attr name="dateSize" format="color"/>
     <attr name="dateBackground" format="color"/>
     <attr name="selectBackground" format="color"/>
     </declare-styleable>
     */
    private int mLineColor;

    private int mWeekColor;

    private float mWeekSize;

    private int mWeekBackground;

    private int mDateColor;

    private float mDateSize;

    private int mDateBackground;

    private int mSelectBackground;

    private float mLineBorder;

    private int mWidth;
    private int mHeight;
    private String[] week = {"日", "一", "二", "三", "四", "五", "六"};
    private Paint mPaint;

    private float mCellWidth;

    private float mCellHeight;

    private int clickIndex = -1;

    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    private int mRowCount = 7;
    private int mColumnCount = 7;

    public CalenderView(Context context) {
        this(context, null);
    }

    public CalenderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CalenderView);

        mLineColor = ta.getColor(R.styleable.CalenderView_lineColor, Color.GRAY);
        mWeekColor = ta.getColor(R.styleable.CalenderView_weekColor, Color.GRAY);
        mWeekSize = ta.getDimension(R.styleable.CalenderView_weekSize, dip2px(20));
        mWeekBackground = ta.getColor(R.styleable.CalenderView_weekBackground, Color.WHITE);
        mDateColor = ta.getColor(R.styleable.CalenderView_dateColor, Color.WHITE);
        mDateBackground = ta.getColor(R.styleable.CalenderView_dateBackground, Color.WHITE);
        mDateSize = ta.getDimension(R.styleable.CalenderView_dateSize, dip2px(20));
        mSelectBackground = ta.getColor(R.styleable.CalenderView_selectBackground, Color.GREEN);
        mLineBorder = ta.getDimension(R.styleable.CalenderView_lineBorder, dip2px(1));
        Log.i(TAG, "CalenderView: mLineColor = " + mLineColor );
        Log.i(TAG, "CalenderView: mWeekColor = " + mWeekColor );
        Log.i(TAG, "CalenderView: mWeekBackground = " + mWeekBackground );
        ta.recycle();
        initData();
    }

    private void initData() {
        mPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(mLineBorder);

        Calendar calendar = Calendar.getInstance();
        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentMonth = calendar.get(Calendar.MONTH) + 1;

        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        Log.i(TAG, "initData:  Year = " + mCurrentYear + "; Month = " + mCurrentMonth + "; Day = " + mCurrentDay);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
        calculateCell();

    }
    private void calculateCell(){
        mCellHeight =  (mHeight - mLineBorder * (mRowCount + 1))/ mRowCount;
        mCellWidth = (mWidth  - mLineBorder * (mColumnCount + 1))/ mColumnCount;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
        drawWeekBackground(canvas);
        drawWeek(canvas);
        drawDateBackground(canvas);
        draCurrentDate(canvas);
        draDate(canvas);
    }

    private void drawDateBackground(Canvas canvas) {
        mPaint.setColor(mDateBackground);
        mPaint.setStyle(Paint.Style.FILL);
        for(int i = 1; i <= mRowCount; i++){
            for (int j = 0; j < mColumnCount; j++){
                float left = j * (mCellWidth + mLineBorder) + mLineBorder;
                float top = (mCellHeight + mLineBorder) * i + mLineBorder;
                float right = j * (mCellWidth + mLineBorder) + mLineBorder + mCellWidth;
                float bottom = (mCellHeight + mLineBorder) * i + mLineBorder + mCellHeight;
                RectF rectF = new RectF(left, top, right, bottom);
                canvas.drawRect(rectF, mPaint);
            }
        }
    }

    private void drawWeekBackground(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mWeekBackground);
        mPaint.setStyle(Paint.Style.FILL);

        for(int i = 0; i < mColumnCount; i++){
            float left = i * mCellWidth + (i+ 1) * mLineBorder - getPaddingLeft();
            float top = mLineBorder + mLineBorder - getPaddingTop();
            float right = (i + 1) * (mLineBorder + mCellWidth)  -getPaddingLeft();
            float bottom = mCellHeight + mLineBorder - getPaddingTop();
            RectF rectF = new RectF(left, top, right, bottom);
            canvas.drawRect(rectF, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                processDown(event);
                break;
        }

        return super.onTouchEvent(event);
    }

    private void processDown(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        int row = (int)Math.floor(y /  (mCellWidth + mLineBorder)) + 1;
        int column = (int) Math.floor(x / (mCellHeight + mLineBorder)) + 1;
        if(row > 1){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mCurrentYear);
            calendar.set(Calendar.MONTH, mCurrentMonth - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            int days = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int week = (calendar.get(Calendar.DAY_OF_WEEK) + 1) % 7;
            clickIndex = (row - 2) * 7 + column -1;
            Log.i(TAG, "processDown: row = " + row + "; column = " + column + "clickIndex = " + clickIndex);
            Log.i(TAG, "processDown: week = " + week + "; days = " + days);
            if(clickIndex < week || clickIndex > days + week){
                clickIndex = -1;
            }else {

           invalidate();
            }

             invalidate();
        }else{
            clickIndex = -1;
        }



        Log.i(TAG, "processDown: event.getX()" + event.getX() + "; event.getY() =" + event.getY());
        Log.i(TAG, "processDown:  event.getRawX()=" +  event.getRawX() + " event.getRawY()=" + event.getRawY());

    }

    private void draCurrentDate(Canvas canvas) {
        int row ;
        int column;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mCurrentYear);
        calendar.set(Calendar.MONTH, mCurrentMonth - 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = mCurrentDay;

        if(clickIndex != -1){
            row = clickIndex / mRowCount;
            column = clickIndex % mColumnCount;
        }else{

            Log.i(TAG, "draCurrentDate: year = " + year + "; month = " + month + "; day = " + day);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int firstWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            Log.i(TAG, "draCurrentDate: year = " + year + "; month = " + month + "; day = " + day + "firstWeek" + firstWeek);
            int dayPostion = day + firstWeek -1;
            row = dayPostion / 7;
            column = dayPostion % 7;
        }

        float x = column * (mCellWidth + mLineBorder) + mLineBorder;
        float y = row * (mCellHeight + mLineBorder) + mCellHeight + mLineBorder;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        canvas.drawRect(x, y, x + mCellWidth, y + mCellHeight, paint);

    }

    private void draDate(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mDateColor);

        Log.i(TAG, "draDate: mDateColor = " + mDateColor);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mCurrentYear);
        calendar.set(Calendar.MONTH, mCurrentMonth -1);
        int dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.i(TAG, "draDate: dayCount = " + dayCount);
        for (int i = 0; i <dayCount; i++){
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            int rowCount = (i + (7 - weekDay)) / 7;
            Log.i(TAG, "draDate: rowCount = " + rowCount);

            float textWidth =  mPaint.measureText(i + 1 + "");
            float x = (weekDay -1 )  * (mCellWidth + mLineBorder) + mLineBorder + (mCellWidth - textWidth) /2;
            Paint.FontMetricsInt metricsInt =  mPaint.getFontMetricsInt();

            float y = (rowCount + 1) * (mCellHeight + mLineBorder) + mLineBorder +
                    (mCellHeight - (metricsInt.bottom - metricsInt.top))/ 2 - metricsInt.top;

            Log.i(TAG, "draDate: x = " + x + "; y = " + y);
            canvas.drawText(i + 1 + "", x, y, mPaint);

        }
    }

    private void drawWeek(Canvas canvas) {

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(mWeekSize);
        mPaint.setColor(mWeekColor);
        for (int i = 0; i < week.length; i++){

           float textWidth =  mPaint.measureText(week[i]);
           Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
           float x = i * (mCellWidth + mLineBorder) + mLineBorder + (mCellWidth - textWidth)/ 2;
            float textHeight = fontMetricsInt.bottom - fontMetricsInt.top;

            float y = (mCellHeight - textHeight)/2 -fontMetricsInt.top + mLineBorder;
            canvas.drawText(week[i], x, y, mPaint);
        }

    }

    private void drawLine(Canvas canvas) {
        Path path = new Path();

        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineBorder);
        for (int i = 0; i <= mRowCount; i++){
            path.moveTo(mLineBorder, i * (mCellHeight+ mLineBorder));
            path.lineTo(mWidth, i * (mCellHeight+ mLineBorder));
        }

        for(int i = 0; i <= mColumnCount; i++){
            path.moveTo(i * (mCellWidth + mLineBorder),0);
            path.lineTo(i *  (mCellWidth + mLineBorder), mHeight);
        }

        canvas.drawPath(path, mPaint);
    }


    private int measureWidth(int widthMeasureSpec){
        int result;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if(mode == MeasureSpec.EXACTLY){
            result = width;
        }else{
            result = getResources().getDisplayMetrics().widthPixels ;
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(result, width);
            }
        }

        mWidth = result;
        return result;
    }

    private int measureHeight(int heightMeasureSpec){
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height= MeasureSpec.getSize(heightMeasureSpec);
        if(mode == MeasureSpec.EXACTLY){
            result = height;
        }else{
            result = getResources().getDisplayMetrics().widthPixels;
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(result, height);
            }
        }

        mHeight = result;
        return result;
    }
    public  int dip2px( float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setDate(int year, int month, int day){
        this.mCurrentYear = year;
        this.mCurrentMonth = month;
        this.mCurrentDay = day;
    }
}
