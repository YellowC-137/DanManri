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

import com.example.dku_lf.R;
import com.example.dku_lf.RegActivity;
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
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.StringTokenizer;

public class AuthenticationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CardView reset_button, start_button;
    private ImageButton question;
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
                resultLogin(account);
            }

        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ // 로그인 성공

                            StringTokenizer Etoken = new StringTokenizer(account.getEmail(), "@");

                            String studentNum = Etoken.nextToken();
                            String emailform = Etoken.nextToken();

                            Log.w("Email : ", emailform);
                            Log.w("학번 : ", studentNum);

                            if(emailform.equals("dankook.ac.kr")){
                                Toast.makeText(getApplicationContext(), "단국대 학생 인증되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), RegActivity.class);
                                intent.putExtra("email", account.getEmail());
                                intent.putExtra("studentName", account.getDisplayName());
                                intent.putExtra("studentNum", studentNum);
                                auth.getCurrentUser().delete();

                                startActivity(intent);
                            }

                        }
                        else{ // 로그인 실패

                        }
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "연결 실패", Toast.LENGTH_SHORT).show();

    }
}