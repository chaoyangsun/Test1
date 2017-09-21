package com.yangge.myapplication.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.yangge.myapplication.R;

/**
 * 自定义圆形进度条
 * @author hai
 *
 */
public class ArcProgressbar extends View {
    /**
     * 圆环半径
     */
    private int mRadius = 115; // Diameter英文为直径，该常量表示小圆直径的dp值
    /**
     * 圆环的宽度
     */
    private int mTrokeWidth = 15;
    /**
     * 起始角度
     */
    private int mStartAngle = 135;
    /**
     * 进度条进度颜色
     */
    private int mArcColor;

    private Paint mPaint;
    private int mProgress;// 表示进度
    private RectF mRect;
    private int mDiameter; // Diameter英文为直径，在该View中要绘制圆环，圆环由两个圆形确定（大圆和小圆），这个整形值表示小圆直径。
    private int mWidth;// 这个值表示圆环的宽度的2倍（大圆直径-小圆直径）

    private final int defaultColor; // 进度条背景颜色

    public ArcProgressbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defaultColor = Color.TRANSPARENT;
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgressbar, defStyle, 0);
        int num = ta.getIndexCount();
        for (int i = 0; i < num; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.ArcProgressbar_startAngle:
                    mStartAngle = ta.getInt(attr, 135);
                    break;
                case R.styleable.ArcProgressbar_arcColor:
                    mArcColor = ta.getColor(attr, Color.parseColor("#eed306"));
                    break;
                case R.styleable.ArcProgressbar_trokeWidth:
                    mTrokeWidth = ta.getInt(attr, 15);
                    break;
                case R.styleable.ArcProgressbar_radius:
                    mRadius = ta.getInt(attr, 115);
                    break;
            }
        }
        init();
    }

    public ArcProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressbar(Context context) {
        this(context, null);
    }

    private void init() {
        Resources res = getResources();
        // getDisplayMetrics()返回当前展示的metrics.
        DisplayMetrics metrics = res.getDisplayMetrics();
        // TypedValue.applyDimension(int unit, float value, DisplayMetrics
        // metrics)
        // 该方法中unit表示要转换成的单位，value表示数值，metrics表示当前的度量方式
        // DIAMETER是常量0x1E,十进制为30，下面语句就表示tmp的值为30dp换算成的像素数值
        float tmp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius, metrics);
        // ceil函数表示向上取整
        mDiameter = (int) Math.ceil(tmp);
        tmp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTrokeWidth, metrics);
        mWidth = (int) Math.ceil(tmp);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        // setStrokeWidth()设置画笔宽度
        // p.setStrokeWidth(0.5F*mWidth+0.5F*mDiameter);
        p.setStrokeWidth(0.4F * mWidth);
        p.setStrokeCap(Cap.ROUND);
        p.setColor(defaultColor);
        mPaint = p;

        float rightTop = (float) (mWidth / 2.0);// 这个值就是圆环宽度（大圆半径-小圆半径）
        mRect = new RectF(rightTop, rightTop, mDiameter + rightTop, mDiameter + rightTop);
        mProgress = 0;
    }

    protected boolean clear = false;

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        // 如果mProgress<360,则圆形进度条还未旋转完成，则用0x7f的透明度绘制一个完整的圆形作为进度条背景
        // 注意要先绘制背景条，再绘制进度条，因为后绘制的会覆盖在先绘制的上面
		/*
		 * if (mProgress < 360) { paint.setAlpha(0x7f);
		 * paint.setColor(defaultColor); canvas.drawArc(mRect, 135, 270, false,
		 * paint); }
		 */
        if (clear) {
            mPaint.setColor(Color.TRANSPARENT);
            clear = false;
            return;
        }
        if (mProgress != 0) {
            Paint paint = mPaint;
            paint.setColor(mArcColor);
            float degree = (float) (360.0f * mProgress / 360);
            paint.setAlpha(0xff);
            paint.setColor(mArcColor);
            canvas.drawArc(mRect, mStartAngle, degree, false, paint);
        }
    }

    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // mDiameter表示小圆直径，mWidth表示圆环宽度的2倍，所以meas表示大圆直径
        // 所以View的hight，width都为meas
        final int meas = mDiameter + mWidth;
        setMeasuredDimension(meas, meas);
    }

    public void setProgress(int p) {
        mProgress = p;
        invalidate();
    }

    public void postProgress(final int p) {
        post(new Runnable() {
            @Override
            public void run() {
                setProgress(p);
            }
        });
    }

    public void setmArcColor(int mArcColor) {
        this.mArcColor = mArcColor;
    }

    public void reset() {
        clear = true;
        invalidate();
        mProgress = 0;
    }
}