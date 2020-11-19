package com.example.dku_lf.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.dku_lf.R;
import com.example.dku_lf.KeywordBack;
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

        key_add.setOnClickListener(new View.OnClickListener() {
            String email = mAuth.getCurrentUser().getEmail();
            DocumentReference keyref = lStore.collection(FirebaseID.keyword).document(email);

            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null) {
                    //Firebase에서 ID, 타이틀, 내용 String으로 가져옴

                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.email, email);
                    lStore.collection(FirebaseID.keyword).document(email).set(data, SetOptions.merge()); // collection 및 document 생성
                    keyref.update(FirebaseID.words, FieldValue.arrayUnion(key_set.getText().toString())); // 새로운 값이면 배열에 추가
                    Toast.makeText(getApplicationContext(), "[" + key_set.getText().toString() +"] 키워드가 등록되었습니다.", Toast.LENGTH_SHORT);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent keyintent = new Intent(getApplicationContext(), KeywordBack.class);
                startService(keyintent);
                Toast.makeText(getApplicationContext(), "키워드 알림이 시작되었습니다.", Toast.LENGTH_SHORT);
            }
        });

    }
}