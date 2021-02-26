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

    @GET("/damoim/checkMeetName.php")
    Call<String> checkMeetName(@Query("meetName") String meetName);

    @GET("/damoim/updateUserInterest.php")
    Call<String> updateUserInterest(@Query("userId") String userId, @Query("userInterest") String userInterest);

    @GET("/damoim/loadUserBaseDBOnIntro.php")
    Call<UserBaseVO> loadUserBaseDBToIntroActivity(@Query("userId") String userId);

    @GET("/damoim/saveUserMeetItem.php")
    Call<String> saveUserMeetItem(@Query("userId") String userId, @Query("meetName") String meetName);

    @Multipart
    @POST("/damoim/saveItemBaseDB.php")
    Call<String> saveItemBaseDataToCreateActivity(@PartMap Map<String, String> dataPart, @Part MultipartBody.Part filePart);

    @GET("/damoim/getItemBaseData.php")
    Call<ArrayList<ItemBaseVO>> getItemBaseDataOnMain();

    @Multipart
    @POST("/damoim/updateItemBaseDB.php")
    Call<String> updateItemBaseDataToModifyActivity(@PartMap Map<String, String> dataPart, @Part MultipartBody.Part titlePart, @Part MultipartBody.Part introPart);

    @GET("/damoim/saveUserMeetData.php")
    Call<String> saveUserMeetData(@Query("userId") String userId, @Query("meetName") String meetName);

    @GET("/damoim/checkUserMeetData.php")
    Call<String> checkUserMeetData(@Query("userId") String userId, @Query("meetName") String meetName);

    @GET("/damoim/loadMembers.php")
    Call<ArrayList<UserBaseVO>> loadMembers(@Query("meetName") String meetName);

    @GET("/damoim/loadUserMeetItem.php")
    Call<ArrayList<ItemBaseVO>> loadUserMeetItem(@Query("userId") String userId);


    @GET("/damoim/saveUserBaseDataToKakako.php")
    Call<String> saveUserBaseDataToKakao(@Query("userId") String userId, @Query("userName") String userName, @Query("userProfileImgUrl") String profileImgUrl);

    @GET("/damoim/saveFCMToken.php")
    Call<String> saveFCMToken(@Query("userId") String userId, @Query("FCMToken") String FCMToken);

    @GET("/damoim/sendFCMMessage.php")
    Call<String> sendFCMMessage(@Query("meetName") String meetName, @Query("userId") String userId);
}
