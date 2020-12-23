package com.example.localinzzaktionapplication.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localinzzaktionapplication.R;
import com.example.localinzzaktionapplication.databaseHelper.TagDatabaseHelper;
import com.example.localinzzaktionapplication.entity.Tag;
import com.example.localinzzaktionapplication.listener.ItemTouchHelperListener;
import com.example.localinzzaktionapplication.listener.OnTagItemClickListener;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.viewHolder> implements ItemTouchHelperListener,OnTagItemClickListener {
    public ArrayList<Tag> tags = new ArrayList<>();
    OnTagItemClickListener listener;
    SQLiteDatabase database;
    TagDatabaseHelper helper;
    private Context context;

    public TagAdapter() {
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tag_item, parent, false);
        context = parent.getContext();
        return new viewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.setItem(tag);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void addItem(Tag tag) {
        tags.add(tag);
    }

    public void setItems(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public Tag getItem(int position) {
        return tags.get(position);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        helper = new TagDatabaseHelper(context);
        database = helper.getWritableDatabase();
        //이동할 객체 저장
        Tag tagFrom = tags.get(from_position);
        Tag tagTo = tags.get(to_position);
        // 이동할 객체 삭제
        tags.remove(from_position);
        // 이동하고 싶은 position에 추가
        tags.add(to_position, tagFrom);
        // Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        //이동쿼리

        String sqlFrom = "update TAG set TAG_POSITION='"+tagTo.getTagPosition()+"' where TAG_NM='"+tagFrom.getTagName()+"'";
        String sqlTo = "update TAG set TAG_POSITION='"+tagFrom.getTagPosition()+"' where TAG_NM='"+tagTo.getTagName()+"'";
        database.execSQL(sqlFrom);
        database.execSQL(sqlTo);
        printLog("이동 update 성공");
        return true;
    }
    @Override
    public void onItemSwipe(int position) {
        tags.remove(position);
        notifyItemRemoved(position);
    }
    //오른쪽 버튼 클릭시 삭제
    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        Tag tag = tags.get(position);
        String tagNo = tag.getTagNo();
        deleteTag(tagNo);
        tags.remove(position);
        notifyItemRemoved(position);
    }
    //삭제하면 데이터베이스 삭제
    public void deleteTag(String tagNo) {
        helper = new TagDatabaseHelper(context);
        database = helper.getWritableDatabase();
        String sql = "delete from tag where TAG_NO='"+tagNo+"'";
        printLog("delete sql: "+sql);
        database.execSQL(sql);
        printLog("delete 성공");
    }

    private void printLog(String data) {
        Log.d("adapter", data);
    }

    @Override
    public void OnItemClick(viewHolder holder, View view, int position) {
        if(listener!=null){
            listener.OnItemClick(holder,view,position);
        }
    }

    public void setOnItemClickListener(OnTagItemClickListener listener){
        this.listener = listener;
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tagCreatedView;
        CardView tagCardView;

        public viewHolder(@NonNull final View itemView, final OnTagItemClickListener listener) {
            super(itemView);
            tagCreatedView = itemView.findViewById(R.id.tagCreatedView);
            tagCardView = itemView.findViewById(R.id.tagCardView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.OnItemClick(viewHolder.this,view,position);
                    }
                }
            });
        }
        public void setItem(final Tag tag) {
            tagCreatedView.setText(tag.getTagName());
            tagCardView.setCardBackgroundColor(Color.parseColor(cardColor(tag.getRgbNo())));
        }
        public String cardColor(String colorNo){
            String hexColor = "#fffafa";
            if(colorNo.equals("0")){
                hexColor = "#E3DDCB";
            }else if(colorNo.equals("1")){
                hexColor = "#E2A6B4";
            }else if(colorNo.equals("2")){
                hexColor = "#F8E77F";
            }else if(colorNo.equals("3")){
                hexColor = "#BB9E8B";
            }else if(colorNo.equals("4")){
                hexColor = "#0B6DB7";
            }else if(colorNo.equals("5")){
                hexColor = "#6C71B5";
            }else if(colorNo.equals("6")){
                hexColor = "#9ED6C0";
            }else if(colorNo.equals("7")){
                hexColor = "#CBDD61";
            }else if(colorNo.equals("8")){
                hexColor = "#84A7D3";
            }else if(colorNo.equals("9")){
                hexColor = "#006494";
            }
            return hexColor;
        }
    }


}
