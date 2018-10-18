package com.caka.base.widget.edittext;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;

import com.caka.base.R;

import static com.caka.base.utils.AppUtils.isNotNullOrEmpty;


/**
 * Created by phung on 14/09/2017.
 */

public class EditTextMaster extends AppCompatMultiAutoCompleteTextView implements TextWatcher_CakaICT.TextWatcherListener, OnFocusChangeListener, OnTouchListener {

    public static final int NORMAL = 0;
    public static final int CLEAR_TEXT = 1;
    public static final int USER_ACCOUNT = 2;
    public static final int SHOW_PASSWORD = 3;
    private int typeEditText = NORMAL;
    private float textHintSize = 12;
    private float textSize = getTextSize();
    private float mScale;

    private Drawable iconDrawableBefore;
    private Drawable iconDrawableAfter;
    private int iconColorBefore = 0;
    private int iconColorAfter = 0;

    private OnTouchListener touchListener;
    private OnFocusChangeListener focusChangeListener;

    private boolean leftToRight = true;
    private boolean isShowingAfter = false;
    private boolean isShowingPassword = false;

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        handlingText();
        if (focusChangeListener != null) {
            focusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    public static enum Location {
        LEFT(0), RIGHT(2);

        final int idx;

        private Location(int idx) {
            this.idx = idx;
        }
    }

    private Location loc = Location.RIGHT;


    public EditTextMaster(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public EditTextMaster(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public EditTextMaster(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
    }


    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }


    @Override
    public void setOnTouchListener(OnTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    private boolean isLeftToRight() {
        // If we are pre JB assume always LTR
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return true;
        }
        Configuration config = getResources().getConfiguration();
        return !(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
    }

    /**
     * Khoi tao view
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        if (attrs != null) {
            TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditTextCakaICT, defStyleAttr, 0);
            try {
                mScale = context.getResources().getDisplayMetrics().density;
                typeEditText = styledAttributes.getInt(R.styleable.EditTextCakaICT_typeEditText, NORMAL);
                iconDrawableBefore = styledAttributes.getDrawable(R.styleable.EditTextCakaICT_iconDrawableBefore);
                iconDrawableAfter = styledAttributes.getDrawable(R.styleable.EditTextCakaICT_iconDrawableAfter);
                iconColorBefore = styledAttributes.getColor(R.styleable.EditTextCakaICT_iconColorBefore, 0);
                iconColorAfter = styledAttributes.getColor(R.styleable.EditTextCakaICT_iconColorAfter, 0);
                textHintSize = styledAttributes.getDimension(R.styleable.EditTextCakaICT_textHintSize, textHintSize) / mScale;
                textSize = getTextSize() / mScale;
            } finally {
                styledAttributes.recycle();
            }
            if (iconColorAfter == 0) {
                iconColorAfter = iconColorBefore;
            }
            leftToRight = isLeftToRight();
            if (typeEditText == SHOW_PASSWORD) {
                isShowingPassword = false;
                maskPassword();
                //save the state of whether the password is being shown
                setSaveEnabled(true);
//                setIconVisible(UtilsCakaICT.isNotNullOrEmpty(getText()));
            }
            addTextChangedListener(new TextWatcher_CakaICT(this, this));
            initDrawableIcon();
        }
    }

    private void initDrawableIcon() {
        if (typeEditText == CLEAR_TEXT || typeEditText == USER_ACCOUNT) {
            if (iconDrawableBefore == null) {
                iconDrawableBefore = getResources().getDrawable(R.drawable.ic_close_white_24dp);
            }
        } else if (typeEditText == SHOW_PASSWORD) {
            if (iconDrawableBefore == null) {
                iconDrawableBefore = getResources().getDrawable(R.drawable.ic_visibility_white_24dp);
            }
            if (iconDrawableAfter == null) {
                iconDrawableAfter = getResources().getDrawable(R.drawable.ic_visibility_off_white_24dp);
            }
        }
        isShowingAfter = iconDrawableAfter != null ? true : false;
        iconDrawableBefore.setBounds(0, 0, iconDrawableBefore.getIntrinsicWidth(), iconDrawableBefore.getIntrinsicHeight());
        int min = getPaddingTop() + iconDrawableBefore.getIntrinsicHeight() + getPaddingBottom();
        if (getSuggestedMinimumHeight() < min) {
            setMinimumHeight(min);
        }
        handlingText();
    }

    @Override
    public void onTextChanged(AppCompatMultiAutoCompleteTextView view, String text) {
        if (isFocused()) {
            handlingText();
        }
    }

    private void handlingText() {
        if (getText().length() == 0) {
            setTypeface(null, Typeface.ITALIC);
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textHintSize);
            isShowingAfter = true;
        } else {
            setTypeface(null, Typeface.NORMAL);
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            isShowingAfter = false;
        }
        setIconVisible();
    }

    /**
     *
     */
    protected void setIconVisible() {
        Drawable[] existingDrawables = getCompoundDrawables();
        Drawable left = existingDrawables[0];
        Drawable top = existingDrawables[1];
        Drawable right = existingDrawables[2];
        Drawable bottom = existingDrawables[3];
        Drawable original = (typeEditText == SHOW_PASSWORD)
                ? (isNotNullOrEmpty(getText()) ? (isShowingPassword ? iconDrawableAfter : iconDrawableBefore) : null)
                : isShowingAfter ? iconDrawableAfter : iconDrawableBefore;
        if (isNotNullOrEmpty(original)) {
            original.mutate();
            if (iconColorBefore == 0) {
                setCompoundDrawablesWithIntrinsicBounds(leftToRight ? left : original, top, leftToRight ? original : right, bottom);
            } else {
                Drawable wrapper = DrawableCompat.wrap(original);
                if (typeEditText == SHOW_PASSWORD && isShowingPassword) {
                    DrawableCompat.setTint(wrapper, iconColorAfter);
                } else if (isShowingAfter && isNotNullOrEmpty(iconDrawableAfter)) {
                    DrawableCompat.setTint(wrapper, iconColorAfter);
                    setCompoundDrawablesWithIntrinsicBounds(leftToRight ? left : wrapper, top, leftToRight ? wrapper : right, bottom);
                } else {
                    DrawableCompat.setTint(wrapper, iconColorBefore);
                }
                setCompoundDrawablesWithIntrinsicBounds(leftToRight ? left : wrapper, top, leftToRight ? wrapper : right, bottom);
            }
        } else {
            setCompoundDrawablesWithIntrinsicBounds(leftToRight ? left : null, top, leftToRight ? null : right, bottom);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int left = (loc == Location.LEFT) ? 0 : getWidth() - getPaddingRight() - iconDrawableBefore.getIntrinsicWidth();
        int right = (loc == Location.LEFT) ? getPaddingLeft() + iconDrawableBefore.getIntrinsicWidth() : getWidth();
        boolean tappedX = x >= left && x <= right && y >= 0 && y <= (getBottom() - getTop());
        if (tappedX) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (typeEditText == SHOW_PASSWORD) {
                    togglePasswordVisibility();
                } else {
                    setText("");
                    requestFocus();
                }
            }
            return true;
        }
        if (touchListener != null) {
            return touchListener.onTouch(view, motionEvent);
        }
        return false;
    }

    public void togglePasswordVisibility() {
        // Store the selection
        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();

        // Set transformation method to show/hide password
        if (isShowingPassword) {
            maskPassword();
        } else {
            unmaskPassword();
        }
        // Restore selection
        this.setSelection(selectionStart, selectionEnd);

        // Toggle flag and show indicator
        isShowingPassword = !isShowingPassword;
        setIconVisible();
    }


    //make it visible
    private void unmaskPassword() {
        setTransformationMethod(null);
    }

    //hide it
    private void maskPassword() {
        setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private final static String IS_SHOWING_PASSWORD_STATE_KEY = "IS_SHOWING_PASSWORD_STATE_KEY";
    private final static String SUPER_STATE_KEY = "SUPER_STATE_KEY";

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putBoolean(IS_SHOWING_PASSWORD_STATE_KEY, this.isShowingPassword);
        return bundle;
    }


    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.isShowingPassword = bundle.getBoolean(IS_SHOWING_PASSWORD_STATE_KEY, false);

            if (isShowingPassword) {
                unmaskPassword();
            }
            state = bundle.getParcelable(SUPER_STATE_KEY);
        }
        super.onRestoreInstanceState(state);
    }
}
