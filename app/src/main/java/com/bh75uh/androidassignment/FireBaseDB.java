package com.bh75uh.androidassignment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FireBaseDB extends AppCompatActivity {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    public StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    /**
     * Used to run code if user is logged in or not (call inside onStart)
     * @param auth Lambda to execute if the user isn't logged in
     * @param activity Lambda to execute if the user is logged in
     */
    protected void onStartFbImplementation(IFirebaseAuth auth, Runnable activity){
        //Check if user is signed in (non-null) and update UI accordingly.
        if(getUser() == null) {
            auth.onStartAuth();
            return;
        }
        activity.run();
    }

    /**
     * Queries the data from FireStore Database to be used in the adapter
     * @param id Logged in user id
     * @return
     */
    protected FirestoreRecyclerOptions<Item> queryDocuments(String id){
        Query query = fireStore.collection(id).orderBy("itemName", Query.Direction.ASCENDING);
        return new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();
    }

    public FirebaseUser getUser(){
        return mAuth.getCurrentUser();
    }

    public String getUserID(){
        return mAuth.getCurrentUser().getUid();
    }

    /**
     * Creates a new document in the specified collection id
     * @param data Data to be inserted in the document
     * @param id Id of the collection
     * @param consumer Code to be executed on success
     */
    public void createDocument(Map data, String id, Consumer consumer){
        fireStore.collection(id)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        consumer.accept(documentReference);
                    }
                });
    }

    /**
     * Gets a specific document from the specified collection id
     * @param id Id of the collection
     * @param consumer Code to be executed on complete
     */
    public void getDocument(String id, Consumer consumer){
        fireStore.collection(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(id)){ //Verifying if the id provided matches the document id
                                    consumer.accept(document);
                                }
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * Updates documents from the specified collection id
     * @param id Id of the collection
     * @param data Data to be updated
     * @param runnable Code to be executed on success
     */
    public void updateDocument(String id, Map<String, Object> data, Runnable runnable){
        fireStore.collection(mAuth.getCurrentUser().getUid())
                .document(id)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        runnable.run();
                    }
                });

    }

    /**
     * Deletes a specific document
     * @param id Id of the document
     * @param runnable Code to be executed on success
     */
    public void deleteDocument(String id, Runnable runnable){
        fireStore.collection(mAuth.getCurrentUser().getUid())
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        runnable.run();
                    }
                });
    }

    /**
     * Downloads an image from the Firebase Storage
     * @param imagePath Image path in Firebase Storage
     * @param consumer Code to be executed on success
     */
    public void downloadImage(String imagePath, Consumer consumer){
        StorageReference photoReference= storageRef.child(imagePath);

        final long FOUR_MEGABYTE = 1024 * 1024 * 4;
        photoReference.getBytes(FOUR_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                consumer.accept(bmp);
            }
        });
    }

    /**
     * Updates an item image
     * @param path Image path in FIrebase Storage
     * @param id Id of the document
     * @param runnable Code to be executed on complete
     */
    public void updateImage(String path, String id, Runnable runnable){
        fireStore.collection(mAuth.getCurrentUser().getUid())
                .document(id)
                .update("imagePath", path).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        runnable.run();
                    }
                });
    }

    /**
     * Logs the user off
     */
    public void LogOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
