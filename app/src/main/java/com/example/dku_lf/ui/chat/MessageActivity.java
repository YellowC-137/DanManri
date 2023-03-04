package com.example.dku_lf.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dku_lf.R;
import com.example.dku_lf.ui.models.ChatModel;
import com.example.dku_lf.ui.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.okhttp.RequestBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MessageActivity extends AppCompatActivity {

    private String otheruid,postname;
    private Button button;
    private EditText editText;
    private String uid,chatRoomuid,title;
    private UserModel userModel;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();//내 uid
        otheruid = getIntent().getStringExtra("otheruid"); //추가하기, 상대uid
        postname = getIntent().getStringExtra("othertitle");
        button = (Button)findViewById(R.id.message_send);
        editText = (EditText)findViewById(R.id.message_edittext);

        recyclerView = (RecyclerView)findViewById(R.id.message_recyclerview);
        final ChatModel chatModel = new ChatModel();
        chatModel.titles = postname;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatModel.users.put(uid,true);
                chatModel.users.put(otheruid,true);

                if (chatRoomuid==null)
                { button.setEnabled(false);
                FirebaseDatabase.getInstance().getReference().child("chatrooms").push()
                        .setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        checkroom();
                    }
                }); }
                else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomuid).child("comments").
                            push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText("");
                        }
                    });

                }

            }
        });
        checkroom();

    }


    void checkroom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class); //uid를 가져와서 확인
                    if (chatModel.users.containsKey(otheruid)){
                        chatRoomuid = item.getKey(); //방의 uid가져오기
                        button.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").child(otheruid).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userModel = snapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        void getMessageList(){

            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomuid).child("comments").
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            comments.clear();
                            for (DataSnapshot item : snapshot.getChildren()){
                                comments.add(item.getValue(ChatModel.Comment.class));
                            }
                            //메세지 새로고침
                            notifyDataSetChanged();
                            recyclerView.scrollToPosition(comments.size()-1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            if (comments.get(position).uid.equals(uid)) //내 메세지
            { messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.mychatbubble);
                messageViewHolder.textView_message.setPadding(15,15,15,15);
            messageViewHolder.linearLayout_other.setVisibility(View.INVISIBLE);
            messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
            }
            else{ //상대방 메세지
                messageViewHolder.textView_name.setText(userModel.userName);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.chatbubble);
                messageViewHolder.textView_message.setPadding(15,15,15,15);
                messageViewHolder.linearLayout_other.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
            }
            long unixtime = (long) comments.get(position).timestamp;
            Date date = new Date(unixtime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_time.setText(time);
            ((MessageViewHolder)holder).textView_message.setText(comments.get(position).message);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textView_name;
            public LinearLayout linearLayout_other;
            public LinearLayout linearLayout_main;
            public  TextView textView_time;
            public MessageViewHolder(View view) {
                super(view);
                textView_message =(TextView) view.findViewById(R.id.message_item);
                textView_name=(TextView)view.findViewById(R.id.message_item_name);
                linearLayout_other=(LinearLayout)view.findViewById(R.id.message_item_linear);
                linearLayout_main=(LinearLayout)view.findViewById(R.id.message_linear_main);
                textView_time = (TextView)view.findViewById(R.id.messageItem_textview_timestamp);
            }
        }
    }
}