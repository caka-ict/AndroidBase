package com.caka.base.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.caka.base.ui.GlobalActivity;

public class KeyboardUtils {


    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * Hide keyboard.
     * <p>
     * <pre>
     * <code>KeyboardUtils.hideKeyboard(getActivity(), searchField);</code>
     * </pre>
     *
     * @param context
     * @param field
     */
    public static void hideKeyboard(Context context, EditText field) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param view 视图
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) AppUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Show keyboard with a 100ms delay.
     * <p>
     * <pre>
     * <code>KeyboardUtils.showDelayedKeyboard(getActivity(), searchField);</code>
     * </pre>
     *
     * @param context
     * @param view
     */
    public static void showDelayedKeyboard(Context context, View view) {
        showDelayedKeyboard(context, view, 100);
    }

    /**
     * Show keyboard with a custom delay.
     * <p>
     * <pre>
     * <code>KeyboardUtils.showDelayedKeyboard(getActivity(), searchField, 500);</code>
     * </pre>
     *
     * @param context
     * @param view
     * @param delay
     */
    public static void showDelayedKeyboard(final Context context, final View view, final int delay) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }

        }.execute();
    }

    public static void showSoftInput(GlobalActivity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void showSoftInput(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) AppUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) AppUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
