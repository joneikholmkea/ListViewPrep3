package com.example.listviewprep3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listviewprep3.adapter.MyAdapter;
import com.example.listviewprep3.model.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Item> names = new ArrayList();
    private ArrayAdapter<String> adapter;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addItems();
        listView = findViewById(R.id.listView);
        //adapter = new ArrayAdapter<>(this, R.layout.myrow, R.id.textView, names);
        myAdapter = new MyAdapter(names, this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailViewActivity.class);
            String text = ((TextView)view.findViewById(R.id.textView)).getText().toString();
            int image = (Integer) view.findViewById(R.id.imageView).getTag();
            intent.putExtra("text", text);
            intent.putExtra("image", image);
            startActivity(intent);
        });
    }

    private void addItems() {
        names.add(new Item("Road Bike", R.drawable.road));
        names.add(new Item("Cruise Bike", R.drawable.cruise));
        names.add(new Item("Fixed Bike", R.drawable.fixed));
        names.add(new Item("Utility Bike", R.drawable.utility));
        names.add(new Item("BMX Bike", R.drawable.bmx));
    }
}