package com.caka.base.widget.progress.widgets;

import android.content.Context;

/**
 * Mo ta class or interface
 * Created by: Phung Dang Hoan. HoanPD1.
 * Create time: 2/28/2017
 * Update time: 2/28/2017
 * Version: 1.0
 */

public class Helper {
    private static float scale;

    public static int dpToPixel(float dp, Context context) {
        if (Float.compare(scale, 0.0f) == 0) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dp * scale);
    }
}
