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

import com.example.dku_lf.ui.chat.ChatActivity;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LostPostActivity extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextView TitleText, ContextText, NameText;

    private String id;

    Button LostPost_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_post);

        TitleText = findViewById(R.id.post_title_lost);
        ContextText = findViewById(R.id.post_contents_lost);
        NameText = findViewById(R.id.post_name_lost);

        LostPost_chat = (Button)findViewById(R.id.lost_chat_btn);


        Intent getIntent = getIntent();
        id = getIntent().getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID ", id);

        mStore.collection(FirebaseID.post).document(id)
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
                                    //String name = String.valueOf(snap.get(FirebaseID.nicname));

                                    TitleText.setText(title);
                                    ContextText.setText(content);
                                    //NameText.setText(name);
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
                Intent in = new Intent(LostPostActivity.this, ChatActivity.class);
                startActivity(in);
            }
        });


    }
}