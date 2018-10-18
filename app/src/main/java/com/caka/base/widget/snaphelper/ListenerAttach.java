package com.caka.base.widget.snaphelper;

import android.support.annotation.IdRes;

public interface ListenerAttach {

    /**
     * attach OnClickListener for view
     *
     * @param viewId view id
     */
    void attachOnClickListener(@IdRes int viewId);

    /**
     * attach OnTouchListener for view
     *
     * @param viewId view id
     */
    void attachOnTouchListener(@IdRes int viewId);

    /**
     * attach OnLongClickListener for view
     *
     * @param viewId view id
     */
    void attachOnLongClickListener(@IdRes int viewId);

    /**
     * attach CompoundButton.OnCheckedChangeListener for CompoundButton
     *
     * @param viewId CompoundButton view id
     */
    void attachOnCheckedChangeListener(@IdRes int viewId);

    /**
     * load image
     *
     * @param viewId
     */
    void attachImageLoader(@IdRes int viewId);
}
