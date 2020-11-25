package com.example.dku_lf.ui.home.found;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import com.bumptech.glide.Glide;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.chat.MessageActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class FoundPostActivity extends AppCompatActivity {
//게시글 쓰기,올리기
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://lostnfound-3024f.appspot.com/");

    private TextView TitleText, ContextText, NameText;
    private ImageView UploadImage;
    private String id, Opponent, op_uid, op_title;

    Button FoundPost_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_post);

        TitleText = findViewById(R.id.post_title_found);
        ContextText = findViewById(R.id.post_contents_found);
        NameText = findViewById(R.id.post_name_found);
        UploadImage = (ImageView) findViewById(R.id.uploaded_image_found);

        FoundPost_chat = (Button)findViewById(R.id.found_chat_btn);

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
                                    String image_docID = String.valueOf(snap.get(FirebaseID.documentId)); //사진 다큐멘트 ID
                                    UserAppliaction.temp = name; //작성자의 이름,uid,게시글이름 받아오기
                                    UserAppliaction.uid = String.valueOf(snap.get(FirebaseID.UID));
                                    UserAppliaction.title = String.valueOf(snap.get(FirebaseID.title));
                                    TitleText.setText(title);
                                    ContextText.setText(content);
                                    NameText.setText(name);

                                    image_upload(image_docID);
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
                Opponent = UserAppliaction.temp;
                op_uid = UserAppliaction.uid;
                op_title = UserAppliaction.title;
                UserAppliaction.temp = null;
                UserAppliaction.title=null;
                UserAppliaction.uid = null;
                Intent in = new Intent(FoundPostActivity.this, MessageActivity.class);
                in.putExtra("othername",Opponent);
                in.putExtra("otheruid",op_uid);
                in.putExtra("othertitle",op_title);
                Toast.makeText(FoundPostActivity.this,"쪽지함이 생성되었습니다",Toast.LENGTH_LONG).show();
                startActivity(in);


            }
        });
    }

    private void image_upload(String image_docID) {

        StorageReference storageRef = storage.getReference();
        storageRef.child("images/found/" + image_docID + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(UploadImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 실패 보고싶을 때 Toast 생성
                // Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}