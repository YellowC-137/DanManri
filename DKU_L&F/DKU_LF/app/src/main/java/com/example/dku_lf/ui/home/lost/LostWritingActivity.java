package com.example.dku_lf.ui.home.lost;

import android.content.Intent;
import android.os.Bundle;

import com.example.dku_lf.CameraActivity;
import com.example.dku_lf.HomeActivity;
import com.example.dku_lf.LocationActivity;
import com.example.dku_lf.R;
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

public class LostWritingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText Title, Contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_writing);

        Button PhBtn = (Button)findViewById(R.id.addPhotoBtn_lost);
        Button MapBtn = (Button)findViewById(R.id.addMapBtn_lost);
        Button Submit = (Button)findViewById(R.id.submitBtn_lost);

        Title = findViewById(R.id.title_edit_lost);
        Contents = findViewById(R.id.contentText_edit_lost);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Write = new Intent(LostWritingActivity.this, HomeActivity.class);
                if(mAuth.getCurrentUser() != null) {
                    // 타이틀이 같아도  생성되도록 함
                    String postId = mStore.collection(FirebaseID.post).document().getId();

                    //Firebase에서 ID, 타이틀, 내용 String으로 가져옴
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.documentId, postId);
                    data.put(FirebaseID.title, Title.getText().toString());
                    data.put(FirebaseID.contents, Contents.getText().toString());
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                    mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
                }
                startActivity(Write);
            }
        });


        //사진, 일단은 찍는거고 나중에 사진첨부로 변경해야함!
        PhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LostWritingActivity.this, CameraActivity.class);
                startActivity(in);
                return;
            }
        });

        //지도
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Location = new Intent(LostWritingActivity.this, LocationActivity.class);
                startActivity(Location);
            }
        });
    }
}