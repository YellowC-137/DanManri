package com.example.dku_lf;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.dku_lf.ui.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WritingActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth lAuth = FirebaseAuth.getInstance();
    //private FirebaseFireStore lStore = FirebaseFireStore.getInstence(); 파이어베이스

    private EditText lTitle, lContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);


        lTitle = findViewById(R.id.title_edit);
        lContents = findViewById(R.id.contentText_edit);

        findViewById(R.id.submitBtn).setOnClickListener(this);
    }



    //submit 버튼 클릭
    @Override
    public void onClick(View v) {
        if(lAuth.getCurrentUser() != null) {

            /* Firebase를 이용한 작성 글 내용 게시하기

            // 타이틀이 같아도  생성되도록 함
            String postiId = lStore.collection(FirebaseID.post).document().getId();

            //Firebase에서 ID, 타이틀, 내용 String으로 가져옴
            Map<String, Object> data = new HashMap<>();
            data.put(FirebaseID.documentId, lAuth.getCurrentUser().getUid());
            data.put(FirebaseID.title, lTitle.getText().toString());
            data.put(FirebaseID.contents, lContents.getText().toString());
            lstore.collection(FirebaseID.post).document(lTitle.getText().toString()).set(data.SetOptions.merge());
            */
        }
        finish();
    }
}