package com.example.dku_lf.ui.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dku_lf.HomeActivity;
import com.example.dku_lf.LoginActivity;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class AuthenticationActivity extends AppCompatActivity{

    private CardView reset_button, start_button;
    private ImageButton question;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 결과 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        reset_button = (CardView)findViewById(R.id.reset_card);
        question = (ImageButton)findViewById(R.id.question_Btn);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(AuthenticationActivity.this);
                ad.setTitle("안내");
                ad.setMessage("본 어플리케이션은 단국대학교 재학생을 위한 분실물 어플입니다. 단국대학교 학생임을 인증하기 위한 수단으로써 단국대학교 G-mail을 사용하고 있습니다. 회원 가입이 완료되면 사건, 사고가 발생할 시 활용할 수 있도록 회원님의 학번과 이름 정보가 서버에서 저장됩니다. 저장된 정보는 회원탈퇴 시 폐기됩니다.");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });


        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, ResetGmailActivity.class);
                startActivity(intent);
            }
        });


        start_button = (CardView)findViewById(R.id.start_card);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

}