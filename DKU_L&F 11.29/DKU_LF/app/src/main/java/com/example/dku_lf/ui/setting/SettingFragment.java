package com.example.dku_lf.ui.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.dku_lf.R;
import com.example.dku_lf.LoginActivity;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.home.HomeFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;


public class SettingFragment extends HomeFragment {

    private GoogleApiClient mGoogleApiClient;
    private TextView studentnum, studentname;
    private ImageView profile;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final FirebaseAuth mAuth = FirebaseAuth.getInstance(); // 현재 계정 상태 가져옴
        final FirebaseFirestore lStore = FirebaseFirestore.getInstance();
        final String email = mAuth.getCurrentUser().getEmail();

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        Button logout = (Button) root.findViewById(R.id.logout_button);
        Button leave  = (Button) root.findViewById(R.id.leave_button);
        studentnum = root.findViewById(R.id.student_num);
        studentname = root.findViewById(R.id.student_name);
        profile = root.findViewById(R.id.profile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setMessage("로그아웃 하시겠습니까?");

                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "성공적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                ad.show();

            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("Confirm Message");
                ad.setMessage("정말로 탈퇴하시겠습니까?");

                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {


                        // 자신의 키워드 삭제
                        lStore.collection(FirebaseID.keyword).document(email)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });

                        // 자신의 유저 정보 삭제
                        lStore.collection(FirebaseID.user).document(email)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });


                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        Toast.makeText(getActivity(), "회원탈퇴가 성공적으로 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialog.dismiss();

                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                ad.show();

            }
        });

        lStore.collection(FirebaseID.user)
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task != null){
                            studentname.setText(String.valueOf(task.getResult().get("StudentName")));
                            studentnum.setText(String.valueOf(task.getResult().get("StudentNum")));
                            Glide.with(getActivity())
                                    .load(mAuth.getCurrentUser().getPhotoUrl())
                                    .circleCrop()
                                    .into(profile);
                        }
                    }
                });




        return root;
    }




}