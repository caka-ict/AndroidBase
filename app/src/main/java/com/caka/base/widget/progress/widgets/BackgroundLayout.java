package com.caka.base.widget.progress.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.caka.base.R;


/**
 * Mo ta class or interface
 * Created by: Phung Dang Hoan. HoanPD1.
 * Create time: 2/28/2017
 * Update time: 2/28/2017
 * Version: 1.0
 */

public class BackgroundLayout extends LinearLayout {

    private float mCornerRadius;
    private int mBackgroundColor;

    public BackgroundLayout(Context context) {
        super(context);
        init();
    }

    public BackgroundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BackgroundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        int color = getContext().getResources().getColor(R.color.COLOR_BG_LOADING);
        initBackground(color, mCornerRadius);
    }

    private void initBackground(int color, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            //noinspection deprecation
            setBackgroundDrawable(drawable);
        }
    }

    public void setCornerRadius(float radius) {
        mCornerRadius = Helper.dpToPixel(radius, getContext());
        initBackground(mBackgroundColor, mCornerRadius);
    }

    public void setBaseColor(int color) {
        mBackgroundColor = color;
        initBackground(mBackgroundColor, mCornerRadius);
    }
}
