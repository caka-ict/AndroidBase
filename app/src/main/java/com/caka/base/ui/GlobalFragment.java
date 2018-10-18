package com.caka.base.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class GlobalFragment extends Fragment {

    private GlobalActivity activity;

    public GlobalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GlobalActivity) {
            GlobalActivity activity = (GlobalActivity) context;
            this.activity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    public GlobalActivity getBaseActivity() {
        return this.activity;
    }

    public boolean isNetworkConnected() {
        return activity != null && activity.isNetworkConnected();
    }

    public void hideKeyboard() {
        if (activity != null) {
            activity.hideKeyboard();
        }
    }

    public void openActivityOnTokenExpire() {
        if (activity != null) {
            activity.openLoginActivity();
        }
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }



}
