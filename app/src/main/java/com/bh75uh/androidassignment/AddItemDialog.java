package com.bh75uh.androidassignment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddItemDialog extends AppCompatDialogFragment implements IFireBase {

    public View view;
    public Activity parent;
    public ListItems parentClass;
    Uri selectedImage;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    ImageView IVPreviewImage;
    Button btnSelectImage;
    String Id;

    public AddItemDialog(Object parent){
        this.parent = (Activity) parent;
        this.parentClass = (ListItems) parent;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.add_dialog, null);

        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        IVPreviewImage = view.findViewById(R.id.imgPreview);

        builder.setView(view).setTitle("Add Item")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createUserDB();
                parent.finish();
                startActivity(new Intent(parent, ListItems.class));
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });


        return builder.create();
    }

    public void createUserDB(){
        String itemName = ((EditText) view.findViewById(R.id.txtItemName)).getText().toString();
        String price = ((EditText) view.findViewById(R.id.txtPrice)).getText().toString();
        String location = ((EditText) view.findViewById(R.id.txtLocation)).getText().toString();
        String description = ((EditText) view.findViewById(R.id.txtDescription)).getText().toString();

        Map<String, Object> userData = new HashMap<>();
        userData.put("itemName", itemName);
        userData.put("price", price);
        userData.put("purchased", false);
        userData.put("geotag", location);
        userData.put("description", description);
        userData.put("imagePath", "images/");

        db.collection(mAuth.getCurrentUser().getUid())
                .add(userData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(parent, "Item added successfully", Toast.LENGTH_LONG).show();
                        Id = documentReference.getId();
                        updateUserDB("images/" + mAuth.getCurrentUser().getUid() + "/" +Id+".png");
                        uploadImage(selectedImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(parent, "Error! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void uploadImage(Uri image){
        storageRef.child("images/"+ mAuth.getCurrentUser().getUid() + "/" +Id+".png").putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("IMAGE_u", "success uploading image");
                Log.d("DOC_ID", Id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("IMAGE_u", "failed uploading image");
            }
        });
    }

    private void imageChooser()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("IMAGE_u", data.getData().toString());
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    IVPreviewImage.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    IVPreviewImage.setImageURI(selectedImage);
                }
                break;
        }
    }

    public void updateUserDB(String path){
        db.collection(mAuth.getCurrentUser().getUid())
                .document(Id)
                .update("imagePath", path);
    }
}
