package com.caka.base.widget.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caka.base.R;
import com.caka.base.widget.progress.widgets.AnnularView;
import com.caka.base.widget.progress.widgets.BackgroundLayout;
import com.caka.base.widget.progress.widgets.BarView;
import com.caka.base.widget.progress.widgets.Determinate;
import com.caka.base.widget.progress.widgets.Helper;
import com.caka.base.widget.progress.widgets.Indeterminate;
import com.caka.base.widget.progress.widgets.PieView;
import com.caka.base.widget.progress.widgets.SpinView;

public class ProgressBarCaka {

    public enum Style {
        PROGRESS_LOGO, SPIN_INDETERMINATE, PIE_DETERMINATE, ANNULAR_DETERMINATE, BAR_DETERMINATE
    }

    // To avoid redundant APIs, all HUD functions will be forward to
    // a custom dialog
    private ProgressDialog mProgressDialog;
    private float mDimAmount;
    private int mWindowColor;
    private float mCornerRadius;
    private Context mContext;
    private int iconProgress;
    private int mAnimateSpeed;

    private int mMaxProgress;
    private boolean mIsAutoDismiss;

    public ProgressBarCaka(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
        mDimAmount = 0;
        //noinspection deprecation
        mWindowColor = context.getResources().getColor(R.color.COLOR_BG_LOADING);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;

        setStyle(Style.PROGRESS_LOGO);
    }

    /**
     * Create a new HUD. Have the same effect as the constructor.
     * For convenient only.
     *
     * @param context Activity context that the HUD bound to
     * @return An unique HUD instance
     */
    public static ProgressBarCaka create(Context context) {
        return new ProgressBarCaka(context);
    }

    /**
     * Create a new HUD. specify the HUD style (if you use a custom view, you need {@code KProgressHUD.create(Context context)}).
     *
     * @param context Activity context that the HUD bound to
     * @param style   One of the KProgressHUD.Style values
     * @return An unique HUD instance
     */
    public static ProgressBarCaka create(Context context, Style style) {
        return new ProgressBarCaka(context).setStyle(style);
    }

    /**
     * Specify the HUD style (not needed if you use a custom view)
     *
     * @param style One of the KProgressHUD.Style values
     * @return Current HUD
     */
    public ProgressBarCaka setStyle(Style style) {
        View view = null;
        switch (style) {
            case PROGRESS_LOGO:
                break;
            case SPIN_INDETERMINATE:
                view = new SpinView(mContext);
                break;
            case PIE_DETERMINATE:
                view = new PieView(mContext);
                break;
            case ANNULAR_DETERMINATE:
                view = new AnnularView(mContext);
                break;
            case BAR_DETERMINATE:
                view = new BarView(mContext);
                break;
            // No custom view style here, because view will be added later
        }
        mProgressDialog.setView(view);
        return this;
    }

    /**
     * Specify the dim area around the HUD, like in Dialog
     *
     * @param dimAmount May take value from 0 to 1.
     *                  0 means no dimming, 1 mean darkness
     * @return Current HUD
     */
    public ProgressBarCaka setDimAmount(float dimAmount) {
        if (dimAmount >= 0 && dimAmount <= 1) {
            mDimAmount = dimAmount;
        }
        return this;
    }

    /**
     * Set HUD size. If not the HUD view will use WRAP_CONTENT instead
     *
     * @param width  in dp
     * @param height in dp
     * @return Current HUD
     */
    public ProgressBarCaka setSize(int width, int height) {
        mProgressDialog.setSize(width, height);
        return this;
    }

    /**
     * Specify the HUD background color
     *
     * @param color ARGB color
     * @return Current HUD
     */
    public ProgressBarCaka setWindowColor(int color) {
        mWindowColor = color;
        return this;
    }

