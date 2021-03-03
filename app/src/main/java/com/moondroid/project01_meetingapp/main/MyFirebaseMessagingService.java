package com.moondroid.project01_meetingapp.main;

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
import com.moondroid.project01_meetingapp.login.IntroActivity;
import com.moondroid.project01_meetingapp.option02notification.NotificationActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i("FCM", "messageReceived");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ch01", "push ch", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, "ch01");

        } else {
            builder = new NotificationCompat.Builder(this, null);
        }

        String fromWho = remoteMessage.getFrom(); // 메세지를 보낸 사람 기기명 [firebase 서버에서 자동 지정한 이름]

        //알림에 넣을 데이터들
        String notiTitle = "title"; //원격메세지에 알림제목 정보가 없을때의 기본값
        String notiText = "message"; //원격메세지에 알림메세지 정보가 없을때의 기본값


        if (remoteMessage.getNotification() != null) {
            notiTitle = remoteMessage.getNotification().getTitle();
            notiText = remoteMessage.getNotification().getBody();
        }

        //알림의 설정들
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_square));
        builder.setSmallIcon(R.drawable.ic_notification_fcm);
        builder.setContentTitle(notiTitle);
        builder.setContentText(notiText);
        builder.setAutoCancel(true);
        
        //푸시에서 온 내용을 알림으로 전환, 클릭시 IntroActivity 로 화면 전환. (sharedPreference 에서 값을 불러오기 위해 인트로로 이동)
        Intent intent = new Intent(this, IntroActivity.class);
        if (notiText.contains("메세지")) {
            intent.putExtra("meetName", notiText.replace("에서 새로운 메세지가 왔습니다.", ""));
        }
        intent.putExtra("sendClass", "FCM");
        intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
        //바로실행되지 않고 알림에 보관되어 있다가 실행되어야 하므로 보류중인 인텐트로 변환
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(11, builder.build());

    }
}
