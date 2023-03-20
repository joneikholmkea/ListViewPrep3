package com.example.listviewprep3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailViewActivity extends AppCompatActivity {

    private TextView textView2;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        textView2 = findViewById(R.id.textView2);
        imageView2 = findViewById(R.id.imageView2);
        String message = getIntent().getStringExtra("text");
        int image = getIntent().getIntExtra("image", 0);
        textView2.setText(message);
        imageView2.setImageResource(image);
    }
}