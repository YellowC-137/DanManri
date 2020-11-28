package com.example.dku_lf.ui.notifications.keyword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.ViewHolder>{

    public List<KeyItem> mDataList;
    Context context;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore lStore = FirebaseFirestore.getInstance();
    String email = mAuth.getCurrentUser().getEmail();
    DocumentReference keyref = lStore.collection(FirebaseID.keyword).document(email);

    public KeywordAdapter(List<KeyItem> dataList, Context context) {
        mDataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_keyword, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        KeyItem item = mDataList.get(position);
        holder.title.setText(item.getTitle());


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton delete;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.key_title);
            delete = itemView.findViewById(R.id.key_del_Btn);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        final KeyItem item = mDataList.get(pos);

                        // 다이얼로그 생성
                        AlertDialog.Builder ad = new AlertDialog.Builder(context);
                        ad.setMessage("키워드를 삭제하시겠습니까?");
                        ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                keyref.update(FirebaseID.words, FieldValue.arrayRemove(item.getTitle()));
                            }
                        });
                        ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        ad.show();

                    }
                }
            });
        }
    }
}
