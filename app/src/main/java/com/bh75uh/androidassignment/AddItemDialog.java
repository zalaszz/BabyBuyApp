package com.bh75uh.androidassignment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.Reflection;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.Sleeper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AddItemDialog extends AppCompatDialogFragment implements IFireBase {

    public View view;
    public Activity parent;
    Uri selectedImage;
    ImageView IVPreviewImage;
    Button btnSelectImage;
    String Id;
    FireBaseDB fDB;

    public AddItemDialog(Object parent){
        this.parent = (Activity) parent;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.add_dialog, null);

        fDB = new FireBaseDB();

        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        IVPreviewImage = view.findViewById(R.id.imgPreview);

        builder.setView(view).setTitle("Add Item")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createUserDB();
                parent.finish();
                startActivity(parent.getIntent());
                ListItemsActivity.needsRefresh = true;
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

    //Creating the item
    public void createUserDB(){
        //Getting data from fields
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

        fDB.createDocument(userData, mAuth.getCurrentUser().getUid(), (document) -> {
            DocumentReference doc = (DocumentReference) document;
            Toast.makeText(parent, "Item added successfully", Toast.LENGTH_LONG).show();
            Id = doc.getId();
            //Updating the image to the path specified
            fDB.updateImage("images/" + mAuth.getCurrentUser().getUid() + "/" + Id + ".png", Id, () -> {
                Log.d("HAAAA", parent.toString());
            });
            fDB.storageRef.child("images/"+ mAuth.getCurrentUser().getUid() + "/" +Id+".png").putFile(selectedImage);
        });
    }

    /**
     * Gets an image from the gallery
     */
    private void imageChooser()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}
