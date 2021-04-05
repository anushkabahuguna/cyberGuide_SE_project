package com.example.cyberguide2.utils;

import android.annotation.SuppressLint;
import android.view.View;

public interface OnRecyclerViewItemClickListener {
    @SuppressLint("NonConstantResourceId")
    public abstract void onItemClick(int position, View view);

}
