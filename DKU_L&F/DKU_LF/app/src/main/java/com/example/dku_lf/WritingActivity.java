package com.example.dku_lf;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WritingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();

    private EditText lTitle, lContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        Button PhBtn = (Button)findViewById(R.id.addPhotoBtn);
        Button MapBtn = (Button)findViewById(R.id.addMapBtn);
        Button Submit = (Button)findViewById(R.id.submitBtn);

        lTitle = findViewById(R.id.title_edit);
        lContents = findViewById(R.id.contentText_edit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Write = new Intent(WritingActivity.this,HomeActivity.class);
                if(mAuth.getCurrentUser() != null) {
                    // 타이틀이 같아도  생성되도록 함
                    String postId = lStore.collection(FirebaseID.post).document().getId();

                    //Firebase에서 ID, 타이틀, 내용 String으로 가져옴
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.documentId, postId);
                    data.put(FirebaseID.title, lTitle.getText().toString());
                    data.put(FirebaseID.contents, lContents.getText().toString());
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                    lStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
                }
                startActivity(Write);
            }
        });


        //사진, 일단은 찍는거고 나중에 사진첨부로 변경해야함!
        PhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(WritingActivity.this,CameraActivity.class);
                startActivity(in);
                return;
            }
        });

        //지도
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Location = new Intent(WritingActivity.this,LocationActivity.class);
                startActivity(Location);
            }
        });
    }
}