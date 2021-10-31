package com.moondroid.project01_meetingapp.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.moondroid.project01_meetingapp.R;
import com.moondroid.project01_meetingapp.ui.activity.IntroActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    static String chatSetting;
    static String meetSetting;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i("FCM", "messageReceived");

        //푸시메세지를 받았을 경우 보여줄 Notification 생성
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;

        //Android API 26버전(오레오) 이상에서는 Notification Channel 을 생성해서 만들어줘야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ch01", "push ch", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, getResources().getString(R.string.default_notification_channel_id));

        } else {
            builder = new NotificationCompat.Builder(this, getResources().getString(R.string.default_notification_channel_id));
        }

        String fromWho = remoteMessage.getFrom(); // 메세지를 보낸 사람 기기명 [firebase 서버에서 자동 지정한 이름]

        //알림에 넣을 데이터들
        String notiTitle = "title"; //원격메세지에 알림제목 정보가 없을때의 기본값
        String notiText = "message"; //원격메세지에 알림메세지 정보가 없을때의 기본값
        String data = "data";

        //푸시메세지의 Notification 속성에 앱에서 필요한 정보들이 실려온다.
        //이 정보들로 푸시메세지 앱에서 컨트롤 가능. (ex. url)
        if (remoteMessage.getNotification() != null) {
            notiTitle = remoteMessage.getNotification().getTitle();
            notiText = remoteMessage.getNotification().getBody();
        }

        if (remoteMessage.getData() != null){
            notiTitle = remoteMessage.getData().get("title");
            notiText = remoteMessage.getData().get("body");
            data = remoteMessage.getData().get("meetName");
        }

        //알림의 설정들
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(notiTitle);
        builder.setContentText(notiText);
        builder.setAutoCancel(true);
        
        //푸시에서 온 내용을 알림으로 전환, 클릭시 IntroActivity 로 화면 전환. (MainActivity 에서 유저정보를 가져오지 않기 때문에 sharedPreference 에 저장된 유저정보 데이터를 불러오기 위해 인트로로 이동)
        Intent intent = new Intent(this, IntroActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("meetName", data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //바로실행되지 않고 알림에 보관되어 있다가 실행되어야 하므로 보류중인 인텐트로 변환
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(11, builder.build());

    }
}
