package com.example.dku_lf.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dku_lf.R;
import com.example.dku_lf.adapters.ChatListAdapter;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.database.UserAppliaction;
import com.example.dku_lf.ui.home.found.FoundPostActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//쪽지함
public class ChatListActivity extends AppCompatActivity {
    private  ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomlist = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot();
    private String name,str_name,str_room;

    Map<String,Object> map = new HashMap<String, Object>();
    ArrayList<String> arraylist = new ArrayList<>();
    String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        listView = (ListView)findViewById(R.id.chat_list);

        myID = UserAppliaction.user_id;

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr_roomlist);
        listView.setAdapter(arrayAdapter);



    }

    public void onItemClick(View view, int position) {
        Intent intent = new Intent(ChatListActivity.this, FoundPostActivity.class);

        startActivity(intent);

    }
}