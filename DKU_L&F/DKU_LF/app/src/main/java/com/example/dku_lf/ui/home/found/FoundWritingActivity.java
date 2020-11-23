package com.example.dku_lf.ui.home.found;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.dku_lf.R;
import com.example.dku_lf.CameraActivity;
import com.example.dku_lf.HomeActivity;
import com.example.dku_lf.LocationActivity;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FoundWritingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText Title, Contents;
    private String postId,posttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_writing);

        Button PhBtn = (Button)findViewById(R.id.addPhotoBtn_found);
        Button MapBtn = (Button)findViewById(R.id.addMapBtn_found);
        Button Submit = (Button)findViewById(R.id.submitBtn_found);

        Title = findViewById(R.id.title_edit_found);
        Contents = findViewById(R.id.contentText_edit_found);
        postId = mStore.collection(FirebaseID.post_found).document().getId();
        posttitle= Title.getText().toString();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Write = new Intent(FoundWritingActivity.this, HomeActivity.class);
                if(mAuth.getCurrentUser() != null) {
                    // 타이틀이 같아도  생성되도록 함

                    //Firebase에서 ID, 타이틀, 내용 String으로 가져옴
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.UID,FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.put(FirebaseID.documentId, postId);
                    data.put(FirebaseID.title, posttitle);
                    data.put(FirebaseID.contents, Contents.getText().toString());
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                    data.put(FirebaseID.StudentName, UserAppliaction.user_name);
                    mStore.collection(FirebaseID.post_found).document(postId).set(data, SetOptions.merge());
                }
                startActivity(Write);
            }
        });


        //사진, 일단은 찍는거고 나중에 사진첨부로 변경해야함!
        PhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FoundWritingActivity.this, CameraActivity.class);
                startActivity(in);
                return;
            }
        });

        //지도
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Location = new Intent(FoundWritingActivity.this, LocationActivity.class);
                Location.putExtra("posttype",1);//게시글 타입
                Location.putExtra("postname",posttitle);//게시글 이름
                Location.putExtra("postuid",postId);//게시글 id
                startActivity(Location);
            }
        });
    }
}