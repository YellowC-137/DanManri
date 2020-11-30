package com.example.dku_lf.ui.home.lost;

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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dku_lf.HomeActivity;
import com.example.dku_lf.LocationActivity;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.home.found.FoundWritingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LostWritingActivity extends AppCompatActivity {
    private static final String TAG = "LostWritingActivity";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private Uri FilePath;
    private EditText Title, Contents;
    private ImageView UploadImage;
    private String postId,posttitle,postname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_writing);

        Button PhBtn = (Button)findViewById(R.id.addPhotoBtn_lost);
        Button MapBtn = (Button)findViewById(R.id.addMapBtn_lost);
        Button Submit = (Button)findViewById(R.id.submitBtn_lost);
        final LinearLayout show_progress = (LinearLayout)findViewById(R.id.RegLay);

        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd");

        final String time = timeFormat.format(now);
        final String day = dayFormat.format(now);

        Title = findViewById(R.id.title_edit_lost);
        Contents = findViewById(R.id.contentText_edit_lost);
        UploadImage = (ImageView) findViewById(R.id.user_upload_image_lost);
        postId = mStore.collection(FirebaseID.post).document().getId();
        postname = UserAppliaction.user_name;
        Log.d(TAG,"으앙 "+postname);



        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_progress.setVisibility(View.VISIBLE);
                posttitle = Title.getText().toString();
                Intent Write = new Intent(LostWritingActivity.this, HomeActivity.class);
                if(mAuth.getCurrentUser() != null) {
                    // 타이틀이 같아도  생성되도록 함
                    if (FilePath != null) {
                        //storage
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        //Unique한 파일명을 만들자.
                        String filename = postId + ".png";
                        //storage 주소와 폴더 파일명을 지정해 준다.
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://lostnfound-3024f.appspot.com").child("images/lost/" + filename);
                        //올라가거라...
                        storageRef.putFile(FilePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                //Firebase에서 ID, 타이틀, 내용 String으로 가져옴
                                Map<String, Object> data = new HashMap<>();
                                data.put(FirebaseID.UID,FirebaseAuth.getInstance().getCurrentUser().getUid());
                                data.put(FirebaseID.documentId, postId);
                                data.put(FirebaseID.title, posttitle);
                                data.put(FirebaseID.contents, Contents.getText().toString());
                                data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                                data.put(FirebaseID.day, day);
                                data.put(FirebaseID.time, time);
                                data.put(FirebaseID.StudentName, postname);
                                mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
                                FilePath = null;
                                show_progress.setVisibility(View.GONE);
                                finish();
                            }
                        });
                    }
                    else{
                        Map<String, Object> data = new HashMap<>();
                        data.put(FirebaseID.UID,FirebaseAuth.getInstance().getCurrentUser().getUid());
                        data.put(FirebaseID.documentId, postId);
                        data.put(FirebaseID.title, posttitle);
                        data.put(FirebaseID.contents, Contents.getText().toString());
                        data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                        data.put(FirebaseID.day, day);
                        data.put(FirebaseID.time, time);
                        data.put(FirebaseID.StudentName, postname);
                        mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
                        show_progress.setVisibility(View.GONE);
                        finish();
                    }

                }
            }
        });


        //사진, 일단은 찍는거고 나중에 사진첨부로 변경해야함!
        PhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        //지도
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Location = new Intent(LostWritingActivity.this, LocationActivity.class);
                Location.putExtra("posttype","lost");
                Location.putExtra("posttitle",posttitle);
                Location.putExtra("postname",postname);
                Location.putExtra("postuid",postId);//게시글 id
                Log.d(TAG,"으앙 지도"+postname);
                startActivity(Location);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK) {
            FilePath = data.getData();
            Log.d("TAG", "uri:" + String.valueOf(FilePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePath);
                UploadImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile(String postId) {
        if (FilePath != null) {
            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //Unique한 파일명을 만들자.
            String filename = postId + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://lostnfound-3024f.appspot.com").child("images/lost/" + filename);
            //올라가거라...
            storageRef.putFile(FilePath);
        }
    }
}