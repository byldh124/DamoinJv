package com.moondroid.project01_meetingapp.network;

import com.moondroid.project01_meetingapp.data.model.GroupInfo;
import com.moondroid.project01_meetingapp.data.model.MoimVO;
import com.moondroid.project01_meetingapp.data.model.UserInfo;

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

    @POST(URLMngr.SAVE_USER)
    Call<String> saveUserBaseDataToAccountActivity(@Body UserInfo userBaseData);  //**

    @Multipart
    @POST(URLMngr.UPDATE_USER)                                                      //**
    Call<String> updateUserProfileImg(@PartMap Map<String, String> dataPart, @Part MultipartBody.Part filePart);

    @GET(URLMngr.CHECK_ID)
    Call<String> checkUserId(@Query("userId") String userId);                       //**

    @GET(URLMngr.CHECK_GROUP_NAME)
    Call<String> checkGroupName(@Query("meetName") String meetName);                 //**

    @GET(URLMngr.UPDATE_INTEREST)                                                   //**
    Call<String> updateUserInterest(@Query("userId") String userId, @Query("userInterest") String userInterest);

    @GET(URLMngr.USER_INFO)
    Call<String> getUserInfo(@Query("userId") String userId);     //**

    @Multipart
    @POST(URLMngr.SAVE_GROUP)                                                        //**
    Call<String> createGroup(@PartMap Map<String, String> dataPart, @Part MultipartBody.Part filePart);

    @GET(URLMngr.GET_GROUP)                                                           //**
    Call<String> getGroupList();

    @Multipart
    @POST(URLMngr.UPDATE_MEET)                                                          //**
    Call<String> updateItemBaseDataToModifyActivity(@PartMap Map<String, String> dataPart, @Part MultipartBody.Part titlePart, @Part MultipartBody.Part introPart);

    @GET(URLMngr.SAVE_USER_MEET_DT)                                                     //**
    Call<String> saveUserMeetData(@Query("userId") String userId, @Query("meetName") String meetName);

    @GET(URLMngr.CHECK_USER_MEET_DT)                                                    //**
    Call<String> checkUserMeetData(@Query("userId") String userId, @Query("meetName") String meetName);

    @GET(URLMngr.LOAD_MEMBERS)
    Call<ArrayList<UserInfo>> loadMembers(@Query("meetName") String meetName);

    @GET(URLMngr.LOAD_USERS_MEET)
    Call<ArrayList<GroupInfo>> loadUserMeetItem(@Query("userId") String userId);


    @GET(URLMngr.SAVE_USER_KAKAO)
    Call<String> saveUserBaseDataToKakao(@Query("userId") String userId, @Query("userName") String userName, @Query("userProfileImgUrl") String profileImgUrl);

    @GET(URLMngr.SAVE_FCM)
    Call<String> saveFCMToken(@Query("userId") String userId, @Query("FCMToken") String FCMToken);

    @GET(URLMngr.SEND_FCM)
    Call<String> sendFCMMessage(@Query("meetName") String meetName, @Query("userId") String userId);

    @POST(URLMngr.SAVE_MOIM)
    Call<MoimVO> saveMoimInfo(@Body MoimVO moimVO);

    @GET(URLMngr.LOAD_MOIM)
    Call<ArrayList<MoimVO>> loadMoims(@Query("meetName") String meetName);

    @GET(URLMngr.LOAD_CHAT)
    Call<String> loadChatInfo (@Query("userId") String userId);

    @GET(URLMngr.SEND_FCM_MOIM)
    Call<String> sendFCMMessageMoim(@Query("meetName") String meetName);

    @GET(URLMngr.LOAD_MOIM_ALL)
    Call<ArrayList<MoimVO>> loadMoimsAll();

    @GET(URLMngr.INSERT_FAVOR)
    Call<String> insertFavor(@Query("userId") String userId, @Query("meetName") String meetName);

    @GET(URLMngr.DELETE_FAVOR)
    Call<String> deleteFavor(@Query("userId") String userId, @Query("meetName") String meetName);

    @GET(URLMngr.LOAD_USER_FAVOR)
    Call<ArrayList<GroupInfo>> loadUserFavoriteItem(@Query("userId") String userId);

    @GET(URLMngr.CHECK_USER_FAVOR)
    Call<String> checkFavorite(@Query("userId") String userId, @Query("meetName") String meetName);

    @GET(URLMngr.SAVE_RECENT)
    Call<String> uploadRecentMoim(@Query("userId") String userId, @Query("meetName") String meetName, @Query("lastTime")String lastTime);

    @GET(URLMngr.LOAD_RECENT)
    Call<ArrayList<GroupInfo>> loadUserRecentViewItem(@Query("userId") String userId);

    @GET(URLMngr.LOAD_JOIN)
    Call<ArrayList<UserInfo>> loadJoinMembers(@Query("joinMembers") String joinMembers);

    @GET(URLMngr.ADD_JOIN)
    Call<String> addJoinMember(@Query("meetName") String meetName, @Query("date") String date, @Query("joinMember") String joinMember);

    @GET(URLMngr.UPDATE_SETTING)
    Call<String> updateFCMSetting(@Query("userId") String userId, @Query("target") String target, @Query("value") String Value);

    @GET(URLMngr.LOAD_SETTING)
    Call<String> loadFCMSetting(@Query("userId") String userId);
}
