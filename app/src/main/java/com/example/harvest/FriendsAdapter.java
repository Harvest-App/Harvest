package com.example.harvest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class FriendsAdapter extends FirestoreRecyclerAdapter<User, FriendsAdapter.FriendHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    //listener variable for onclicklistener
    private OnItemClickListener listener;

    public FriendsAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendHolder holder, int position, @NonNull User model) {
    //tell the adapter what we want to put in each view in our cardview layout
        holder.name.setText(model.getFullName());
        holder.username.setText(model.getUsername());
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //tell the adapter which layout it has to inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_user_item,parent, false);
        return new FriendHolder(v);
    }

//adapter gets data from data source into recyclerview

    class FriendHolder extends RecyclerView.ViewHolder{

        //initialise the elements you want
        TextView name, username;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);

            //actually define the different elements you want
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);

            //defining an onclicklistener so that something happens when we click a card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();

                    //prevents crash if an item is clicked while it is being deleted
                    //makes sure the click is valid
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        //our listener will be our AddFriends activity
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                    //calling interface method on listener so we can send a click there
                }
            });
        }
    }
//interface required for clicks to be sent to the underlying class
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
