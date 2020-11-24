package com.example.dku_lf.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.dku_lf.R;
import com.example.dku_lf.adapters.ChatAdapter;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.models.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ChatAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText ChatText;
    FirebaseDatabase database;
    ArrayList<ChatModel> chattingArrayList;
    private static final String TAG = "ChatActivity";
    String myID,myname;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = FirebaseDatabase.getInstance();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore lStore = FirebaseFirestore.getInstance();
        final String email = mAuth.getCurrentUser().getEmail();
        myname = UserAppliaction.user_name;
        myID = UserAppliaction.user_id;

        chattingArrayList = new ArrayList<>();
        ChatText = (EditText)findViewById(R.id.ChatText);
        String stText = ChatText.getText().toString();
        Button back = (Button)findViewById(R.id.Chat_backBtn);
        Button send = (Button)findViewById(R.id.ChatSend);

        recyclerView = (RecyclerView) findViewById(R.id.Chat_RecyclerView );

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ChatAdapter(chattingArrayList,email);
        recyclerView.setAdapter(mAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                ChatModel chatting = dataSnapshot.getValue(ChatModel.class);
                String commentKey = dataSnapshot.getKey();
                String stEmail = chatting.getEmail();
                String chatText = chatting.getText();
                chattingArrayList.add(chatting);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
/*
                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                ChatModel chatting = dataSnapshot.getValue(ChatModel.class);
                String commentKey = dataSnapshot.getKey();
                String stEmail = chatting.getID();
                String chatText = chatting.getChatting();
*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
/*
                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                //comment 대신에 chatlist이용
                ChatModel chatting = dataSnapshot.getValue(ChatModel.class);
                String commentKey = dataSnapshot.getKey();

                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference ref = database.getReference("message");
        ref.addChildEventListener(childEventListener);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String stText = ChatText.getText().toString();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    String datetime = dateformat.format(c.getTime());
                    DatabaseReference myRef = database.getReference("message").child(datetime);
                    Toast.makeText(ChatActivity.this, "ID: " + myname + "MSG :" + stText, Toast.LENGTH_LONG).show();
                    Hashtable<String, String> chats = new Hashtable<String, String>();
                    chats.put("email", myID);
                    chats.put("name",myname);
                    chats.put("text", stText);

                    myRef.setValue(chats);
            }
        });




    }

    // Read from the database

}