    /**
     * Specify corner radius of the HUD (default is 10)
     *
     * @param radius Corner radius in dp
     * @return Current HUD
     */
    public ProgressBarCaka setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }

    /**
     * Change animate speed relative to default. Only have effect when use with indeterminate style
     *
     * @param scale 1 is default, 2 means double speed, 0.5 means half speed..etc.
     * @return Current HUD
     */
    public ProgressBarCaka setAnimationSpeed(int scale) {
        mAnimateSpeed = scale;
        return this;
    }

    /**
     * Optional label to be displayed on the HUD
     *
     * @return Current HUD
     */
    public ProgressBarCaka setLabel(String label) {
        mProgressDialog.setLabel(label);
        return this;
    }

    /**
     * Optional detail description to be displayed on the HUD
     *
     * @return Current HUD
     */
    public ProgressBarCaka setDetailsLabel(String detailsLabel) {
        mProgressDialog.setDetailsLabel(detailsLabel);
        return this;
    }

    /**
     * Max value for use in one of the determinate styles
     *
     * @return Current HUD
     */
    public ProgressBarCaka setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        return this;
    }

    /**
     * Set current progress. Only have effect when use with a determinate style, or a custom
     * view which implements Determinate interface.
     */
    public void setProgress(int progress) {
        mProgressDialog.setProgress(progress);
    }

    /**
     * Provide a custom view to be displayed.
     *
     * @param view Must not be null
     * @return Current HUD
     */
    public ProgressBarCaka setCustomView(View view) {
        if (view != null) {
            mProgressDialog.setView(view);
        } else {
            throw new RuntimeException("Custom view must not be null!");
        }
        return this;
    }

    /**
     * Specify whether this HUD can be cancelled by using back button (default is false)
     *
     * @return Current HUD
     */
    public ProgressBarCaka setCancellable(boolean isCancellable) {
        mProgressDialog.setCancelable(isCancellable);
        return this;
    }

    /**
     * Specify whether this HUD closes itself if progress reaches max. Default is true.
     *
     * @return Current HUD
     */
    public ProgressBarCaka setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    public ProgressBarCaka setIconProgress(int iconProgress) {
        this.iconProgress = iconProgress;
        return this;
    }

    public ProgressBarCaka show() {
        if (!isShowing()) {
            mProgressDialog.show();
        }
        return this;
    }

    public boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void dismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private class ProgressDialog extends Dialog {

        private ImageView ivProgress;
        private ProgressBar progressBar;
        private Determinate mDeterminateView;
        private Indeterminate mIndeterminateView;
        private View mView;
        private TextView mLabelText;
        private TextView mDetailsText;
        private String mLabel;
        private String mDetailsLabel;
        private FrameLayout mCustomViewContainer;
        private BackgroundLayout mBackgroundLayout;
        private int mWidth, mHeight;

        public ProgressDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.widget_loading_progress_caka);
            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = mDimAmount;
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);

            setCanceledOnTouchOutside(false);

            initViews();
        }

        private void initViews() {
            ivProgress = (ImageView) findViewById(R.id.ivProgress);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            mBackgroundLayout = (BackgroundLayout) findViewById(R.id.background);
            mBackgroundLayout.setBaseColor(mWindowColor);
            mBackgroundLayout.setCornerRadius(mCornerRadius);
            if (mWidth != 0) {
                updateBackgroundSize();
            }

            mCustomViewContainer = (FrameLayout) findViewById(R.id.container);
            addViewToFrame(mView);

            if (mDeterminateView != null) {
                mDeterminateView.setMax(mMaxProgress);
            }
            if (mIndeterminateView != null) {
                mIndeterminateView.setAnimationSpeed(mAnimateSpeed);
            }

            mLabelText = (TextView) findViewById(R.id.label);
            if (mLabel != null) {
                mLabelText.setText(mLabel);
                mLabelText.setVisibility(View.VISIBLE);
            } else {
                mLabelText.setVisibility(View.GONE);
            }
            mDetailsText = (TextView) findViewById(R.id.details_label);
            if (mDetailsLabel != null) {
                mDetailsText.setText(mDetailsLabel);
                mDetailsText.setVisibility(View.VISIBLE);
            } else {
                mDetailsText.setVisibility(View.GONE);
            }

            if (iconProgress > 0) {
                Drawable myDrawable = getContext().getResources().getDrawable(iconProgress);
                ivProgress.setImageDrawable(myDrawable);
                ivProgress.setVisibility(View.VISIBLE);
            } else {
                ivProgress.setVisibility(View.GONE);
            }
        }

        private void addViewToFrame(View view) {
            if (view == null) return;
            int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
            mCustomViewContainer.addView(view, params);
        }

        private void updateBackgroundSize() {
            ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
            params.width = Helper.dpToPixel(mWidth, getContext());
            params.height = Helper.dpToPixel(mHeight, getContext());
            mBackgroundLayout.setLayoutParams(params);
        }

        public void setProgress(int progress) {
            if (mDeterminateView != null) {
                mDeterminateView.setProgress(progress);
                if (mIsAutoDismiss && progress >= mMaxProgress) {
                    dismiss();
                }
            }
        }

        public void setView(View view) {
            if (view != null) {
                if (view instanceof Determinate) {
                    mDeterminateView = (Determinate) view;
                }
                if (view instanceof Indeterminate) {
                    mIndeterminateView = (Indeterminate) view;
                }
                mView = view;
                if (isShowing()) {
                    mCustomViewContainer.removeAllViews();
                    addViewToFrame(view);
                }
            }
        }

        public void setLabel(String label) {
            mLabel = label;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }

        public void setDetailsLabel(String detailsLabel) {
            mDetailsLabel = detailsLabel;
            if (mDetailsText != null) {
                if (detailsLabel != null) {
                    mDetailsText.setText(detailsLabel);
                    mDetailsText.setVisibility(View.VISIBLE);
                } else {
                    mDetailsText.setVisibility(View.GONE);
                }
            }
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
            if (mBackgroundLayout != null) {
                updateBackgroundSize();
            }
        }

        @Override
        public void show() {
            super.show();
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(1000);
            progressBar.setAnimation(anim);
            progressBar.startAnimation(anim);
        }
    }



}
