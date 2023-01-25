package com.bh75uh.androidassignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class DemoAdapter extends FirestoreRecyclerAdapter<Item, DemoAdapter.ViewHolder> {

    // Pass in the contact array into the constructor
    public DemoAdapter(FirestoreRecyclerOptions<Item> options) {
        super(options);
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

//    @Override
//    public int getItemCount() {
//        return items.size();
//    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Item model) {
        holder.itemName.setText(model.getItemName());
        Button button = holder.editBtn;
        button.setText("Edit");
//        button.setTag(model.getDocumentId());
        DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());
        button.setTag(doc.getId());
    }

    // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView itemName;
            public Button editBtn;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                itemName = (TextView) itemView.findViewById(R.id.txtDisplayItemName);
                editBtn = (Button) itemView.findViewById(R.id.editBtn);
            }
        }
}
