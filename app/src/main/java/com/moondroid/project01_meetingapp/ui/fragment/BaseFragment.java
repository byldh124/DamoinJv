package com.moondroid.project01_meetingapp.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moondroid.project01_meetingapp.helpers.firebase.DMFBCrash;
import com.moondroid.project01_meetingapp.helpers.utils.DMLog;

public class BaseFragment extends Fragment {

    private final static String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void logException(Exception e){
        DMLog.e(TAG, "[BaseFragment::logException] E : " + e.toString());
        DMFBCrash.logException(e);
    }
}
