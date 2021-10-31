package com.moondroid.project01_meetingapp.helpers.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LinearLayoutManagerWrapper extends LinearLayoutManager {

    public LinearLayoutManagerWrapper(Context context) {
        super(context);
    }

    public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //LinearLayoutManager 발생하는 오류 해결을 위한 Wrapper Class
    //데이터 재로드시에 자주 발생함.
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
