package com.example.dku_lf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dku_lf.database.FirebaseID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity {
    private static final String TAG = "RegActivity";
    EditText RegID,RegPW,RegCfPW;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mAuth = FirebaseAuth.getInstance();

        RegID = (EditText) findViewById(R.id.RegEmail);
        RegPW = (EditText) findViewById(R.id.RegPw);
        RegCfPW = (EditText)findViewById(R.id.RegPwConfirm);
        final String RegStPW = RegPW.getText().toString();
        final String RegStID = RegID.getText().toString();
        final String RegStCfPW = RegCfPW.getText().toString();
        Button BtnPh = (Button)findViewById(R.id.RegPhoto);

        BtnPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegActivity.this,CameraActivity.class);
                startActivity(in);
            }
        });

        Button BtnReg = (Button)findViewById(R.id.RegButton);
        BtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.createUserWithEmailAndPassword(RegStID, RegStPW)
                        .addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null) { //user가 null값이 아닐 때 Map에 Uid, Email, PW 입력
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put(FirebaseID.documentId, user.getUid());
                                        userMap.put(FirebaseID.email, RegStID);
                                        userMap.put(FirebaseID.password, RegStPW);
                                        mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); //marge는 document 새로 추가될 때 덮어쓰기.
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(RegActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
        });
    }
}