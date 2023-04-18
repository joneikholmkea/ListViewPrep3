package com.example.listviewprep3.repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FirebaseService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private String collection = "notes";

    public FirebaseService() {
        storageRef = storage.getReference();
        startListener();
    }

    public void saveImage(byte[] is){
        StorageReference spaceRef = storageRef.child("space.jpg");
        UploadTask uploadTask = spaceRef.putBytes(is);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            System.out.println("Success in upload to storage " + is.toString());
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        });

        uploadTask.addOnFailureListener(exception -> {
            System.out.println("failed to upload to storage " + is.toString());
        });
    }

    public void saveImageWithStream(InputStream is){
        StorageReference spaceRef = storageRef.child("space.jpg");
        UploadTask uploadTask = spaceRef.putStream(is);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            System.out.println("Success in upload to storage with Stream " + is.toString());
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        });

        uploadTask.addOnFailureListener(exception -> {
            System.out.println("failed to upload to storage with Stream " + is.toString());
        });
    }

    public void downloadImageFromFirebase(String imagePath) {
        StorageReference storageReference = storageRef.child(imagePath);
        storageReference.getBytes(1024 * 1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            System.out.println("received image: " + bitmap.getWidth() + " " + bitmap.getHeight());
        }).addOnFailureListener(e -> {
            System.out.println("error fetching image from firebase: " + e.getMessage());
        });
    }

    public void addData(){
        System.out.println("firebase update() called");
        Map<String, Object> data = new HashMap<>();
        data.put("text", "greeting from Android");
        db.collection(collection).document()
                .set(data); // will overwrite if doc exist. Else create new doc. Use update() to avoid creating new doc.
        // Use .set(data, SetOptions.merge()) to only update specified field, and leave the rest unchanged.
    }

    private void startListener(){
        db.collection(collection).addSnapshotListener((value, error) -> {
            System.out.println("items:");
            for(DocumentSnapshot doc : value.getDocuments()){
                System.out.println(doc.get("text"));
            }
        });
    }
}
