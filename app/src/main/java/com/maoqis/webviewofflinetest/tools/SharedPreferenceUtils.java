package com.maoqis.webviewofflinetest.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.util.Log;

import com.maoqis.webviewofflinetest.TestApplication;
import com.orhanobut.logger.Logger;

/***
 * SharedPreferences工具类
 * 使用时保证使用的地方是主线程
 */
public class SharedPreferenceUtils {
    private static final String TAG = "SharedPreferenceUtils";
    private static final String SP_KEY = "sp_test";

    /**  */
    private static SharedPreferenceUtils sInstance;
    private final Context mContext;
    private final SharedPreferences mSharedPreferences;


    public static SharedPreferenceUtils getInstance() {
        if (sInstance == null){
            init(TestApplication.context);
        }
        return sInstance;
    }

    public static void init(Context context) {
        // 加密密钥跟机器相关
        sInstance = new SharedPreferenceUtils(context);
    }

    private SharedPreferenceUtils(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
    }

    public void clear() {
        Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }



    public boolean getBoolean(String key, boolean defaultValue) {
        boolean aBoolean = mSharedPreferences.getBoolean(key, defaultValue);
        Logger.d(TAG, "getBoolean() called with: " + "key = [" + key + "], defaultValue = [" + defaultValue + "]"+"ret="+aBoolean);
        return aBoolean;

    }

    public void putBoolean(String key, boolean value) {
        Log.d(TAG, "putBoolean() called with: key = [" + key + "], value = [" + value + "]");
        Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
}
