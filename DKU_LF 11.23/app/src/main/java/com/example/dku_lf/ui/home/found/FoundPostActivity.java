package com.example.dku_lf.ui.home.found;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dku_lf.R;
import com.example.dku_lf.ui.chat.ChatActivity;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FoundPostActivity extends AppCompatActivity {
//게시글 쓰기,올리기
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextView TitleText, ContextText, NameText;

    private String id;

    Button FoundPost_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_post);

        TitleText = findViewById(R.id.post_title_found);
        ContextText = findViewById(R.id.post_contents_found);
        NameText = findViewById(R.id.post_name_found);

        FoundPost_chat = (Button)findViewById(R.id.found_chat_btn);

        Intent getIntent = getIntent();
        id = getIntent().getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID ", id);

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

                                    TitleText.setText(title);
                                    ContextText.setText(content);
                                    NameText.setText(name);
                                }
                            }
                            else {
                                Toast.makeText(FoundPostActivity.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        //바로 채팅방으로 연결
        FoundPost_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FoundPostActivity.this, ChatActivity.class);

                startActivity(in);
            }
        });



    }
}