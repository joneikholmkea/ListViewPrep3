package com.example.listviewprep3.repo;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
