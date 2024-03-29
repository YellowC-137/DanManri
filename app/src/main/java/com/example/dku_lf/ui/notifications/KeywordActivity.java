package com.example.dku_lf.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dku_lf.KeywordBack;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
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

    private EditText key_set;
    private ActivityKeywordBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("키워드 설정");

        binding = ActivityKeywordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        key_set = findViewById(R.id.key_set);
        ImageButton key_add = (ImageButton)findViewById(R.id.key_add);
        Button start = (Button)findViewById(R.id.keyword_start);

        final RecyclerView recyclerView = findViewById(R.id.Keyword_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        final List<KeyItem> dataList = new ArrayList<>();

        try{
            lStore.collection(FirebaseID.keyword)
                    .document(email)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                return;
                            }

                            if(value != null){
                                dataList.clear();
                                List<String> words = (List)value.getData().get(FirebaseID.words);
                                for(int i=0; i<words.size(); i++){
                                    dataList.add(new KeyItem(words.get(i)));
                                }
                                KeywordAdapter adapter = new KeywordAdapter(dataList, KeywordActivity.this);
                                recyclerView.setAdapter(adapter);

                            }
                        }
                    });

        }catch (Exception e){
            Log.w(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), " : 키워드 로드 실패" + e);
            return;
        }


        key_add.setOnClickListener(new View.OnClickListener() {
            DocumentReference keyref = lStore.collection(FirebaseID.keyword).document(email);

            @Override
            public void onClick(View v) {
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
                Toast.makeText(KeywordActivity.this, "키워드 알림이 시작되었습니다!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), KeywordBack.class);
                startService(intent);
            }
        });
    }

}