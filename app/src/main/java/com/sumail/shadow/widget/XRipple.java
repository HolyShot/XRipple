package com.sumail.shadow.widget;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sumail.shadow.App;
import com.sumail.shadow.R;

/**
 * @author Shadow
 * @date 2018.03.22.
 */

public class XRipple extends View {
    private boolean rise;
    private   int waveLength;
    private   int waveHeight;
    private   int levelY;
    private   int darution;
    private  int boatBitmap;

    private Bitmap mBitmap;
    private Paint mPain;
    private Path mPath;
    private Path mPath1;
    private int width;
    private int height;
    private int dex;
    private int dey;
    private Region mRegion;
    private Region mRegion1;

    public XRipple(Context context) {
        this(context,null);
    }

    public XRipple(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XRipple(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        rise = typedArray.getBoolean(R.styleable.WaveView_rise, false);
        waveLength = (int)typedArray.getDimension(R.styleable.WaveView_waveLength, 400);
        waveHeight = (int)typedArray.getDimension(R.styleable.WaveView_waveHeight, 400);
        levelY = (int)typedArray.getDimension(R.styleable.WaveView_levelY, 500);
        darution = typedArray.getInteger(R.styleable.WaveView_darution, 2000);
        boatBitmap = typedArray.getResourceId(R.styleable.WaveView_boatBitmap, 0);
        typedArray.recycle();


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;//缩放图片
        if(boatBitmap > 0){
            mBitmap = BitmapFactory.decodeResource(getResources(),boatBitmap, options);
            //xformode 处理成圆形图片
            mBitmap =getcirculeBitmap(mBitmap);

        }else {
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tot,options);
        }
        mPain = new Paint();
        mPain.setColor(getResources().getColor(R.color.wavebg));
        mPain.setStyle(Paint.Style.FILL);

        mPath = new Path();
        mPath1 = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize,heightSize);
        width = widthSize;
        height= heightSize;
    }

    /**
     * 图片裁剪 setXfermode
     * @param mBitmap
     * @return
     */
    private Bitmap getcirculeBitmap(Bitmap mBitmap) {
        try {
            if (mBitmap == null){
                return null;
            }
            Bitmap Cribitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(Cribitmap);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight()));
            float roundPx = 0.0f;
            if (mBitmap.getWidth() > mBitmap.getHeight()){
                roundPx = mBitmap.getHeight() / 2.0f;
            }else {
                roundPx = mBitmap.getWidth() / 2.0f;
            }

            paint.setAntiAlias(true);
            canvas.drawARGB(0,0,0,0);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(rectF,roundPx,roundPx,paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight());
            canvas.drawBitmap(mBitmap,src,rect,paint);
            return Cribitmap;
        }catch (Exception e){
            Log.d(App.TAG,e.toString());
            return mBitmap;
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPath();
        canvas.drawPath(mPath,mPain);
        canvas.drawPath(mPath1,mPain);
        Rect bounds = mRegion.getBounds();
        Rect bounds1 = mRegion1.getBounds();
        int currtentTop = 0;
        if (bounds.top == 0){
            currtentTop = bounds1.top;
        }else if (bounds1.top == 0){
            currtentTop = bounds.top;
        }
        if (bounds1.top < bounds.top || bounds1.bottom < bounds.bottom){
            bounds = bounds1;
        }
        Paint p = new Paint();
        if (bounds.top > 0 || bounds.right > 0  ) {

            if (bounds.top < levelY) {
                canvas.drawBitmap(mBitmap, bounds.left - mBitmap.getWidth() / 2, bounds.top - mBitmap.getHeight(), p);
            } else {
                canvas.drawBitmap(mBitmap, bounds.left - mBitmap.getWidth() / 2, bounds.bottom - mBitmap.getHeight(), p);
            }
        }else {
            if (currtentTop > levelY){
                canvas.drawBitmap(mBitmap, width/2 - mBitmap.getWidth() / 2, currtentTop- mBitmap.getHeight(), p);
            }else {
                canvas.drawBitmap(mBitmap, width/2 - mBitmap.getWidth() / 2, currtentTop- mBitmap.getHeight(), p);
            }
        }

    }

    private void setPath() {
        mPath.reset();
        mPath1.reset();
        Log.d(App.TAG,"dey:" +dey);
        mPath.moveTo(-waveLength + dex,levelY -dey);
        mPath1.moveTo(-waveLength- waveLength /4 + dex,levelY -dey);
        float halfWaveLenght = waveLength / 2;
            for (int i = -waveLength; i < width + waveLength; i += waveLength) {
                //相对路径 相对于 moveTo点位（0,0）
                mPath.rQuadTo(halfWaveLenght / 2, -waveHeight, halfWaveLenght, 0);
                mPath.rQuadTo(halfWaveLenght / 2, waveHeight, halfWaveLenght,0 );
            }

            for (int i = -waveLength -waveLength/4; i < width + waveLength; i += waveLength) {
                //相对路径 相对于 moveTo点位（0,0）
                mPath1.rQuadTo(halfWaveLenght / 2, -waveHeight, halfWaveLenght, 0);
                mPath1.rQuadTo(halfWaveLenght / 2, waveHeight, halfWaveLenght,0 );
            }

            mRegion = new Region();
            mRegion1 = new Region();
            int x = width/ 2;
            Region clip = new Region((int)(x - 0.1),0,x,height);
            mRegion.setPath(mPath,clip);
            mRegion1.setPath(mPath1,clip);

            mPath.lineTo(width, height);
            mPath.lineTo(0, height);
            mPath.close();

            mPath1.lineTo(width, height);
            mPath1.lineTo(0, height);
            mPath1.close();
        }
    private ValueAnimator valueAnimator;
        public void setAnimation(){
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(darution);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    dex = (int) (waveLength * fraction);
        /*            if (rise){
                        dey += 1;
                    }*/
                    postInvalidate();
                }
            });
            valueAnimator.start();
        }

        public void setWaveUp(float percent){
            dey = (int)(percent * levelY);
            Log.d(App.TAG,"dey1 :" +dey + ",percent:" + percent+ ",levelY：" + levelY);
        }

        public void resetWaveUp(float percent){
            dey = (int)(1 - percent) * dey;
        }


}
