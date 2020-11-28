package com.example.dku_lf.adapters;

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
import com.example.dku_lf.ui.models.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class LostPostAdapter extends RecyclerView.Adapter<LostPostAdapter.PostViewHolder> {

    private List<Post> datas;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://lostnfound-3024f.appspot.com/");
    private ImageView temp;

    public LostPostAdapter(List<Post> datas) {
        this.datas = datas;
    }


    //ViewHolder를 만들어라
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        Post data = datas.get(position);  //Post 객체를 하나 만듦. 위에서부터 아래로 0~포지션에 하나 넣어줌
        holder.title.setText(data.getTitle());  //각 하나가 holder임 포지션1에 홀더, 2에홀더... 거기에 datas.gettitle를 넣어줌. post에 있는 getTitle활용.
        holder.contents.setText(data.getContents());
        holder.user.setText(data.getUser());
        holder.time.setText(data.getTime());

        storage.getReference().child("images/lost/" + data.getDocumentId() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) { // 성공 시

                Glide.with(holder.photo.getContext())
                        .load(uri)
                        .into(holder.photo);
            }
        });

    }

    // 아이템의 총 길이를 가져온다
    @Override
    public int getItemCount() {
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView contents;
        private TextView user;
        private TextView time;
        private ImageView photo;

        // 제목이랑 내용 가져오기
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_post_title);
            contents = itemView.findViewById(R.id.item_post_contents);
            user = itemView.findViewById(R.id.item_post_user);
            time = itemView.findViewById(R.id.item_post_time);
            photo = itemView.findViewById(R.id.post_image);

        }
    }
}
