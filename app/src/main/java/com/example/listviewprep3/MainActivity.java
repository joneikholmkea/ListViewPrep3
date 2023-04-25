package com.example.listviewprep3;

import static android.content.Intent.ACTION_PICK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listviewprep3.adapter.MyAdapter;
import com.example.listviewprep3.model.Item;
import com.example.listviewprep3.repo.FirebaseService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Item> names = new ArrayList();
    private ArrayAdapter<String> adapter;
    private MyAdapter myAdapter;
    private FirebaseService fs;
    private ActivityResultLauncher<Intent> launchGalleryForResult;
    private ActivityResultLauncher<Intent> launchCameraForResult;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fs = new FirebaseService();
        imageView = findViewById(R.id.imageView);

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
        createLaunchers();
        fs.downloadImageFromFirebase("space.jpg");
    }

    private void createLaunchers() {
        launchGalleryForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intentData = result.getData();
                        imageView.setImageURI(intentData.getData()); // when you set an existing file into imageView
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                        fs.saveImage(getBytes(bitmap)); // use this for smaller files
                        fs.saveImageWithStream(new ByteArrayInputStream(getBytes(bitmap)));
                    }
                });
        launchCameraForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intentData = result.getData();
                        Bitmap bitmap = (Bitmap) intentData.getExtras().get("data");
                        //bitmap = drawTextToBitmap(bitmap,"you look good");
                        saveImageToPhotos(bitmap);
//                      bitmap = Bitmap.createScaledBitmap(bitmap, 300, 200, true); // to resize the image
                        imageView.setImageBitmap(bitmap); // when you set an image from memory into imageView
                        fs.saveImage(getBytes(bitmap));
                    }
                });
    }

    public void cameraBtnClicked(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launchCameraForResult.launch(intent);
    }


    public void galleryBtnPressed(View view){
        Intent intent = new Intent(ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        launchGalleryForResult.launch(intent);
        try{

        }catch (Exception e){
            System.out.println("error " + e.getMessage());
        }
    }

    public void saveImageToPhotos(Bitmap bitmap){
        String result = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "myImage", "simple image");
        System.out.println("result of saving image to Photos: " + result);
    }

    @NonNull
    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap drawTextToBitmap(Bitmap image, String gText) {
        Bitmap.Config bitmapConfig = image.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        image = image.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// new antialised Paint
        paint.setColor(Color.rgb(161, 161, 161));
        paint.setTextSize((int) (15)); // text size in pixels
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // text shadow
        canvas.drawText(gText, 10, 100, paint);
        return image;
    }

    private void addItems() {
        names.add(new Item("Road Bike", R.drawable.road));
        names.add(new Item("Cruise Bike", R.drawable.cruise));
        names.add(new Item("Fixed Bike", R.drawable.fixed));
        names.add(new Item("Utility Bike", R.drawable.utility));
        names.add(new Item("BMX Bike", R.drawable.bmx));
    }
}