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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity {
    private static final String TAG = "RegActivity";
    EditText RegEtID,RegEtPW;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mAuth = FirebaseAuth.getInstance();

        RegEtID = (EditText)findViewById(R.id.RegEmail);
        RegEtPW = (EditText)findViewById(R.id.RegPw);


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
                final Intent a = new Intent(RegActivity.this,LoginActivity.class);
                mAuth.createUserWithEmailAndPassword(RegStEmail, RegStPW)
                        .addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (RegStEmail.isEmpty())
                                {
                                    Toast.makeText(RegActivity.this,"이메일을 입력해주세요",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(RegStPW.isEmpty())
                                {
                                    Toast.makeText(RegActivity.this,"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (task.isSuccessful()) {
                                    Toast.makeText(RegActivity.this,RegStEmail+" & "+RegStPW,Toast.LENGTH_LONG).show();
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startActivity(a);
                                    // updateUI(user);
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