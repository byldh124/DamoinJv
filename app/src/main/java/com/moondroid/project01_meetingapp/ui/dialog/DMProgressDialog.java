package com.moondroid.project01_meetingapp.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class DMProgressDialog extends ProgressDialog {

    private static Context mContext;

    private static ProgressDialog mInstance = null;

    private DMProgressDialog(Context context) {
        super(context);
        mContext = context;
    }

    public static synchronized ProgressDialog getInstance(){
        if (mInstance == null){
            mInstance = new ProgressDialog(mContext);
        }
        return mInstance;
    }
}
