package com.example.localinzzaktionapplication.listener;

import android.view.View;

import com.example.localinzzaktionapplication.adapter.TagAdapter;

public interface OnTagItemClickListener {
    public void OnItemClick(TagAdapter.viewHolder holder, View view, int position);
}
