package com.yangge.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.yangge.myapplication.R;

/**
 * Created by charming-yin on 2017/9/6.
 */

public class CustomImageView extends View {
    private Rect mTextBount;
    private Rect rect;
    private int mImaageScale;
    private Bitmap mImage;
    private  Paint mPaint;
    int width;
    int heigth;
    /**
     * 图片缩放模式
     */
    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;

    /**
     * 文本
     */
    private String mTitle;
    /**
     * 文本的颜色
     */
    private int mTitleColor;
    /**
     * 文本的大小
     */
    private int mTitleSize;


    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageView_titleText :
                        mTitle = ta.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    mTitleSize = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            16, getResources().getDisplayMetrics()
                    ));
                    break;
                case R.styleable.CustomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), ta.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImaageScale = ta.getInt(attr, 0);
            }
        }
        ta.recycle();
        rect = new Rect();
        mPaint = new Paint();
        mTextBount = new Rect();
        mPaint.setTextSize(mTitleSize);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if(mode == MeasureSpec.EXACTLY) {
            width = size;
        }else {
            int desiredTitle = getPaddingLeft() + mTextBount.width() + getPaddingRight();
            int desiredImage = getPaddingLeft() + mImage.getWidth() + getPaddingRight();
            if(mode == MeasureSpec.AT_MOST) {
                int desired = Math.max(desiredImage, desiredTitle);
                width = Math.min(desired, size);
            }
        }
        
        mode = MeasureSpec.getMode(heightMeasureSpec);
        size = MeasureSpec.getSize(heightMeasureSpec);
        if(mode == MeasureSpec.EXACTLY) {
            heigth = size;
        }else {
            int desired = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextBount.height();
            if(mode == MeasureSpec.AT_MOST) {
                heigth = Math.min(desired, size);
            }

        }
        setMeasuredDimension(width, heigth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 边框
         */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        rect.left = getPaddingLeft();
        rect.right = width - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = heigth - getPaddingBottom();

        mPaint.setColor(mTitleColor);
        mPaint.setStyle(Paint.Style.FILL);

        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        if(mTextBount.width() > width) {
            TextPaint textPaint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitle, textPaint, (float) (width - getPaddingLeft() - getPaddingRight()),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), heigth - getPaddingBottom(), mPaint);
        }else {//正常情况，将字体居中  
            canvas.drawText(mTitle, width/2 - mTextBount.width()*1f/2, heigth - getPaddingBottom(), mPaint);
        }

        rect.bottom -= mTextBount.height();
        if(mImaageScale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, rect, mPaint);
        }else {
            rect.left = width / 2 - mImage.getWidth() / 2;
            rect.right = width / 2 + mImage.getWidth() / 2;
            rect.top = (heigth - mTextBount.height())/2 - mImage.getHeight()/2;
            rect.bottom = (heigth - mTextBount.height())/2 + mImage.getHeight()/2;
            canvas.drawBitmap(mImage, null, rect, mPaint);
        }
    }
}
