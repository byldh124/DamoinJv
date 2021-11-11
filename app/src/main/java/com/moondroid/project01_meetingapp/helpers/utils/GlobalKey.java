package com.moondroid.project01_meetingapp.helpers.utils;

public class GlobalKey {
    public static interface NTWRK_RTN_TYPE{
        String CODE = "code";
        String MSG = "message";
        String RESULT = "result";

        int SUCCESS = 1000;
        int FAIL = 2000;
        int NOT_EXIST = 2001;
    }

    public static interface INTENT_PARAM_TYPE{
        String SEND_ACTIVITY = "sendActivity";
        String INTEREST = "interest";
        String ICON_URL = "iconUrl";
        String LOCATION = "location";
    }

    public static interface ACTIVITY_CODE{
        int INTRO_ACTIVITY = 1;
        int LOGIN_ACTIVITY = 2;
        int MAIN_ACTIVITY = 3;
        int CREATE_ACTIVITY = 4;
        int ACCOUNT_ACTIVITY = 5;
        int OPTION_MODIFY_ACTIVITY = 14;
    }

    public static interface REQUEST_CODE{
        int MAIN01 = 31;
        int ACCOUNT01 = 51;
        int ACCOUNT02 = 52;
        int CREATE01 = 41;
        int CREATE02 = 42;
        int CREATE03 = 43;
    }

}
