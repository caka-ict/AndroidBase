package com.caka.base.widget.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caka.base.R;

import static com.caka.base.utils.AppUtils.isRTL;
import static com.caka.base.utils.AppUtils.toDp;

@SuppressLint("ViewConstructor")
public class ToastCaka  extends LinearLayout {

    private int cornerRadius;
    private int backgroundColor;
    private int strokeColor;
    private int strokeWidth;
    private int iconStart;
    private int iconEnd;
    private int textColor;
    private int font;
    private int length;
    private int style;
    private float textSize;
    private boolean isTextSizeFromStyleXml = false;
    private boolean solidBackground;
    private boolean textBold;
    private String text;
    private TypedArray typedArray;
    private TextView textView;
    private int gravity;
    private Toast toast;
    private LinearLayout rootLayout;

    public static ToastCaka makeText(@NonNull Context context, String text, int length, @StyleRes int style) {
        return new ToastCaka(context, text, length, style);
    }

    public static ToastCaka makeText(@NonNull Context context, String text, @StyleRes int style) {
        return new ToastCaka(context, text, Toast.LENGTH_SHORT, style);
    }

    private ToastCaka(@NonNull Context context, String text, int length, @StyleRes int style) {
        super(context);
        this.text = text;
        this.length = length;
        this.style = style;
    }

    private ToastCaka(ToastCaka.Builder builder) {
        super(builder.context);
        this.backgroundColor = builder.backgroundColor;
        this.cornerRadius = builder.cornerRadius;
        this.iconEnd = builder.iconEnd;
        this.iconStart = builder.iconStart;
        this.strokeColor = builder.strokeColor;
        this.strokeWidth = builder.strokeWidth;
        this.solidBackground = builder.solidBackground;
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.textBold = builder.textBold;
        this.font = builder.font;
        this.text = builder.text;
        this.gravity = builder.gravity;
        this.length = builder.length;
    }

    private void initStyleableToast() {
        View v = inflate(getContext(), R.layout.widget_toast, null);
        rootLayout = (LinearLayout) v.getRootView();
        textView = v.findViewById(R.id.textview);
        if (style > 0) {
            typedArray = getContext().obtainStyledAttributes(style, R.styleable.ToastCaka);
        }

        makeShape();
        makeTextView();
        makeIcon();

        // Very important to recycle AFTER the make() methods!
        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    public void show() {
        initStyleableToast();
        toast = new Toast(getContext());
        toast.setGravity(gravity, 0, gravity == Gravity.CENTER ? 0 : toast.getYOffset());
        toast.setDuration(length == Toast.LENGTH_LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setView(rootLayout);
        toast.show();
    }


    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }


    private void makeShape() {
        loadShapeAttributes();
        GradientDrawable gradientDrawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            gradientDrawable = (GradientDrawable) rootLayout.getBackground().mutate();
        }else {
            gradientDrawable = (GradientDrawable) rootLayout.getBackground();
        }

        gradientDrawable.setAlpha(getResources().getInteger(R.integer.defaultBackgroundAlpha));

        if (strokeWidth > 0) {
            gradientDrawable.setStroke(strokeWidth, strokeColor);
        }
        if (cornerRadius > -1) {
            gradientDrawable.setCornerRadius(cornerRadius);
        }
        if (backgroundColor != 0) {
            gradientDrawable.setColor(backgroundColor);
        }
        if (solidBackground) {
            gradientDrawable.setAlpha(getResources().getInteger(R.integer.fullBackgroundAlpha));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootLayout.setBackground(gradientDrawable);
        }else {
            rootLayout.setBackgroundDrawable(gradientDrawable);
        }
    }

    private void makeTextView() {
        loadTextViewAttributes();
        textView.setText(text);
        if (textColor != 0) {
            textView.setTextColor(textColor);
        }
        if (textSize > 0) {
            textView.setTextSize(isTextSizeFromStyleXml ? 0 : TypedValue.COMPLEX_UNIT_SP, textSize);
        }
        if (font > 0) {
            textView.setTypeface(ResourcesCompat.getFont(getContext(), font), textBold ? Typeface.BOLD : Typeface.NORMAL);
        }
        if (textBold && font == 0) {
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }
    }


