package com.example.dku_lf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.home.found.FoundPostActivity;
import com.example.dku_lf.ui.register.AuthenticationActivity;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginActivity";
    EditText LogEtID,LogEtPW;
    FirebaseDatabase database;
    ProgressBar progressBar;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 결과 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        Button BtnLogin = (Button)findViewById(R.id.LoginSignBtn);
        TextView BtntoReg = (TextView)findViewById(R.id.LoginRegBtn);
        progressBar = (ProgressBar)findViewById(R.id.LoginProg);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            UserAppliaction.user_id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            UserAppliaction.user_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(in);
        }

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        BtnLogin.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                                            progressBar.setVisibility(View.VISIBLE);
                                            startActivityForResult(intent, REQ_SIGN_GOOGLE);

                                                }
                                    }
        );

        BtntoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_reg = new Intent(LoginActivity.this, AuthenticationActivity.class);
                startActivity(to_reg);
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
                return;
            }

        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
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
                                Toast.makeText(getApplicationContext(), "단국대학생 로그인되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("email", account.getEmail());
                                intent.putExtra("studentName", account.getDisplayName());
                                intent.putExtra("studentNum", studentNum);

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user != null){
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put(FirebaseID.UID,FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    userMap.put(FirebaseID.email, user.getEmail());
                                    userMap.put(FirebaseID.StudentName, user.getDisplayName());
                                    userMap.put(FirebaseID.StudentNum, emailform);
                                    mStore.collection(FirebaseID.user).document(user.getEmail()).set(userMap, SetOptions.merge());

                                    List<String> words = new ArrayList<>();
                                    Map<String, Object> keyMap = new HashMap<>();
                                    keyMap.put(FirebaseID.words, words);

                                    FirebaseFirestore.getInstance().collection(FirebaseID.keyword).document(account.getEmail()).set(keyMap);
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                }
                            }

                            else{
                                Toast.makeText(getApplicationContext(), "단국대 G-mail로 로그인해주세요.", Toast.LENGTH_SHORT).show();
                                googleApiClient.clearDefaultAccountAndReconnect();
                                progressBar.setVisibility(View.GONE);
                                return;

                            }
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}