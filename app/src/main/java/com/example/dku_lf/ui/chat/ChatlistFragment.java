package com.example.dku_lf.ui.chat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.home.HomeFragment;
import com.example.dku_lf.ui.models.ChatModel;
import com.example.dku_lf.ui.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatlistFragment extends HomeFragment {


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");
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
        private List<String> keys = new ArrayList<>();
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
                                keys.add(item.getKey());
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
            String  otheruid=null;

            for (String user: chatModels.get(position).users.keySet()) { //방의 유저들의 정보를 가져옴
                if (user != uid) {
                    otheruid = user;
                    otheruser.add(otheruid);
                }
            }

            FirebaseDatabase.getInstance().getReference().child("users").child(otheruid).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            customViewHolder.textView_title.setText("게시글 [ "+chatModels.get(position).titles+" ] 에서 생성됨");

                            FirebaseFirestore.getInstance().collection("user")
                                    .document(otheruser.get(customViewHolder.getAdapterPosition()))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.getResult() != null){
                                                Map<String, Object> snap = task.getResult().getData();
                                                String usernick = String.valueOf(snap.get("StudentNick"));
                                                customViewHolder.textView_name.setText("FROM : " + usernick);
                                            }
                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comment);
            if(commentMap.keySet().toArray().length > 0) {
                String lastMessageKey = (String) commentMap.keySet().toArray()[0];
                Log.d("TEST : ",lastMessageKey );
                customViewHolder.textView_lastmessage.setText(chatModels.get(position).
                        comment.get(lastMessageKey).message);

                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                long unixTime = (long) chatModels.get(position).comment.get(lastMessageKey).timestamp;
                Date date = new Date(unixTime);
                Log.d("TEST : ", String.valueOf(date));
                customViewHolder.textView_timestamp.setText(simpleDateFormat.format(date));
            }

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    intent.putExtra("otheruid",otheruser.get(position));
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_title;
            public TextView textView_lastmessage;
            public TextView textView_timestamp;
            public TextView textView_name;
            public CustomViewHolder(View view) {
                super(view);

                textView_lastmessage=(TextView)view.findViewById(R.id.chatlist_lastmessage);
                textView_title=(TextView)view.findViewById(R.id.item_chatlist_title);
                textView_timestamp = (TextView)view.findViewById(R.id.chatitem_textview_timestamp);
                textView_name=(TextView)view.findViewById(R.id.item_chatlist_name);

            }
        }
    }
}