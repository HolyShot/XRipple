package com.sumail.shadow.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.sumail.shadow.App;
import com.sumail.shadow.R;

/**
 * @author Shadow
 * @date 2018.03.23.
 */

public class PullZoomView extends ListView {
    private ImageView mIV_head;
    private XRipple waveView;
    private int mDefaultHeight; //ImgeView 开始的高度
    private int mImageViewHeight;//ImageView中图片的高度
    private int screenHeight;//ImageView中图片的高度
    public PullZoomView(Context context) {
        this(context,null);
    }

    public PullZoomView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullZoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();
        //加载头布局
        View headerView = View.inflate(getContext(), R.layout.zoom_header,null);
        mIV_head = headerView.findViewById(R.id.iv_header);
        waveView = headerView.findViewById(R.id.waveView);
        addHeaderView(headerView);
        mIV_head.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mIV_head.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mDefaultHeight = mIV_head.getMeasuredHeight();
                mImageViewHeight = mIV_head.getDrawable().getIntrinsicHeight();
            }
        });

    }
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        if (isTouchEvent && deltaY < 0){
            if (mIV_head.getHeight() <= mImageViewHeight    ){
                int newHeight = (int)(mIV_head.getHeight() - (deltaY / 3.0f));
                mIV_head.getLayoutParams().height = newHeight;
                mIV_head.requestLayout();

            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    private int lastY;
    private int newY;
    float percent = 0;
    float offsetY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                 lastY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                 newY = (int)ev.getRawY();

                offsetY = newY - lastY;
                percent = offsetY / screenHeight;
                  if (percent > 0){
                      waveView.setWaveUp(percent);
                  }
                break;
            case MotionEvent.ACTION_UP:
                int currHeight = mIV_head.getHeight();
                ValueAnimator animator = ValueAnimator.ofInt(currHeight, mDefaultHeight);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        mIV_head.getLayoutParams().height = value;
                        mIV_head.requestLayout();
                        waveView.resetWaveUp(value/mDefaultHeight);
                    }
                });
                animator.setDuration(500);
                animator.setInterpolator(new OvershootInterpolator());
                animator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void startAnimation(){
        waveView.setAnimation();
    }
}
