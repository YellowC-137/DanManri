package com.example.dku_lf.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dku_lf.LostWritingActivity;
import com.example.dku_lf.R;
import com.example.dku_lf.adapters.PostAdapter;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.models.Post;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LostFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();


    private RecyclerView lPostRecyclerView;

    private PostAdapter lAdapter;
    private List<Post> lDatas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lDatas = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_lost, container, false);

        lPostRecyclerView = root.findViewById(R.id.lost_recyclerview);

        root.findViewById(R.id.lost_WriteBtn).setOnClickListener(this);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        lStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)  // 시간순서대로 내림차순
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(queryDocumentSnapshots != null) {
                            lDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                Post data = new Post(documentId, title, contents);
                                lDatas.add(data);
                            }
                        }
                        lAdapter = new PostAdapter(lDatas);
                        lPostRecyclerView.setAdapter(lAdapter);
                    }
                });
    }




    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), LostWritingActivity.class));
    }
}