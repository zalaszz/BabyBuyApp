package com.bh75uh.androidassignment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DemoAdapter extends FirestoreRecyclerAdapter<Item, DemoAdapter.ViewHolder> {

    Activity ac;

    // Pass in the contact array into the constructor
    public DemoAdapter(FirestoreRecyclerOptions<Item> options, Activity a) {
        super(options);
        ac = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_model, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Item model) {
        holder.itemName.setText(model.getItemName());
        Button button = holder.editBtn;
        ImageView img = holder.itemDisplayImg;
        button.setText("Edit");
        DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());
        button.setTag(doc.getId());

        Log.d("MERDA", model.getImagePath());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child(model.getImagePath());

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("ENTROU MERDA FDS", model.getImagePath());
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(bmp);
            }
        });
    }

    // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView itemName;
            public Button editBtn;
            public ImageView itemDisplayImg;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                itemName = (TextView) itemView.findViewById(R.id.txtDisplayItemName);
                editBtn = (Button) itemView.findViewById(R.id.editBtn);
                itemDisplayImg = (ImageView) itemView.findViewById(R.id.itemDisplayImg);
            }
        }
}
