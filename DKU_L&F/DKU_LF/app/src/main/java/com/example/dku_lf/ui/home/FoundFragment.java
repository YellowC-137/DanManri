package com.example.dku_lf.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dku_lf.R;
import com.example.dku_lf.WritingActivity;
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

public class FoundFragment extends Fragment implements View.OnClickListener {

    private RecyclerView fPostRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_found, container, false);

        fPostRecyclerView = root.findViewById(R.id.found_recyclerview);
        root.findViewById(R.id.found_WriteBtn).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), WritingActivity.class));
    }
}