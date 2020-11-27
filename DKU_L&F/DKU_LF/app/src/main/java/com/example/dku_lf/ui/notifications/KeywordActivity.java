package com.example.dku_lf.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dku_lf.KeywordBack;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.databinding.ActivityKeywordBinding;
import com.example.dku_lf.ui.notifications.keyword.KeyItem;
import com.example.dku_lf.ui.notifications.keyword.KeywordAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();
    String email = mAuth.getCurrentUser().getEmail();
    DocumentReference keyref = lStore.collection(FirebaseID.keyword).document(email);

    private EditText key_set;
    private ActivityKeywordBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Keyword Notification");

        binding = ActivityKeywordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        key_set = findViewById(R.id.key_set);
        Button key_add = (Button)findViewById(R.id.key_add);
        Button start = (Button)findViewById(R.id.keyword_start);

        final RecyclerView recyclerView = findViewById(R.id.Keyword_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        final List<KeyItem> dataList = new ArrayList<>();

        final DocumentReference docRef = lStore.collection(FirebaseID.keyword).document(UserAppliaction.user_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                String TAG = "Activity";
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    dataList.clear();
                    if(snapshot.getData().get(FirebaseID.words) != null){
                        List<String> words = (List)snapshot.getData().get(FirebaseID.words);
                        for(int i=0; i<words.size(); i++){
                            dataList.add(new KeyItem(words.get(i)));
                        }
                        KeywordAdapter adapter = new KeywordAdapter(dataList, KeywordActivity.this);
                        recyclerView.setAdapter(adapter);
                    }

                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


        key_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), KeywordBack.class);
                stopService(intent);

                if(mAuth.getCurrentUser() != null) {
                    //Firebase에서 ID, 타이틀, 내용 String으로 가져옴

                    Map<String, Object> data = new HashMap<>();
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

    public void onStopService(View view) {
        Intent keyintent = new Intent(getApplicationContext(), KeywordBack.class);
        stopService(keyintent);
    }
}