package com.example.xcroundprogressbardemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xcroundprogressbardemo.R;
import com.example.xcroundprogressbardemo.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class MyProgress extends View {

    private Paint textPaint;//写文字的笔
    private Paint bgPaint;//背景画笔
    private Paint progressPaint;//进度的画笔
    private Paint pointPaint;//选择的点
    private Paint unpointPaint;//没有选择的点

    private static int TOTAL_PROGRESS = 100;//总长度

    /**
     * 进度条渐变的颜色和原点的颜色
     * 如果需要加渐变色则加END_COLOR
     */
    private int STATR_COLOR;//进度条的颜色
    private int BG_COLOR;//背景的颜色
    private int TEXT_COLOR;//文字的颜色
    //    private static int END_COLOR = Color.parseColor("#FFFFFF");

    //设置进度
    private int progress;

    private int pointRadius;//节点的半径
    private int progressWidth;//进度的宽
    private int pointMun;//节点的个数
    private int textSize;//文字的大小

    private int viewWidth;//控件的宽
    private int viewHeight;//控件的高

    private int line_height;//线的开始位置

    private int marginTop;//文字与线之间

    private Boolean isText;//是否有文字

    private List<String> arrStr;//文字数据
    private List<Rect> mBounds;//每个文字需要的空间，主要用于计算高度


    //设置文字数据
    public void setArrStr(List<String> arrStr) {
        this.arrStr = arrStr;
        //设置数据同时，生成mBounds的list集合
        if (arrStr != null) {
            mBounds = new ArrayList<>();
            for (int i = 0; i < arrStr.size(); i++) {
                Rect rect = new Rect();
                textPaint.getTextBounds(arrStr.get(i), 0, arrStr.get(i).length(), rect);
                mBounds.add(rect);
            }
        }
    }

    //设置进度数据
    public void setProgress(int progress) {
        this.progress = progress;
    }

    public MyProgress(Context context) {
        super(context);
        init(context, null);
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //初始化 属性 和画笔
    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyProgress);
        //获取属性
        STATR_COLOR = array.getColor(R.styleable.MyProgress_progressColor, Color.parseColor("#BCA371"));
        BG_COLOR = array.getColor(R.styleable.MyProgress_progressBackgroundColor, Color.parseColor("#E5E5E5"));
        TEXT_COLOR = array.getColor(R.styleable.MyProgress_progressTextColor, Color.BLACK);
        progressWidth = array.getDimensionPixelOffset(DensityUtils.dp2px(context, R.styleable.MyProgress_progressWidth), DensityUtils.dp2px(context, 3));
        pointRadius = array.getDimensionPixelSize(R.styleable.MyProgress_progressPointRadius, 12);
        textSize = array.getDimensionPixelSize(DensityUtils.sp2px(context, R.styleable.MyProgress_progressTextSize), DensityUtils.sp2px(context, 10));
        pointMun = array.getInteger(R.styleable.MyProgress_progressMun, 3);
        isText = array.getBoolean(R.styleable.MyProgress_progressIsText, false);

        marginTop = DensityUtils.dp2px(context, 10);

        //设置画笔
        bgPaint = new Paint();
        bgPaint.setStrokeWidth(progressWidth);
        bgPaint.setColor(BG_COLOR);

        pointPaint = new Paint();
        pointPaint.setColor(STATR_COLOR);

        unpointPaint = new Paint();
        unpointPaint.setColor(BG_COLOR);

        progressPaint = new Paint();
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(STATR_COLOR);

        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(TEXT_COLOR);


    }


    /**
     * 手动计算 宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            //layout_height="match_parent"时
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            //layout_height="wrap_content"时
            if (isText) {
                //存在文字时计算空间高度
                Rect rect = new Rect();
                textPaint.getTextBounds("123", 0, 2, rect);
                viewHeight = marginTop * 2 + rect.height() + Math.max(pointRadius * 2, progressWidth);
            } else {
                //不存在文字时计算高度
                viewHeight = Math.max(pointRadius * 2, progressWidth);
            }
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画文字
        if (isText && arrStr != null) {
            //计算线的高度
            line_height = pointRadius * 2 + marginTop;
            for (int i = 0; i < arrStr.size(); i++) {

                if (i == 0) {
                    canvas.drawText(arrStr.get(0), 0, marginTop, textPaint);
                } else if (i == arrStr.size() - 1) {
                    canvas.drawText(arrStr.get(0), viewWidth - mBounds.get(i).width(), marginTop, textPaint);
                } else {
                    canvas.drawText(arrStr.get(i), (viewWidth * i / (arrStr.size() - 1)) - pointRadius, marginTop, textPaint);
                }
            }
            /**
             * 如果存在文字时候，需要的原点与文字列表数量不一样时，以文字列表为主
             */
            if (pointMun != arrStr.size()) {
                pointMun = arrStr.size();
            }
        } else {
            //不存在文字时 进度条开始的y坐标
            line_height = pointRadius;
        }

        //画背景线
        canvas.drawLine(pointRadius, line_height, viewWidth, line_height, bgPaint);
        //画该变色的点
        for (int i = 0; i < pointMun; i++) {
            if (i == 0 && progress == 0) {
                canvas.drawCircle(pointRadius, line_height, pointRadius, unpointPaint);
            } else if (i == 0) {
                canvas.drawCircle(pointRadius, line_height, pointRadius, pointPaint);
            } else if ((progress * 100 / TOTAL_PROGRESS) >= (i * 100 / (pointMun - 1)) && progress != 100) {
                canvas.drawCircle(viewWidth * i / (pointMun - 1), line_height, pointRadius, pointPaint);
            } else if (i == pointMun - 1 && progress != 100) {
                canvas.drawCircle((viewWidth * i / (pointMun - 1)) - pointRadius, line_height, pointRadius, unpointPaint);
            } else if (progress == 100) {
                canvas.drawCircle((viewWidth * i / (pointMun - 1)) - pointRadius, line_height, pointRadius, pointPaint);
            } else {
                canvas.drawCircle(viewWidth * i / (pointMun - 1), line_height, pointRadius, unpointPaint);
            }
        }
        //渐变色的画法
//        LinearGradient lg = new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{STATR_COLOR, END_COLOR}, new float[]{0, 1.0f}, Shader.TileMode.CLAMP);
//        progressPaint.setShader(lg);
        if (progress > 0) {
            //画进度条
            canvas.drawLine(pointRadius, line_height, viewWidth * progress / TOTAL_PROGRESS, line_height, progressPaint);
        }
    }
}
