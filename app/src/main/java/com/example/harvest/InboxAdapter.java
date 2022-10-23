package com.example.harvest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class InboxAdapter extends FirestoreRecyclerAdapter<GotInvite, InboxAdapter.InboxHolder> {


    //listener variable for OnClickListener
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public InboxAdapter(@NonNull FirestoreRecyclerOptions<GotInvite> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull InboxHolder holder, int position, @NonNull GotInvite model) {

        //tell the adapter what we want to put in each view in our cardview layout

        //information of the person who invited the user and the log they're invited to
        String userString = "Username: "+model.getSenderUsername();
        String logHeading = "Log name: "+model.getLogName();
        holder.inviter.setText(model.getSenderName());
        holder.username.setText(userString);
        holder.logName.setText(logHeading);

    }

    @NonNull
    @Override
    public InboxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //tell the adapter which layout it has to inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_inbox_item,parent, false);
        return new InboxHolder(v);
    }
    //adapter gets data from data source into recyclerview

    class InboxHolder extends RecyclerView.ViewHolder{

        TextView inviter, username, logName;
        ImageView rejectIcon, acceptIcon;

        public InboxHolder(@NonNull View itemView) {
            super(itemView);

            inviter = itemView.findViewById(R.id.inviter);
            username = itemView.findViewById(R.id.username);
            logName = itemView.findViewById(R.id.logName);
            rejectIcon = itemView.findViewById(R.id.image_reject);
            acceptIcon = itemView.findViewById(R.id.image_accept);

            rejectIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();

                    //prevents crash if an item is clicked while it is being deleted
                    //makes sure the click is valid
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        //our listener will be our FriendsManager activity
                        listener.onRejectClick(getSnapshots().getSnapshot(position), position);
                    }
                    //calling interface method on listener so we can send a click there
                }
            });
            acceptIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();

                    //prevents crash if an item is clicked while it is being deleted
                    //makes sure the click is valid
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        //our listener will be our FriendsManager activity
                        listener.onAcceptClick(getSnapshots().getSnapshot(position), position);
                    }
                    //calling interface method on listener so we can send a click there
                }
            });

        }
    }
    //interface required for clicks to be sent to the underlying class
    public interface OnItemClickListener {
      //  void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onAcceptClick(DocumentSnapshot documentSnapshot,int position);
        void onRejectClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(InboxAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
