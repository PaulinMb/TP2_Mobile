package com.example.tp2_mob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int[] logos;
    private LayoutInflater inflater;
    private MainActivity mainActivity;

    public ImageAdapter(Context context, int[] logos, MainActivity mainActivity) {
        this.context = context;
        this.logos = logos;
        this.inflater = (LayoutInflater.from(context));
        this.mainActivity = mainActivity;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.image_gridview, null);
        ImageView icon = (ImageView) view.findViewById(R.id.logo);
        icon.setImageResource(logos[i]);
        icon.setOnClickListener((v) -> mainActivity.selectImage(v, i));
        return view;
    }

    @Override
    public int getCount() {
        return logos.length;
    }

    @Override
    public ImageView getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return this.logos[i];
    }
}
