package com.example.localinzzaktionapplication.ui.write;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.localinzzaktionapplication.MainActivity;
import com.example.localinzzaktionapplication.R;
import com.example.localinzzaktionapplication.adapter.TagAdapter;
import com.example.localinzzaktionapplication.databaseHelper.TagDatabaseHelper;
import com.example.localinzzaktionapplication.entity.Tag;
import com.example.localinzzaktionapplication.listener.ItemTouchHelperCallback;
import com.example.localinzzaktionapplication.listener.OnTagItemClickListener;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "noteNo";

    // TODO: Rename and change types of parameters
    int noteNo = 0;

    public TagFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TagFragment newInstance(int noteNo) {
        TagFragment fragment = new TagFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, noteNo);
        fragment.setArguments(args);
        return fragment;
    }

    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteNo = getArguments().getInt(ARG_PARAM1);
        }
    }

    RecyclerView recyclerView;
    TagAdapter adapter;
    LinearLayoutManager layoutManager;
    EditText editCreateTag;
    Button btnColorPicker, btnCreateTag,btnSavedTag;
    SQLiteDatabase database;
    TagDatabaseHelper helper;
    ItemTouchHelper touchHelper;
    FlowLayout flowLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tag, container, false);
        editCreateTag = root.findViewById(R.id.editCreateTag);
        btnCreateTag = root.findViewById(R.id.btnCreateTag);
        recyclerView = root.findViewById(R.id.recyclerViewTag);
        btnColorPicker = root.findViewById(R.id.btnColorPicker);
        flowLayout = root.findViewById(R.id.layoutSelectedTag);
        btnSavedTag = root.findViewById(R.id.btnSavedTag);
        /*노트 작성 페이지의 노트번호 받기*/

        /*컬러피커*/
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TagAdapter();
        selectData();
        recyclerView.setAdapter(adapter);
        //ItemTouchHelper 생성
        touchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        touchHelper.attachToRecyclerView(recyclerView);

        /*태그 생성*/
        btnCreateTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable buttonBackground = (ColorDrawable) btnColorPicker.getBackground();
                int bgColor = buttonBackground.getColor();
                String hexColor = String.format("#%06X", (0xFFFFFF & bgColor));
                printLog("hexColor: "+hexColor);
                int tagColor = tagColor(hexColor);
                insertTagData(tagColor);
                selectData();
                btnColorPicker.setBackgroundColor(Color.parseColor("#b4c3ff"));
            }
        });


        final FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT /* layout_width */, FlowLayout.LayoutParams.WRAP_CONTENT /* layout_height*/);
        layoutParams.setMargins(10,10,10,10);
        selectTag(noteNo,layoutParams);
        adapter.setOnItemClickListener(new OnTagItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void OnItemClick(TagAdapter.viewHolder holder, View view, int position) {
                Tag tag = adapter.tags.get(position);
                final int tagNo = Integer.parseInt(tag.getTagNo());
                String tagName = tag.getTagName();
                String rgbNo = tag.getRgbNo();
                final TextView tv = new TextView(getContext());
                tv.setText(tag.getTagName());
                tv.setLayoutParams(layoutParams);
                tv.setId(tagNo);
                tv.setBackgroundColor(Color.parseColor(cardColor(tag.getRgbNo())));
                insertNote(noteNo,tagName,rgbNo);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteNote(tagNo);
                        flowLayout.removeView(tv);
                    }
                });
                flowLayout.addView(tv);
            }
        });

        btnSavedTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(2,noteNo);
            }
        });




        // Inflate the layout for this fragment
        return root;
    }

    private void selectTag(int noteNo,FlowLayout.LayoutParams layoutParams) {
        helper = new TagDatabaseHelper(getContext());
        database = helper.getReadableDatabase();
        String sql = "select * from TAG where NOTE_NO="+noteNo+" order by TAG_NO";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        printLog("count" + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToNext();
            final Tag tag = new Tag(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            printLog("write select 성공: " + tag.toString());
            final TextView tv = new TextView(getContext());
            tv.setText(tag.getTagName());
            tv.setLayoutParams(layoutParams);
            tv.setBackgroundColor(Color.parseColor(cardColor(tag.getRgbNo())));
            tv.setId(i);
            flowLayout.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sql = "delete from tag where TAG_NO='"+tag.getTagNo()+"'";
                    database.execSQL(sql);
                    printLog("데이터 삭제 성공: "+sql);
                    flowLayout.removeView(tv);
                }
            });
        }
        printLog("select 성공");
    }

    private void insertTagData(int tagColor) {
        helper = new TagDatabaseHelper(getContext());
        database = helper.getWritableDatabase();
        String tagName = editCreateTag.getText().toString();
        String sql = "select * from TAG where NOTE_NO='"+0+"'";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount()+1;
        printLog("데이터입력 전 count: "+count);
        String sql2 = "insert into TAG(NOTE_NO,TAG_NM,RGB_NO,TAG_POSITION) values("+0+",'"+tagName+ "',"+tagColor+","+count+")";
        database.execSQL(sql2);
        printLog("데이터입력 성공: "+sql2);
        init();
    }

    private void selectData() {
        helper = new TagDatabaseHelper(getContext());
        database = helper.getReadableDatabase();
        adapter.tags.clear();
        String sql = "select * from TAG where NOTE_NO=0 order by TAG_POSITION";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        printLog("count" + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToNext();
            Tag tag = new Tag(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            printLog("select note=0 성공: " + tag.toString());
            adapter.addItem(tag);
            adapter.notifyDataSetChanged();
        }
        printLog("select 성공");
    }

    private void insertNote(int noteNo,String tagName,String rgbNo){
        helper = new TagDatabaseHelper(getContext());
        database = helper.getReadableDatabase();
        String sql = "insert into TAG(NOTE_NO,TAG_NM,RGB_NO) values("+noteNo+",'"+tagName+"','"+rgbNo+"')";
        printLog(sql);
        database.execSQL(sql);
        printLog("데이터 업데이트 성공");
    }

    private void deleteNote(int tagNo){
        helper = new TagDatabaseHelper(getContext());
        database = helper.getReadableDatabase();
        String sql = "update TAG set NOTE_NO=0 where TAG_NO="+tagNo;
        database.execSQL(sql);
        printLog("데이터 삭제 성공");
    }


    private int tagColor(String hexColor){
        int colorNo = -1;
        if(hexColor.equals("#E3DDCB")){
            colorNo = 0;
        }else if(hexColor.equals("#E2A6B4")){
            colorNo = 1;
        }else if(hexColor.equals("#F8E77F")){
            colorNo = 2;
        }else if(hexColor.equals("#BB9E8B")){
            colorNo = 3;
        }else if(hexColor.equals("#0B6DB7")){
            colorNo = 4;
        }else if(hexColor.equals("#6C71B5")){
            colorNo = 5;
        }else if(hexColor.equals("#9ED6C0")){
            colorNo = 6;
        }else if(hexColor.equals("#CBDD61")){
            colorNo = 7;
        }else if(hexColor.equals("#84A7D3")){
            colorNo = 8;
        }else if(hexColor.equals("#006494")){
            colorNo = 9;
        }
        return colorNo;
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

    private void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker((Activity) getContext());  // ColorPicker 객체 생성
        final ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list
        colors.add("#E3DDCB");
        colors.add("#E2A6B4");
        colors.add("#F8E77F");
        colors.add("#BB9E8B");
        colors.add("#0B6DB7");
        colors.add("#6C71B5");
        colors.add("#9ED6C0");
        colors.add("#CBDD61");
        colors.add("#84A7D3");
        colors.add("#006494");
        colorPicker.setColors(colors)  // 만들어둔 list 적용
                .setColumns(5)  // 5열로 설정
                .setRoundColorButton(true)  // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        btnColorPicker.setBackgroundColor(color);  // OK 버튼 클릭 시 이벤트
                        printLog("position: "+position);
                        printLog("color: "+colors.get(position));

                    }
                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }
    private void printLog(String data) {
        Log.d("TagActivity", data);
    }
    private void init() {
        editCreateTag.setText("");
    }
}