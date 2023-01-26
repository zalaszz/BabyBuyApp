package com.bh75uh.androidassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditItemActivity extends AppCompatActivity implements IFireBase {

    public String id;
    ImageView IVPreviewImage;
    Button btnSelectImage;
    String phoneNumber;
    private static final int RESULT_PICK_CONTACT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        id = getIntent().getStringExtra("id");

        readUserDB();

        btnSelectImage = findViewById(R.id.btnEditImgSelect);
        IVPreviewImage = findViewById(R.id.editImgPreview);

        findViewById(R.id.btnConfirmEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDB();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_delete){
            deleteUserDB();
            return true;
        }else if (item.getItemId() == R.id.menu_sendSMS){
            Intent in = new Intent (Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult (in, RESULT_PICK_CONTACT);
        }

        return super.onOptionsItemSelected(item);
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneNo = null;
            Uri uri = data.getData ();
            cursor = getContentResolver ().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            int phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);

            phoneNumber = cursor.getString (phoneIndex);

        } catch (Exception e) {
            e.printStackTrace ();
        }
        sendSMS();
    }

    public void sendSMS(){
        String itemName = String.valueOf(((EditText)findViewById(R.id.txtEditItemName)).getText());
        String itemPrice = String.valueOf(((EditText)findViewById(R.id.txtEditPrice)).getText());
        String geoTag = String.valueOf(((EditText)findViewById(R.id.txtEditGeoTag)).getText());


        String smsMessage = "The item " +itemName+ " with the price "+itemPrice+" can be purchased here "+geoTag+".";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", smsMessage);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    public void readUserDB(){
        db.collection(mAuth.getCurrentUser().getUid())
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("YEET", String.valueOf(document.getId().equals(id)));
                        if(document.getId().equals(id)){
                            Log.d("YEET", String.valueOf(Boolean.parseBoolean(document.getData().get("purchased").toString())));
                            ((EditText) findViewById(R.id.txtEditItemName)).setText(String.valueOf(document.getData().get("itemName")));
                            ((EditText) findViewById(R.id.txtEditGeoTag)).setText(String.valueOf(document.getData().get("geotag")));
                            ((EditText) findViewById(R.id.txtEditPrice)).setText(String.valueOf(document.getData().get("price")));
                            ((EditText) findViewById(R.id.txtEditDescription)).setText(String.valueOf(document.getData().get("description")));
                            ((Switch) findViewById(R.id.switchPurchased)).setChecked(Boolean.parseBoolean(document.getData().get("purchased").toString()));
                            downloadImage(String.valueOf(document.getData().get("imagePath")));
                        }
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }

    public void updateUserDB(){
        Map<String, Object> editedData = new HashMap<String, Object>(){
            {
                put("itemName", ((EditText) findViewById(R.id.txtEditItemName)).getText().toString());
                put("price", ((EditText) findViewById(R.id.txtEditPrice)).getText().toString());
                put("geotag", ((EditText) findViewById(R.id.txtEditGeoTag)).getText().toString());
                put("purchased", ((Switch) findViewById(R.id.switchPurchased)).isChecked());
                put("description", ((EditText) findViewById(R.id.txtEditDescription)).getText().toString());
            }
        };

        db.collection(mAuth.getCurrentUser().getUid())
                .document(id)
                .update(editedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Item edited with success!", Toast.LENGTH_LONG);
                        startActivity(new Intent(getApplicationContext(), ListItems.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! Something went wrong", Toast.LENGTH_LONG);
                    }
                });

    }

    public void deleteUserDB(){
        db.collection(mAuth.getCurrentUser().getUid())
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Item deleted with success!", Toast.LENGTH_LONG);
                        startActivity(new Intent(getApplicationContext(), ListItems.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! Something went wrong", Toast.LENGTH_LONG);
                    }
                });

    }

    private void imageChooser()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }

    private void downloadImage(String path){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child(path);

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                IVPreviewImage.setImageBitmap(bmp);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    IVPreviewImage.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    IVPreviewImage.setImageURI(selectedImage);
                }
                break;
            case RESULT_PICK_CONTACT:
                contactPicked (data);
                break;
        }
    }
}