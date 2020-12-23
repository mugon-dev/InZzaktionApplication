package com.example.inzzaktionapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ViewHolder> implements OnListItemClickListener {
    private static TagDatabaseHelper helper;
    private static SQLiteDatabase database;
    private Context context;
    ArrayList<String> noteTitleList = new ArrayList<>();
    OnListItemClickListener listener;
    String noteNo;

    public ListNoteAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item,parent,false);
        context = parent.getContext();
        return new ViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = noteTitleList.get(position).toString();
        holder.setItem(title,noteNo);
        printLog("bind noteNo: "+noteNo);
    }

    @Override
    public int getItemCount() {
        return noteTitleList.size();
    }

    @Override
    public void OnItemClick(ViewHolder holder, View view, int position) {

    }
    public void addNoteItem(String title,String note) {
        noteTitleList.add(title);
        noteNo = note;
        printLog("noteNo: "+noteNo);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        EditText noteListTitle;
        FlowLayout listTagLayout;
        List<Tag> tags=null;
        public ViewHolder(@NonNull final View itemView, final OnListItemClickListener listener) {
            super(itemView);
            noteListTitle = itemView.findViewById(R.id.noteListTitle);
            listTagLayout = itemView.findViewById(R.id.listTagLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.OnItemClick(ViewHolder.this,view,position);
                    }
                }
            });
        }

        public void setItem(final String title, String noteNo) {
            noteListTitle.setText(title);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT /* layout_width */, FlowLayout.LayoutParams.WRAP_CONTENT /* layout_height*/);
            layoutParams.setMargins(10, 10, 10, 10);
            helper = new TagDatabaseHelper(itemView.getContext());
            database = helper.getReadableDatabase();
            String sql = "select * from TAG where NOTE_NO='" + noteNo + "' order by TAG_NO";
            String sql2 = "select * from TAG";
            printLog("setitem sql"+sql2);
            Cursor cursor = database.rawQuery(sql2, null);
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                cursor.moveToNext();
                final Tag tag = new Tag(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                printLog("tag item"+tag.toString());
                TextView tv = new TextView(itemView.getContext());
                tv.setText(tag.getTagName());
                printLog("list tag name"+tag.getTagName());
                tv.setLayoutParams(layoutParams);
                tv.setBackgroundColor(Color.parseColor(cardColor(tag.getRgbNo())));
                tv.setId(i);
                listTagLayout.addView(tv);
            }
        }
        private void printLog(String data) {
            Log.d("ListAdapt", data);
        }
        private String cardColor(String colorNo){
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
    private void printLog(String data) {
        Log.d("ListAdapt", data);
    }
}
