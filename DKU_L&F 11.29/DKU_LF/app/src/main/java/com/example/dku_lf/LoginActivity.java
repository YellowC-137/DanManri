package com.example.dku_lf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.dku_lf.ui.models.UserModel;
import com.example.dku_lf.ui.register.AuthenticationActivity;
import com.example.dku_lf.ui.register.ResetGmailActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText LogEtID,LogEtPW;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    private static String Token;
    private final static int RC_SIGN_IN = 9391;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Button BtnLogin = (Button)findViewById(R.id.LoginSignBtn);
        TextView BtntoReg = (TextView) findViewById(R.id.LoginRegBtn);
        progressBar = (ProgressBar)findViewById(R.id.LoginProg);


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            pushTokenToServer();
            data();
            Intent in = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(in);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleSignInClient = GoogleSignIn.getClient(this, gso);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }


        });



        BtntoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_auth = new Intent(LoginActivity.this, AuthenticationActivity.class);
                startActivity(to_auth);
            }
        });

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String useraccount = account.getAccount().name;

            firebaseAuthWithGoogle(account.getIdToken(), useraccount);

        }catch (ApiException e){
            progressBar.setVisibility(View.GONE);
            Log.w("ERROR ", e);
        }
    }

    private void firebaseAuthWithGoogle(String idToken, final String useraccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            UserAppliaction.user_id = null;
                            UserAppliaction.user_name = null;

                            FirebaseUser user = mAuth.getCurrentUser();

                            StringTokenizer TokenizedID = new StringTokenizer(useraccount, "@");
                            String StudentNum = TokenizedID.nextToken();
                            String emailform = TokenizedID.nextToken();

                            if(!emailform.equals("dankook.ac.kr")){
                                Toast.makeText(LoginActivity.this, "단국대 이메일이 아닙니다.", Toast.LENGTH_SHORT).show();
                                googleSignInClient.revokeAccess();
                                FirebaseAuth.getInstance().getCurrentUser().delete(); // 이메일 형식 맞지 않을 시 삭제!
                                progressBar.setVisibility(View.GONE);
                            }

                            else{

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(FirebaseID.email, useraccount);
                                userMap.put(FirebaseID.StudentName, user.getDisplayName());
                                userMap.put(FirebaseID.StudentNum, StudentNum);
                                FirebaseFirestore.getInstance().collection(FirebaseID.user).document(useraccount).set(userMap, SetOptions.merge());

                                UserAppliaction.user_name = user.getDisplayName();
                                UserAppliaction.user_id = useraccount;

                                UserModel userModel = new UserModel();
                                userModel.userName = user.getDisplayName();
                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Map<String, Object> keyMap = new HashMap<>();
                                keyMap.put(FirebaseID.words, Arrays.asList("Temp-word"));
                                FirebaseFirestore.getInstance().collection(FirebaseID.keyword).document(useraccount).set(keyMap, SetOptions.merge());
                                FirebaseFirestore.getInstance().collection(FirebaseID.keyword).document(useraccount).update(FirebaseID.words, FieldValue.arrayRemove("Temp-word"));

                                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userModel);

                                googleSignInClient.signOut();

                                progressBar.setVisibility(View.GONE);

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
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

        try {
            lStore.collection("user")
                    .document(mAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot ds = task.getResult();
                            String username = ds.getData().get("StudentName").toString();
                            UserAppliaction.user_name = username;
                            UserAppliaction.user_id = ds.getData().get("email").toString();
                        }
                    });

        }catch (Exception e){
            Log.w("data() ", "실패");
        }

    }




    void pushTokenToServer(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token;
        final Map<String,Object> map = new HashMap<>();


        FirebaseMessaging.getInstance().getToken().
                addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
            Token = task.getResult();
                map.put("pushtoken",Token);
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("어플을 종료하시겠습니까?");
        ad.setIcon(R.mipmap.splashicon);
        ad.setTitle("단만리");


        ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        ad.show();
    }
}