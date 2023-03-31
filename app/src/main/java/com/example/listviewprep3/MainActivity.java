package com.example.listviewprep3;

import static android.content.Intent.ACTION_PICK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
    }

    private void createLaunchers() {
        launchGalleryForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageView.setImageURI(data.getData());
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        fs.saveImage(getBytes(bitmap));
                    }
                });
        launchCameraForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                                    bitmap.getHeight(), true);
                        imageView.setImageBitmap(bitmap);
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

    @NonNull
    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        System.out.println("result from permission quest: ");
//        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            System.out.println("YES, we may save !!");
//        }
//    }

//    private void handlePermissionUpdate() {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); //ask for p.
//        }
//    }

    private void addItems() {
        names.add(new Item("Road Bike", R.drawable.road));
        names.add(new Item("Cruise Bike", R.drawable.cruise));
        names.add(new Item("Fixed Bike", R.drawable.fixed));
        names.add(new Item("Utility Bike", R.drawable.utility));
        names.add(new Item("BMX Bike", R.drawable.bmx));
    }
}