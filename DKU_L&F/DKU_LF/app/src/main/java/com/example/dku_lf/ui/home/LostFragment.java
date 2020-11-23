package com.example.dku_lf.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dku_lf.R;
import com.example.dku_lf.adapters.PostAdapter;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.home.lost.LostPostActivity;
import com.example.dku_lf.ui.home.lost.LostWritingActivity;
import com.example.dku_lf.ui.models.Post;
import com.example.dku_lf.ui.models.RecyclerViewItemClickListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LostFragment extends HomeFragment implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();


    private RecyclerView PostRecyclerView;

    private PostAdapter mAdapter;
    private List<Post> mDatas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatas = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_lost, container, false);

        PostRecyclerView = root.findViewById(R.id.lost_recyclerview);

        root.findViewById(R.id.lost_WriteBtn).setOnClickListener(this);

        PostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), PostRecyclerView, this));

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)  // 시간순서대로 내림차순
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String user = String.valueOf(shot.get(FirebaseID.user));
                                Post data = new Post(documentId, title,user, contents);
                                mDatas.add(data);
                            }
                        }
                        mAdapter = new PostAdapter(mDatas);
                        PostRecyclerView.setAdapter(mAdapter);
                    }
                });
    }




    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), LostWritingActivity.class));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), LostPostActivity.class);
        intent.putExtra(FirebaseID.documentId, mDatas.get(position).getDocumentId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("삭제 하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId()).delete();
                Toast.makeText(getActivity(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("삭제 알림");
        dialog.show();

    }
}