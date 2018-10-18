package com.caka.base.widget.edittext;

import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Hoanpd1 on 6/7/2017.
 */

public class TextWatcher_CakaICT  implements TextWatcher {

    public interface TextWatcherListener {

        void onTextChanged(AppCompatMultiAutoCompleteTextView view, String text);

    }

    private final AppCompatMultiAutoCompleteTextView view;
    private final TextWatcherListener listener;


    public TextWatcher_CakaICT(AppCompatMultiAutoCompleteTextView editText, TextWatcherListener listener) {
        this.view = editText;
        this.listener = listener;
    }


    boolean hint;
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        listener.onTextChanged(view, s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // pass
    }

    @Override
    public void afterTextChanged(Editable s) {
        // pass
    }
}
