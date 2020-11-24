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
    EditText RegEtID,RegEtPW, RegEtPwCon, StName, StNum;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        mAuth = FirebaseAuth.getInstance();

        RegEtID = (EditText)findViewById(R.id.RegEmail);
        RegEtPW = (EditText)findViewById(R.id.RegPw);
        RegEtPwCon = (EditText)findViewById(R.id.PassConfirm);
        StName = (EditText)findViewById(R.id.StudentName);
        StNum = (EditText)findViewById(R.id.StudentNum);

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
                final String RegStEmail = RegEtID.getText().toString().trim();
                final String RegStPW = RegEtPW.getText().toString().trim();
                final String RegStPwCon = RegEtPwCon.getText().toString().trim();
                final String StudentName = StName.getText().toString().trim();
                final String StudentNum = StNum.getText().toString().trim();

                final Intent a = new Intent(RegActivity.this,LoginActivity.class);

                if (RegStEmail.isEmpty())
                {
                    Toast.makeText(RegActivity.this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

               /* if (RegStEmail.())
                {
                    Toast.makeText(RegActivity.this,"이미 존재하는 이메일 입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
*/
                if(RegStPW.isEmpty())
                {
                    Toast.makeText(RegActivity.this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(RegStPW.length() < 6)
                {
                    Toast.makeText(RegActivity.this,"비밀번호는 6자 이상이어야 합니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(RegStPwCon.isEmpty())
                {
                    Toast.makeText(RegActivity.this,"비밀번호 확인을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!RegStPW.equals(RegStPwCon))
                {
                    Toast.makeText(RegActivity.this,"입력된 비밀번호가 서로 다릅니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(StudentName.isEmpty())
                {
                    Toast.makeText(RegActivity.this,"이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(StudentNum.isEmpty())
                {
                    Toast.makeText(RegActivity.this,"학번을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                    mAuth.createUserWithEmailAndPassword(RegStEmail, RegStPW)
                            .addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegActivity.this,"Register Succesful!",Toast.LENGTH_SHORT).show();
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user != null) {
                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put(FirebaseID.email, RegStEmail);
                                            userMap.put(FirebaseID.StudentName, StudentName);
                                            userMap.put(FirebaseID.StudentNum, StudentNum);
                                            mStore.collection(FirebaseID.user).document(RegStEmail).set(userMap, SetOptions.merge());
                                        }
                                        startActivity(a);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                        //  updateUI(null);
                                    }

                                    // ...
                                }
                            });


            }


        });
    }
}