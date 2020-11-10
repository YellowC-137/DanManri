package com.example.dku_lf;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dku_lf.database.FirebaseID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.Metadata;

import static android.content.ContentValues.TAG;

public class KeywordBack extends Service {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();
    private List<String> words = new ArrayList();
    private String new_contents;
    public String new_title;
    String email = mAuth.getCurrentUser().getEmail();
    private NotificationManager notificationManager;
    public static final String CHANNEL_ID = "keyword channel";
    DocumentReference keyRef = lStore.collection(FirebaseID.keyword).document(email);
    public String add_post_flag;
    public boolean repeat = false;
    ListenerRegistration registration;


    public KeywordBack() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
    @Override
    public void onCreate(){ // 백그라운드 서비스가 처음 실행될 때 한 번만 실행되는 메소드
        super.onCreate();
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
                } else {
                    Log.d(TAG, "Field 'words' is Empty");
                }
            }
        }); // 자신의 keyword document가 변동될 경우 String List words에 변경된 키워드를 실시간으로 받는다.

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Log.w(TAG, "서비스 시작");
        boolean already_noti = false;



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
                    } else {
                        Log.d(TAG, "Field 'words' is Empty");
                    }
                }
            }); // 자신의 keyword document가 변동될 경우 String List words에 변경된 키워드를 실시간으로 받는다.




        registration = lStore.collection(FirebaseID.post)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot qvalue, @Nullable FirebaseFirestoreException error) {
                        Log.w(TAG, "DOC Changes : " + qvalue.getDocumentChanges());
                        Log.w(TAG, "쿼리 메타 데이터 : " + qvalue.getMetadata());

                        SnapshotMetadata meta = qvalue.getMetadata();

                        if(meta.hasPendingWrites() == true){ // 로컬에서 쓰여진 거면 서비스 강제 종료
                            Log.w(TAG, "서비스 종료");
                            stopSelf();
                        }

                        if( meta.isFromCache() == false && meta.hasPendingWrites() == false){ // 두 번째 실행부터
                            for(DocumentChange dc : qvalue.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){ // 새로운 문서가 추가된 경우
                                    Log.w(TAG, "추가된 도큐먼트의 타이틀 : " + dc.getDocument().getData().get("title"));

                                    new_title = dc.getDocument().getData().get(FirebaseID.title).toString();
                                    new_contents = dc.getDocument().getData().get(FirebaseID.contents).toString();

                                    for(int i=0; i<words.size(); i++){
                                        if(new_title.indexOf(words.get(i)) == -1 || new_contents.indexOf(words.get(i)) == -1){

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
                                            notificationManager.notify(1, builder.build());

                                        }
                                    }

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

        registration.remove();
    }
}