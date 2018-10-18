package com.caka.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.BuildConfig;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.TypedValue;
import com.caka.base.utils.log.Log;
import com.caka.base.widget.progress.ProgressBarCaka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppUtils {

    // Mang cac ky tu goc co dau
    private static char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự',};

    // Mang cac ky tu thay the khong dau
    private static char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u',};


    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private Activity activeActivity;



    public AppUtils(@NonNull Application context) {
        AppUtils.context = context.getApplicationContext();
        setupActivityListener(context);
//        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Configuration changeLanguage(String language) {
        Resources res = getContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(language.toLowerCase()));
        } else {
            conf.locale = new Locale(language.toLowerCase());
        }
        res.updateConfiguration(conf, dm);
        return conf;
    }


    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    public static String getString(int id) {
        return context.getString(id);
    }

    /**
     * check Object isEmpty
     *
     * @param obj
     * @return
     * @Created by CakaICT
     */
    public static boolean isNotNullOrEmpty(Object obj) {
        return !isNullOrEmpty(obj);
    }

    /**
     * check Object isEmpty
     *
     * @param obj
     * @return
     * @Created by CakaICT
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof SpannableStringBuilder && obj.toString().length() == 0) {
            return true;
        }
        if (obj instanceof SpannableString && obj.toString().length() == 0) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    public static String replaceString(String string, String oldSubstring, String newSubstring) {
        String result = string;
        if ((string != null) && (string.length() > 0) && (oldSubstring != null) && (oldSubstring.length() > 0) && (newSubstring != null)) {
            int pos = string.indexOf(oldSubstring);
            result = string.substring(0, pos) + newSubstring + string.substring(pos + oldSubstring.length());
        }
        return result;
    }

    /**
     * Bo dau 1 chuoi
     *
     * @param s
     * @return
     */
    public static String removeAccentVN(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccentVN(sb.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * Bo dau 1 ky tu
     *
     * @param ch
     * @return
     */
    public static char removeAccentVN(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static ProgressBarCaka showLoading(AppCompatActivity activity, int logoLoading) {
        ProgressBarCaka progressLoading = ProgressBarCaka.create(activity)
                .setStyle(ProgressBarCaka.Style.PROGRESS_LOGO)
                .setIconProgress(logoLoading)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        progressLoading.show();
        return progressLoading;
    }

    public static ProgressBarCaka showLoading(AppCompatActivity activity, int logoLoading, String labelLoading) {
        ProgressBarCaka progressLoading = ProgressBarCaka.create(activity)
                .setStyle(ProgressBarCaka.Style.PROGRESS_LOGO)
                .setIconProgress(logoLoading)
                .setLabel(labelLoading)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        progressLoading.show();
        return progressLoading;
    }

    public static ProgressBarCaka showLoading(AppCompatActivity activity, String labelLoading) {
        ProgressBarCaka progressLoading = ProgressBarCaka.create(activity)
                .setStyle(ProgressBarCaka.Style.PROGRESS_LOGO)
                .setLabel(labelLoading)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        progressLoading.show();
        return progressLoading;
    }

    public static String objectToJSON(Object object) {
        Class c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            String value = null;
            try {
                value = String.valueOf(field.get(object));
            } catch (IllegalAccessException e) {
                Log.e(Log.class.getName() + " - Function: objectToJSON ", "error: " + e.getMessage());
                throw new RuntimeException(e);
            }
            try {
                jsonObject.put(name, value);
            } catch (JSONException e) {
                Log.e(Log.class.getName() + " - Function: objectToJSON ", "error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        if (BuildConfig.DEBUG) System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }

    public static String listObjectToJSON(List list) {
        JSONArray jsonArray = new JSONArray();
        for (Object i : list) {
            String jstr = objectToJSON(i);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jstr);
            } catch (JSONException e) {
                Log.e(Log.class.getName() + " - Function: listObjectToJSON ", "error: " + e.getMessage());
                throw new RuntimeException(e);
            }
            jsonArray.put(jsonObject);
        }
        if (BuildConfig.DEBUG) System.out.println(jsonArray.toString());
        return jsonArray.toString();
    }

    public static int toDp(Context context, int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static boolean isRTL() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) AppUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void setupActivityListener(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
            }
            @Override
            public void onActivityPaused(Activity activity) {
                activeActivity = null;
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public Activity getActiveActivity(){
        return activeActivity;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
