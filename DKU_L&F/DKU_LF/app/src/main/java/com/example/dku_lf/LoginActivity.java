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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
EditText LogEtID,LogEtPW;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        LogEtID = (EditText) findViewById(R.id.LoginEmail);
        LogEtPW = (EditText) findViewById(R.id.LoginPw);
        Button BtnLogin = (Button)findViewById(R.id.LoginSignBtn);
        Button BtntoReg = (Button)findViewById(R.id.LoginRegBtn);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this,HomeActivity.class);
                String LogstID = LogEtID.getText().toString();
                String LogstPW = LogEtPW.getText().toString();

                if (LogstID.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"이메일을 입력해주세요",Toast.LENGTH_LONG).show();
                }
                
                if(LogstPW.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                }

                mAuth.signInWithEmailAndPassword(LogstID, LogstPW)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                   // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                   // updateUI(null);
                                }

                                // ...
                            }
                        });

                startActivity(in);
            }
        });

        BtntoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this,RegActivity.class);
                startActivity(in);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
     //나중에 추가!    updateUI(currentUser);
    }

}