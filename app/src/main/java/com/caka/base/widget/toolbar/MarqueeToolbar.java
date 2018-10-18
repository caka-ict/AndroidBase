package com.caka.base.widget.toolbar;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MarqueeToolbar  extends Toolbar {

    TextView title;

    public MarqueeToolbar(Context context) {
        super(context);
    }

    public MarqueeToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!reflected) {
            reflected = reflectTitle();
        }
        super.setTitle(title);
        selectTitle();
    }

    @Override
    public void setTitle(int resId) {
        if (!reflected) {
            reflected = reflectTitle();
        }
        super.setTitle(resId);
        selectTitle();
    }

    boolean reflected = false;

    private boolean reflectTitle() {
        boolean isResult = false;
        try {
            Field field = Toolbar.class.getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            title = (TextView) field.get(this);
            title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            title.setMarqueeRepeatLimit(-1);
            isResult = true;
        } catch (NoSuchFieldException e) {
            android.util.Log.e(MarqueeToolbar.class.getName(), "error: " +  e.toString());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            android.util.Log.e(MarqueeToolbar.class.getName(), "error: " +  e.toString());
            e.printStackTrace();
        } catch (NullPointerException e) {
            android.util.Log.e(MarqueeToolbar.class.getName(), "error: " +  e.toString());
            e.printStackTrace();
        }
        return isResult;
    }

    public void selectTitle() {
        if (title != null)
            title.setSelected(true);
    }
}
