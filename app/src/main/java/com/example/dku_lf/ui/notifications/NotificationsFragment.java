package com.example.dku_lf.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.notifications.notification.NotiAdapter;
import com.example.dku_lf.ui.notifications.notification.NotiItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private String email = mAuth.getCurrentUser().getEmail();
    private List<NotiItem> dataList;
    private NotiAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dataList = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        FloatingActionButton key_set = (FloatingActionButton)root.findViewById(R.id.key_set);

        key_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KeywordActivity.class));
            }
        });

        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                mStore.collection(FirebaseID.user).document(email).collection(FirebaseID.notifications).document(dataList.get(position).getNotificationID()).delete();
            }
        };

        final RecyclerView recyclerView = root.findViewById(R.id.noti_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mStore.collection(FirebaseID.user)
                .document(email)
                .collection(FirebaseID.notifications)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null){
                            dataList.clear();
                            for(DocumentSnapshot snapshot : value.getDocuments()){
                                Map<String, Object> shot = snapshot.getData();
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String documentID = String.valueOf(shot.get(FirebaseID.documentId));
                                String notificationID = String.valueOf(snapshot.getId());
                                NotiItem data = new NotiItem(title, contents, documentID, notificationID);
                                dataList.add(data);
                            }
                            adapter = new NotiAdapter(dataList);
                            recyclerView.setAdapter(adapter);

                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                            itemTouchHelper.attachToRecyclerView(recyclerView);
                        }
                    }
                });


        return root;
    }
}