package com.example.dku_lf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dku_lf.database.FirebaseID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class KeywordBack extends Service {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();
    private List<String> words = new ArrayList();
    private String new_contents;
    private String new_title;
    private String new_documentId;
    String email = mAuth.getCurrentUser().getEmail();
    private NotificationManager notificationManager;
    DocumentReference keyRef = lStore.collection(FirebaseID.keyword).document(email);
    ListenerRegistration registration;


    public KeywordBack() {
    }

    public void KeywordNotification(){

        for(int i=0; i<words.size(); i++){
            if(new_title.indexOf(words.get(i)) != -1 || new_contents.indexOf(words.get(i)) != -1){

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");

                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("[키워드 알림] '" + words.get(i) + "' 관련 새 글이 등록되었습니다.");
                builder.setColor(Color.RED);
                // 사용자가 탭을 클릭하면 자동 제거
                builder.setAutoCancel(true);

                // 알림 표시
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                }

                // id값은
                // 정의해야하는 각 알림의 고유한 int값
                notificationManager.notify((int)(System.currentTimeMillis()/1000), builder.build());

                Map<String, Object> data = new HashMap<>();
                data.put(FirebaseID.title, "["+ words.get(i) +"]");
                data.put(FirebaseID.contents, "키워드 [" + words.get(i) + "] 에 대한 새 글이 등록되었습니다.");
                data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                data.put(FirebaseID.documentId, new_documentId);
                Log.w(TAG, "새로운 도큐먼트 ID : " + new_documentId);
                lStore.collection(FirebaseID.user).document(email).collection(FirebaseID.notifications)
                        .document().set(data, SetOptions.merge());

            }
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
    @Override
    public void onCreate(){ // 백그라운드 서비스가 처음 실행될 때 한 번만 실행되는 메소드
        super.onCreate();

        Log.w(TAG, "서비스 생성");

    }



    @Override
    public int onStartCommand(Intent intent, int flags, final int startId){

        Log.w(TAG, "서비스 실행 중...");


        keyRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }



                if (snapshot != null && snapshot.exists()) {
                    words = (List) snapshot.getData().get(FirebaseID.words);
                    Log.w(TAG, "현재 키워드 : " + words);

                }

                if(snapshot.getMetadata().isFromCache() == false){
                    Log.w(TAG, "키워드에 변동이 있었습니다. 서비스를 재시작합니다.");
                    stopSelf(startId);
                }
            }
        }); // 자신의 keyword document가 변동될 경우 String List words에 변경된 키워드를 실시간으로 받는다.



        registration = lStore.collection(FirebaseID.post_found)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot qvalue, @Nullable FirebaseFirestoreException error) {
                        Log.w(TAG, "DOC Changes : " + qvalue.getDocumentChanges());
                        Log.w(TAG, "쿼리 메타 데이터 : " + qvalue.getMetadata());

                        SnapshotMetadata meta = qvalue.getMetadata();

                        if(meta.hasPendingWrites() == true){ // 로컬에서 쓰여진 거면 서비스 강제 종료
                            Log.w(TAG, "서비스 종료");
                            stopSelf(startId);
                        }

                        if( meta.isFromCache() == false && meta.hasPendingWrites() == false){ // 두 번째 실행부터
                            for(DocumentChange dc : qvalue.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){ // 새로운 문서가 추가된 경우


                                    new_title = dc.getDocument().getData().get(FirebaseID.title).toString();
                                    new_contents = dc.getDocument().getData().get(FirebaseID.contents).toString();
                                    new_documentId = dc.getDocument().getId();

                                    KeywordNotification();

                                    Log.w(TAG, "추가된 Document Title : " + new_title);


                                }
                            }

                        }

                    }
                });



        Log.w(TAG, "한 번 종료");


        return START_STICKY; // 강제 종료되었을 경우 다시 실행

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.w(TAG, "서비스 종료됨");
    }
}