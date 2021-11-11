package com.moondroid.project01_meetingapp.helpers.firebase;

import android.content.Context;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class DMFBCrash {
    private static Context mContext = null;
    private static FirebaseCrashlytics mInstance = null;

    private DMFBCrash() {
        super();
    }

    private DMFBCrash(Context context) {
        mContext = context;
    }

    public static synchronized FirebaseCrashlytics initInstance(Context context) {
        if (mInstance == null) {
            mInstance = FirebaseCrashlytics.getInstance();
            mContext = context;
        }
        return mInstance;
    }

    public static synchronized FirebaseCrashlytics getInstance() {
        if (mInstance == null) {
            mInstance = FirebaseCrashlytics.getInstance();
        }
        return mInstance;
    }

    public static void logException(Exception e) {
        DMFBCrash.getInstance().recordException(e);
    }

    public static void logException(Throwable t) {
        DMFBCrash.getInstance().recordException(t);
    }
}
