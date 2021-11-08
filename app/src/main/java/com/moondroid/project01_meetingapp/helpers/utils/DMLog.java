package com.moondroid.project01_meetingapp.helpers.utils;

import com.moondroid.project01_meetingapp.network.URLMngr;

public class DMLog {
    static final boolean LOG = URLMngr.ENV == "TEST";

    public static void i(String tag, String string) {       // Info
        if (LOG) android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {       // Error
        if (LOG) android.util.Log.e(tag, string);
    }

    public static void d(String tag, String string) {       // Debug
        if (LOG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {       // Verbose
        if (LOG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {       // Warning
        if (LOG) android.util.Log.w(tag, string);
    }
}
