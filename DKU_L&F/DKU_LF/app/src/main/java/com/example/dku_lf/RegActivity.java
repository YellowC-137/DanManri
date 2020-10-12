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
    EditText RegID,RegPW,RegCfPW;
    private FirebaseAuth mAuth;
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
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //  updateUI(null);
                                }

                                // ...
                            }
                        });

                    Intent in = new Intent(RegActivity.this,LoginActivity.class);
                    startActivity(in);

                }


        });




    }
}