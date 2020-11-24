package com.example.dku_lf.ui.chat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dku_lf.R;
import com.example.dku_lf.ui.home.HomeFragment;
import com.example.dku_lf.ui.models.ChatModel;
import com.example.dku_lf.ui.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChatlistFragment extends HomeFragment {

    Button back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatlist,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.chat_recyclerview);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return view;
    }
    class ChatRecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<ChatModel> chatModels = new ArrayList<>();
        private String uid;
        private ArrayList<String> otheruser = new ArrayList<>();
        public ChatRecyclerViewAdapter() {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            chatModels.clear();
                            for (DataSnapshot item : snapshot.getChildren()){
                                chatModels.add(item.getValue(ChatModel.class));
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatlist,parent,false);



            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String  otheruid = null;

            for (String user: chatModels.get(position).users.keySet()){
                if (!user.equals(uid)){
                    otheruid=user;
                    otheruser.add(otheruid);
                }

FirebaseDatabase.getInstance().getReference().child("users").child(otheruid).
            addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    customViewHolder.textView_title.setText(chatModels.get(position).titles); //게시글 이름으로 바꾸기

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
                Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
                commentMap.putAll(chatModels.get(position).comment);

                customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getView().getContext(),MessageActivity.class);
                        intent.putExtra("otheruid",otheruser.get(position));
                        startActivity(intent);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

            class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_title;
            public CustomViewHolder(View view) {
                super(view);

                textView_title=(TextView)view.findViewById(R.id.item_chatlist_textview);

            }
        }
    }
}