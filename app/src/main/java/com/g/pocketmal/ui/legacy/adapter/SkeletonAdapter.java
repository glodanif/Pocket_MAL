package com.g.pocketmal.ui.legacy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class SkeletonAdapter<T> extends BaseAdapter {

    private List<T> data = new ArrayList<>();

    private final Context context;
    private final LayoutInflater inflater;

    public SkeletonAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data != null ? data : new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }
}
