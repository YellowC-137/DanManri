package com.example.dku_lf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.home.found.FoundPostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.Arrays;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText LogEtID,LogEtPW;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        LogEtID = (EditText) findViewById(R.id.LoginEmail);
        LogEtPW = (EditText) findViewById(R.id.LoginPw);
        Button BtnLogin = (Button)findViewById(R.id.LoginSignBtn);
        Button BtntoReg = (Button)findViewById(R.id.LoginRegBtn);
        progressBar = (ProgressBar)findViewById(R.id.LoginProg);

        // 자동 로그인
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent in = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(in);
        }

        BtnLogin.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            final String LogstID = LogEtID.getText().toString();
                                            final String LogstPW = LogEtPW.getText().toString();

                                            if (LogstID.isEmpty())
                                            {
                                                Toast.makeText(LoginActivity.this,"이메일을 입력해주세요",Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                            if(LogstPW.isEmpty())
                                            {
                                                Toast.makeText(LoginActivity.this,"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            progressBar.setVisibility(View.VISIBLE);
                                            UserAppliaction.user_name = null;
                                            UserAppliaction.user_id = null;
                                            mAuth.signInWithEmailAndPassword(LogstID, LogstPW)
                                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    if (task.isSuccessful()) {
                                                                        // Sign in success, update UI with the signed-in user's information
                                                                        Log.d(TAG, "signInWithEmail:success");
                                                                        UserAppliaction.user_id = LogstID;
                                                                        data();
                                                                        Intent in = new Intent(LoginActivity.this,HomeActivity.class);
                                                                        startActivity(in);
                                                                        // updateUI(user);
                                                                    } else {
                                                                        // If sign in fails, display a message to the user.
                                                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                                                Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                        // updateUI(null);
                                                                    }

                                                                    // ...
                                                                }
                                                            }

                                                    );


                                                }
                                    }
        );

        BtntoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regin = new Intent(LoginActivity.this,RegActivity.class);
                startActivity(regin);
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

    public void data(){
        FirebaseFirestore lStore = FirebaseFirestore.getInstance();

        lStore.collection("user")
                .document(mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                        String username = ds.getData().get("StudentName").toString();
                        UserAppliaction.user_name = username;
                    }
                });
    }
}