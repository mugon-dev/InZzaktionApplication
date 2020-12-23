package com.example.localinzzaktionapplication.ui.list;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localinzzaktionapplication.MainActivity;
import com.example.localinzzaktionapplication.R;
import com.example.localinzzaktionapplication.adapter.ListNoteAdapter;
import com.example.localinzzaktionapplication.databaseHelper.TagDatabaseHelper;
import com.example.localinzzaktionapplication.databaseHelper.WriteDatabaseHelper;
import com.example.localinzzaktionapplication.listener.OnListItemClickListener;

public class ListFragment extends Fragment {
    MainActivity mainActivity;

    private ListViewModel galleryViewModel;
    EditText editSearch;
    ImageView listSearch;
    Button btnCreateNote;
    RecyclerView recyclerView;

    String memberNo="1";
    //int noteNo = 2;

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

    LinearLayoutManager layoutManager;
    ListNoteAdapter listNoteAdapter;
    SQLiteDatabase database;
    TagDatabaseHelper tagHelper;
    WriteDatabaseHelper writeDatabaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(ListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        editSearch = root.findViewById(R.id.editSearch);
        listSearch = root.findViewById(R.id.listSearch);
        btnCreateNote = root.findViewById(R.id.btnCreateNote);
        recyclerView = root.findViewById(R.id.listRecyclerView);
        final int noteNo = mainActivity.noteCount(memberNo);
        printLog("noteNo: "+noteNo);

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listNoteAdapter = new ListNoteAdapter();
        selectNoteData(memberNo);
        recyclerView.setAdapter(listNoteAdapter);
        listNoteAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void OnItemClick(ListNoteAdapter.ViewHolder holder, View view, int position) {
                mainActivity.changeFragment(2,1);
            }
        });


        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(2,noteNo);
            }
        });


        return root;
    }

    private int noteCount(String memberNo){
        writeDatabaseHelper = new WriteDatabaseHelper(getContext());
        database = writeDatabaseHelper.getReadableDatabase();
        String sql = "select * from NOTE where MEMBER_NO='" + memberNo + "'";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        return count;
    }

    private void selectNoteData(String memberNo) {
        writeDatabaseHelper = new WriteDatabaseHelper(getContext());
        database = writeDatabaseHelper.getReadableDatabase();
        //listAdapter.writes.clear();
        String sql = "select NOTE_TITLE,NOTE_NO from NOTE where MEMBER_NO='" + memberNo + "'";
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            cursor.moveToNext();
            listNoteAdapter.addNoteItem(cursor.getString(0), cursor.getInt(1));
            printLog("list note title: "+cursor.getString(0));
            printLog("list note no: "+cursor.getInt(1));
            listNoteAdapter.notifyDataSetChanged();
        }
        printLog("note title select 성공");
    }
    private void printLog(String data) {
        Log.d("ListActivity", data);
    }
}