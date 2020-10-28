package com.example.dku_lf.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dku_lf.R;
import com.example.dku_lf.WritingActivity;

public class FoundFragment extends Fragment implements View.OnClickListener {

    private RecyclerView fPostRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_found, container, false);

        fPostRecyclerView = root.findViewById(R.id.found_recyclerview);
        root.findViewById(R.id.lost_WriteBtn).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), WritingActivity.class));
    }
}