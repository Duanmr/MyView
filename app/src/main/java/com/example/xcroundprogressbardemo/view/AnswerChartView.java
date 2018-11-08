package com.example.xcroundprogressbardemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xcroundprogressbardemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 弧线对比图
 *
 * @param
 * @author LH
 * @return
 * @data 2016年1月25日 下午6:17:34
 **/
public class AnswerChartView extends View {
    private Paint mPaint;
    private int mCircleWidth; //圆环宽度
    /**
     * 画文字的画笔
     */
    private Paint textPaint;




    private int circleColor = Color.parseColor("#EDEDED");
    private int  ringColor = Color.parseColor("#EE3B3B");
    private int angle = 0;
    private RectF oval;

    public AnswerChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundRateView, defStyleAttr, 0);
        int n = array.getIndexCount();//自定义属性个数
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);//通过索引获取具体属性
            switch (attr) {
                case R.styleable.RoundRateView_rrv_circleWidth:
                    mCircleWidth = array.getDimensionPixelSize(attr, (int) dip2px(20f));
                    if (mCircleWidth < 2) {
                        mCircleWidth = 2;//最小宽度是2
                    }
                    break;
                case R.styleable.RoundRateView_rrv_circleColor:
                    circleColor = array.getColor(attr,Color.GRAY);
                    break;
                case R.styleable.RoundRateView_rrv_ringColor:
                    ringColor = array.getColor(attr,Color.GRAY);
                    break;
            }
        }
        array.recycle();//定义完后属性对象回


        mPaint = new Paint();  //创建画笔
        mPaint.setAntiAlias(true);  //设置绘制时抗锯齿
        mPaint.setStyle(Paint.Style.STROKE); //设置绘画空心（比如画圆时画的是空心圆而不是实心圆）
        mPaint.setStrokeWidth(mCircleWidth);//设置画笔线宽

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;//获取屏幕宽
        int heightPixels = this.getResources().getDisplayMetrics().heightPixels;//获取屏幕高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hedight = MeasureSpec.getSize(heightMeasureSpec);
        int minWidth = Math.min(widthPixels, width);
        int minHedight = Math.min(heightPixels, hedight);
        setMeasuredDimension(Math.min(minWidth, minHedight), Math.min(minWidth, minHedight));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    /**
     * 画出圆弧
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        if (oval == null) {
            int min = Math.min(getWidth() - mCircleWidth / 2, getHeight() - mCircleWidth / 2);
            oval = new RectF(mCircleWidth / 2, mCircleWidth / 2,
                    min, min);
        }
        float startAngle = -90f;//经过试验，-90这个值就是12点方向的位置

        //画一个底层圆环,颜色和间隙颜色一样,因为间隙的色块和其他色块之间会有小缝隙
        mPaint.setColor(circleColor);
        mPaint.setStrokeWidth(mCircleWidth - 1); //宽度减1是防止底色溢出
        canvas.drawArc(oval, -90, 360, false, mPaint);


        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(ringColor);//设置画笔颜色
        canvas.drawArc(oval, startAngle, (angle / 100) * 360, false, mPaint);
    }

    public void setAngle(int angle) {
        this.angle = angle;
        postInvalidate();//重绘
    }




    public static float dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }

}