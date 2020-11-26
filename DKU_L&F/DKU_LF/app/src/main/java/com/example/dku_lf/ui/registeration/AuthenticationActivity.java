package com.example.dku_lf.ui.registeration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dku_lf.R;
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
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.StringTokenizer;

public class AuthenticationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CardView reset_button, start_button;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth auth;
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 결과 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        reset_button = (CardView)findViewById(R.id.reset_card);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, ResetGmailActivity.class);
                startActivity(intent);
            }
        });


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();

        start_button = (CardView)findViewById(R.id.start_card);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // 구글 로그인 인증을 요청했을 때 결과 값을 돌려 받는 곳
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){ // 인증 결과가 성공적이면
                GoogleSignInAccount account = result.getSignInAccount();
                String email = account.getEmail();

                StringTokenizer Etoken = new StringTokenizer(email, "@");

                String studentNum = Etoken.nextToken();
                String emailform = Etoken.nextToken();

                Log.w("Email : ", emailform);
                Log.w("학번 : ", studentNum);

                if(emailform.equals("dankook.ac.kr")){
                    Toast.makeText(this, "단국대 학생 인증되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), RegActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("studentName", account.getDisplayName());
                    intent.putExtra("studentNum", studentNum);
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(this, "단국대 이메일이 아닙니다!", Toast.LENGTH_SHORT).show();
                    Auth.GoogleSignInApi.signOut(googleApiClient); // 단국대 이메일이 아니면 로그아웃
                }

            }

        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "연결 실패", Toast.LENGTH_SHORT).show();

    }
}