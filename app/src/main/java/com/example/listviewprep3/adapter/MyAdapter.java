package com.example.listviewprep3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.listviewprep3.R;
import com.example.listviewprep3.model.Item;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Item> items;
    private LayoutInflater layoutInflater;

    public MyAdapter(List<Item> items, Context context) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View linearLayout, ViewGroup listView) {
        if(linearLayout==null){
            linearLayout = layoutInflater.inflate(R.layout.myrow, null);
            // root: optional parent of inflated view
        }
        System.out.println("View type: " + linearLayout.getClass().getName());
        System.out.println("ViewGroup type: " + listView.getClass().getName());

        TextView textView = linearLayout.findViewById(R.id.textView);
        ImageView imageView = linearLayout.findViewById(R.id.imageView);
        imageView.setImageResource(items.get(position).getImage());
        imageView.setTag(items.get(position).getImage());
        textView.setText(items.get(position).getText());
        return linearLayout;
    }
}
