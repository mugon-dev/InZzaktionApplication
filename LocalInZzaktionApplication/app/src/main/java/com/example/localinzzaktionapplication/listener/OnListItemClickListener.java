package com.example.localinzzaktionapplication.listener;

import android.view.View;

import com.example.localinzzaktionapplication.adapter.ListNoteAdapter;

public interface OnListItemClickListener {
    public void OnItemClick(ListNoteAdapter.ViewHolder holder, View view, int position);
}
