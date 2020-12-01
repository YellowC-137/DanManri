package com.example.dku_lf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.dku_lf.database.FirebaseID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class FoundWritingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();

    private EditText fTitle, fContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_writing);

        Button PhBtn = (Button)findViewById(R.id.addPhotoBtn_found);
        Button MapBtn = (Button)findViewById(R.id.addMapBtn_found);
        Button Submit = (Button)findViewById(R.id.submitBtn_found);

        fTitle = findViewById(R.id.title_edit_found);
        fContents = findViewById(R.id.contentText_edit_found);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Write = new Intent(FoundWritingActivity.this,HomeActivity.class);
                if(mAuth.getCurrentUser() != null) {
                    // 타이틀이 같아도  생성되도록 함
                    String postId = lStore.collection(FirebaseID.post_found).document().getId();

                    //Firebase에서 ID, 타이틀, 내용 String으로 가져옴
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.documentId, mAuth.getCurrentUser().getUid());
                    data.put(FirebaseID.title, fTitle.getText().toString());
                    data.put(FirebaseID.contents, fContents.getText().toString());
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                    lStore.collection(FirebaseID.post_found).document(postId).set(data, SetOptions.merge());
                }
                startActivity(Write);
            }
        });


        //사진, 일단은 찍는거고 나중에 사진첨부로 변경해야함!
        PhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FoundWritingActivity.this,CameraActivity.class);
                startActivity(in);
                return;
            }
        });

        //지도
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Location = new Intent(FoundWritingActivity.this,LocationActivity.class);
                startActivity(Location);
            }
        });
    }
}