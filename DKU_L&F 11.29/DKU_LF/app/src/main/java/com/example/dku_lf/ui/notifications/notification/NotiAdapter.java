package com.example.dku_lf.ui.notifications.notification;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.example.dku_lf.ui.home.found.FoundPostActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHolder>{

    private final List<NotiItem> mDataList;
    Context context;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://lostnfound-3024f.appspot.com/");

    public NotiAdapter(List<NotiItem> dataList){
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final NotiItem item = mDataList.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoundPostActivity.class);
                intent.putExtra(FirebaseID.documentId, item.getDocumentID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        storage.getReference().child("images/found/" + item.getDocumentID() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) { // 성공 시

                Glide.with(context)
                        .load(uri)
                        .into(holder.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noti_title);
            content = itemView.findViewById(R.id.noti_content);
            image = itemView.findViewById(R.id.item_image);

        }
    }
}
