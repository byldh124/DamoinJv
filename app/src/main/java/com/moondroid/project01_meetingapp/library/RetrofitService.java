package com.moondroid.project01_meetingapp.library;

import com.moondroid.project01_meetingapp.variableobject.ItemBaseVO;
import com.moondroid.project01_meetingapp.variableobject.UserBaseVO;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {

    @POST("/damoim/saveUserBaseDB.php")
    Call<UserBaseVO> saveUserBaseDataToAccountActivity(@Body UserBaseVO userBaseData);

    @Multipart
    @POST("/damoim/updateUserBaseDB.php")
    Call<String> updateUserProfileImg(@PartMap Map<String, String> dataPart, @Part MultipartBody.Part filePart);

    @GET("/damoim/checkUserId.php")
    Call<String> checkUserId(@Query("userId") String userId);

    @GET("/damoim/updateUserInterest.php")
    Call<String> updateUserInterest(@Query("userId") String userId, @Query("userInterest") String userInterest);

    @GET("/damoim/loadUserBaseDBOnIntro.php")
    Call<UserBaseVO> loadUserBaseDBToIntroActivity(@Query("userId") String userId);

    @GET("/damoim/saveUserMeetItem.php")
    Call<String> saveUserMeetItem(@Query("userId") String userId, @Query("meetName") String meetName);

    @Multipart
    @POST("/damoim/saveItemBaseDB.php")
    Call<ItemBaseVO> saveItemBaseDataToCreateActivity(@Body ItemBaseVO itemBaseData, @Part MultipartBody.Part filePart);

}