    private void makeIcon() {
        loadIconAttributes();
        int paddingVertical = (int) getResources().getDimension(R.dimen.toast_vertical_padding);
        int paddingHorizontal1 = (int) getResources().getDimension(R.dimen.toast_horizontal_padding_icon_side);
        int paddingNoIcon = (int) getResources().getDimension(R.dimen.toast_horizontal_padding_empty_side);
        int iconSize = (int) getResources().getDimension(R.dimen.icon_size);

        if (iconStart != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), iconStart);
            if (drawable != null) {
                drawable.setBounds(0, 0, iconSize, iconSize);
                TextViewCompat.setCompoundDrawablesRelative(textView, drawable, null, null, null);
                if (isRTL()) {
                    rootLayout.setPadding(paddingNoIcon, paddingVertical, paddingHorizontal1, paddingVertical);
                } else {
                    rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingNoIcon, paddingVertical);
                }
            }
        }

        if (iconEnd != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), iconEnd);
            if (drawable != null) {
                drawable.setBounds(0, 0, iconSize, iconSize);
                TextViewCompat.setCompoundDrawablesRelative(textView, null, null, drawable, null);
                if (isRTL()) {
                    rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingNoIcon, paddingVertical);
                } else {
                    rootLayout.setPadding(paddingNoIcon, paddingVertical, paddingHorizontal1, paddingVertical);
                }
            }
        }

        if (iconStart != 0 && iconEnd != 0) {
            Drawable drawableLeft = ContextCompat.getDrawable(getContext(), iconStart);
            Drawable drawableRight = ContextCompat.getDrawable(getContext(), iconEnd);
            if (drawableLeft != null && drawableRight != null) {
                drawableLeft.setBounds(0, 0, iconSize, iconSize);
                drawableRight.setBounds(0, 0, iconSize, iconSize);
                textView.setCompoundDrawables(drawableLeft, null, drawableRight, null);
                rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingHorizontal1, paddingVertical);
            }
        }
    }

    /**
     * loads style attributes from styles.xml if a style resource is used.
     */

    private void loadShapeAttributes() {
        if (style == 0) {
            return;
        }

        int defaultBackgroundColor = ContextCompat.getColor(getContext(), R.color.default_background_color);
        int defaultCornerRadius = (int) getResources().getDimension(R.dimen.default_corner_radius);

        solidBackground = typedArray.getBoolean(R.styleable.ToastCaka_solidBackground, false);
        backgroundColor = typedArray.getColor(R.styleable.ToastCaka_colorBackground, defaultBackgroundColor);
        cornerRadius = (int) typedArray.getDimension(R.styleable.ToastCaka_radius, defaultCornerRadius);
        length = typedArray.getInt(R.styleable.ToastCaka_length, 0);
        gravity = typedArray.getInt(R.styleable.ToastCaka_gravity, Gravity.BOTTOM);

        if (gravity == 1) {
            gravity = Gravity.CENTER;
        } else if (gravity == 2) {
            gravity = Gravity.TOP;
        }

        if (typedArray.hasValue(R.styleable.ToastCaka_strokeColor) && typedArray.hasValue(R.styleable.ToastCaka_strokeWidth)) {
            strokeWidth = (int) typedArray.getDimension(R.styleable.ToastCaka_strokeWidth, 0);
            strokeColor = typedArray.getColor(R.styleable.ToastCaka_strokeColor, Color.TRANSPARENT);
        }
    }

    private void loadTextViewAttributes() {
        if (style == 0) {
            return;
        }

        textColor = typedArray.getColor(R.styleable.ToastCaka_textColor, textView.getCurrentTextColor());
        textBold = typedArray.getBoolean(R.styleable.ToastCaka_textBold, false);
        textSize = typedArray.getDimension(R.styleable.ToastCaka_textSize, 0);
        font = typedArray.getResourceId(R.styleable.ToastCaka_font, 0);
        isTextSizeFromStyleXml = textSize > 0;
    }


    private void loadIconAttributes() {
        if (style == 0) {
            return;
        }
        iconStart = typedArray.getResourceId(R.styleable.ToastCaka_iconStart, 0);
        iconEnd = typedArray.getResourceId(R.styleable.ToastCaka_iconEnd, 0);
    }

    public static class Builder {
        private int cornerRadius = -1;
        private int backgroundColor;
        private int strokeColor;
        private int strokeWidth;
        private int iconStart;
        private int iconEnd;
        private int textColor;
        private int font;
        private int length;
        private float textSize;
        private boolean solidBackground;
        private boolean textBold;
        private String text;
        private int gravity = Gravity.BOTTOM;
        private ToastCaka toast;
        private final Context context;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder textColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder textBold() {
            this.textBold = true;
            return this;
        }

        public Builder textSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * @param font A font resource id like R.font.somefont as introduced with the new font api in Android 8
         */
        public Builder font(@FontRes int font) {
            this.font = font;
            return this;
        }

        public Builder backgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        /**
         * This call will make the StyleableToast's background completely solid without any opacity.
         */
        public Builder solidBackground() {
            this.solidBackground = true;
            return this;
        }

        public Builder stroke(int strokeWidth, @ColorInt int strokeColor) {
            this.strokeWidth = toDp(context, strokeWidth);
            this.strokeColor = strokeColor;
            return this;
        }

        /**
         * @param cornerRadius Sets the corner radius of the StyleableToast's shape.
         */
        public Builder cornerRadius(int cornerRadius) {
            this.cornerRadius = toDp(context, cornerRadius);
            return this;
        }

        public Builder iconStart(@DrawableRes int iconStart) {
            this.iconStart = iconStart;
            return this;
        }

        public Builder iconEnd(@DrawableRes int iconEnd) {
            this.iconEnd = iconEnd;
            return this;
        }

        /**
         * Sets where the StyleableToast will appear on the screen
         */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * @param length {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
         */
        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public void show() {
            toast = new ToastCaka(this);
            toast.show();
        }
    }
}
