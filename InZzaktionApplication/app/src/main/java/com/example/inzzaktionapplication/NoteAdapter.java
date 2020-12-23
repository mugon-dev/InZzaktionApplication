package com.example.inzzaktionapplication;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    List<NoteSearch> tagItems;

    public NoteAdapter(List<NoteSearch> tagList) {
        tagItems = tagList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvTitle, tvTagNm, tvLikedCount, tvLiked;

        public ViewHolder(final View view) {
            super(view);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTagNm = itemView.findViewById(R.id.tvTags);
            tvLikedCount = itemView.findViewById(R.id.tvLikedCount);
            tvLiked = itemView.findViewById(R.id.tvLiked);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    Intent intent = new Intent(view.getContext(), MainActivity.class );
                    TagSearch tagSearch = tagItems.get(getAdapterPosition());
                    intent.putExtra("menu",menu);
                    view.getContext().startActivity(intent);

                     */
                }
            });

        }
    }

    public NoteAdapter(List<NoteSearch> myDataset, Context context) {
        tagItems = myDataset;
    }



    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_tag_search, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteSearch tagSearch = tagItems.get(position);

        //holder.ivPhoto.setImageResource(tagSearch.getPhoto());
        holder.tvTitle.setText(tagSearch.getTitle());
        holder.tvTagNm.setText(tagSearch.getTagNm());
        holder.tvLikedCount.setText(tagSearch.getLikedCount());
        holder.tvLiked.setText(tagSearch.getLiked());

/*
        if (holder.tagItems.getImage_name() != null) {
            Uri uri = Uri.parse(holder.tagItems.getImage_name());
            holder.iv_image.setImageURI(uri);
        }else {
            Log.d("NEWS","널값");
            Uri uri =  Uri.parse("http://192.168.0.77:8081/AndroidConn/images/ca5.jpg");
            holder.iv_image.setImageURI(uri);
        }


        if(tagSearch.getLiked().equals('Y')) {
            holder.btnLiked.setBackground(Drawable.createFromPath("@android:drawable/btn_star_big_on"));
        } else if(tagSearch.getLiked().equals('N')) {
            holder.btnLiked.setBackground(Drawable.createFromPath("@android:drawable/btn_star_big_off"));
        }

 */

    }


    @Override
    public int getItemCount() {
        return tagItems == null ? 0 : tagItems.size();
    }
}
