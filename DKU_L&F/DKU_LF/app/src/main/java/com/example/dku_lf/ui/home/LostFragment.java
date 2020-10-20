package com.example.dku_lf.ui.home;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dku_lf.R;
import com.example.dku_lf.WritingActivity;
import com.example.dku_lf.adapters.PostAdapter;
import com.example.dku_lf.ui.models.Post;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;

public class LostFragment extends Fragment implements View.OnClickListener {

    //private FirebaseFirestore lStore = FirebaseFirestore.getInstance();


    private RecyclerView lPostRecyclerView;

    private PostAdapter lAdapter;
    private List<Post> lDatas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lost, container, false);

        lPostRecyclerView = root.findViewById(R.id.lost_recyclerview);



        //샘플 게시판 모양
        lDatas = new ArrayList<>();
        lDatas.add(new Post(null, "title", "contents"));
        lDatas.add(new Post(null, "title", "contents"));
        lDatas.add(new Post(null, "title", "contents"));

        lAdapter = new PostAdapter(lDatas);
        lPostRecyclerView.setAdapter(lAdapter);

        root.findViewById(R.id.lost_WriteBtn).setOnClickListener(this);

        return root;
    }


    /* Firebase 연동 실시간 게시글 생성
    @Override
    protected void onStart() {
        super.onStart();
        lDatas = new ArrayList<>();
        lStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)  //시간순서대로 내림차순
                //실시간 게시글 생성 -> addSnapshot
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(queryDocumentSnapshots != null) {
                            lDatas.clear(); // 이전에 있던 글들이 중복으로 올라오기 때문에 이전 것들을 삭제시킴
                            for (DocumentSnapshot snpq : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();  // 스냅샷을 하나 만들어서 샷을 만듦. getdata로 들어감
                                String documentId = String.valueOf(shot.get(FirevaseId.documentId));  // Id와 Title, contents를 샷에서 가져옴
                                String title = String.valueOf(snap.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                Post data = new Post(Id, title, contents); //Post를 만들어서 lDatas에 넣어줌
                                lDatas.add(data);
                            }
                            //Data를 다 넣었을 때 Adapter에 꽂아줌
                            lAdapter = new PostAdapter(lDatas);
                            lPostRecyclerView.setAdapter(lAdapter);
                        }
                    }
                });
    }
     */

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), WritingActivity.class));
    }
}