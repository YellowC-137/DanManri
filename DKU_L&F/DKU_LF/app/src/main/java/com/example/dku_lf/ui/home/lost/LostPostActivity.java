package com.example.dku_lf.ui.home.lost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.chat.MessageActivity;
import com.example.dku_lf.ui.home.found.FoundPostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LostPostActivity extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextView TitleText, ContextText, NameText;

    private String id,Opponent,op_uid,op_title;

    Button LostPost_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_post);

        TitleText = findViewById(R.id.post_title_found);
        ContextText = findViewById(R.id.post_contents_found);
        NameText = findViewById(R.id.post_name_found);

        LostPost_chat = (Button)findViewById(R.id.found_chat_btn);

        Intent getIntent = getIntent();
        id = getIntent().getStringExtra(FirebaseID.documentId);

        mStore.collection(FirebaseID.post_found).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData();
                                    String title = String.valueOf(snap.get(FirebaseID.title));
                                    String content = String.valueOf(snap.get(FirebaseID.contents));
                                    String name = String.valueOf(snap.get(FirebaseID.StudentName));
                                    UserAppliaction.temp = name; //작성자의 이름,uid,게시글이름 받아오기
                                    UserAppliaction.uid = String.valueOf(snap.get(FirebaseID.UID));
                                    UserAppliaction.title = String.valueOf(snap.get(FirebaseID.title));
                                    TitleText.setText(title);
                                    ContextText.setText(content);
                                    NameText.setText(name);
                                }
                            }
                            else {
                                Toast.makeText(LostPostActivity.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        LostPost_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opponent = UserAppliaction.temp;
                op_uid = UserAppliaction.uid;
                op_title = UserAppliaction.title;
                UserAppliaction.temp = null;
                UserAppliaction.title=null;
                UserAppliaction.uid = null;
                Intent in = new Intent(LostPostActivity.this, MessageActivity.class);
                in.putExtra("othername",Opponent);
                in.putExtra("otheruid",op_uid);
                in.putExtra("othertitle",op_title);
                Toast.makeText(LostPostActivity.this,"쪽지함이 생성되었습니다",Toast.LENGTH_LONG).show();
                startActivity(in);
            }
        });


    }
}