package com.example.dku_lf.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.databinding.ActivityKeywordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class KeywordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();

    private EditText key_set;
    private ActivityKeywordBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Keyword Notification");

        binding = ActivityKeywordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        key_set = findViewById(R.id.key_set);
        Button key_add = (Button)findViewById(R.id.key_add);
        Button start = (Button)findViewById(R.id.keyword_start);
        Button stop = (Button)findViewById(R.id.keyword_stop);

        key_add.setOnClickListener(new View.OnClickListener() {
            String email = mAuth.getCurrentUser().getEmail();
            DocumentReference keyref = lStore.collection(FirebaseID.keyword).document(email);

            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null) {
                    //Firebase에서 ID, 타이틀, 내용 String으로 가져옴

                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.UserID, email);
                    lStore.collection(FirebaseID.keyword).document(email).set(data, SetOptions.merge()); // collection 및 document 생성
                    keyref.update(FirebaseID.words, FieldValue.arrayUnion(key_set.getText().toString())); // 새로운 값이면 배열에 추가
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {


            private void createNotification() { // 기본 알림


                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");

                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("알림 제목");
                builder.setContentText("알람 세부 텍스트");

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

            @Override
            public void onClick(View v) {

                createNotification();


            }
        });

    }
}