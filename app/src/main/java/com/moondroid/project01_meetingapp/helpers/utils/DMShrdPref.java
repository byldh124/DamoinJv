package com.moondroid.project01_meetingapp.helpers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.moondroid.project01_meetingapp.R;

public class DMShrdPref {
    private final String TAG = DMShrdPref.class.getSimpleName();

    private static DMShrdPref mInstance;
    private static SharedPreferences mPref = null;
    private static final String PREF_NAME = "damoim_pref";

    private DMShrdPref(Context context){
    }

    public static synchronized void initializeInstance(Context context){
        if (mInstance == null){
            mInstance = new DMShrdPref(context);

            PreferenceManager.setDefaultValues(context, PREF_NAME, Context.MODE_PRIVATE, R.xml.preferences, true);

            mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public static synchronized DMShrdPref getInstance(Context context){
        if (mInstance == null){
            DMShrdPref.initializeInstance(context);
        }
        return mInstance;
    }

    public long getValue(String key){
        try{
            return mPref.getLong(key, 0);
        } catch (Exception e){
            try {
                return (mPref.getBoolean(key, false)) ? 1L : 0L;
            }
            catch (Exception e2)
            {
                // Due to some ridiculous Android bug with 'android:defaultValue' and PreferenceActivity,
                // we need to save everything only as strings
                return Integer.parseInt(mPref.getString(key, "0"));
            }
        }
    }

    public String getString(String key){
        try {
            return mPref.getString(key, "");
        }
        catch (Exception e)
        {
            try {
                return (mPref.getBoolean(key, false)) ? "1" : "0";
            }
            catch (Exception e2)
            {
                // Due to some ridiculous Android bug with 'android:defaultValue' and PreferenceActivity,
                // we need to save everything only as strings
                return String.valueOf(mPref.getLong(key, 0L));
            }
        }
    }

    public boolean getBoolean(String key){
        try{
            return mPref.getBoolean(key, false);
        }catch(Exception e){
            return false;
        }
    }

    public void setLong(String key, long value) {
        mPref.edit().putLong(key, value).apply();
    }

    public void setString(String key, String value){
        mPref.edit().putString(key, value).apply();
    }

    public void setBoolean(String key, boolean value) {
        mPref.edit().putBoolean(key, value).apply();
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }

}
