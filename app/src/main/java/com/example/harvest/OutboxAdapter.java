package com.example.harvest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class OutboxAdapter extends FirestoreRecyclerAdapter<SentInvite, OutboxAdapter.OutboxHolder> {

    //listener variable for onclicklistener
    //  private InboxAdapter.OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OutboxAdapter(@NonNull FirestoreRecyclerOptions<SentInvite> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull OutboxHolder holder, int position, @NonNull SentInvite model) {
        //tell the adapter what we want to put in each view in our cardview layout
        String nameWithUsername = model.getFriendName()+" ("+model.getFriendUsername()+")";
        String logNameHeading = "Log name: "+model.getLogName();
        String statusHeading = "Status: "+model.getStatus();
        holder.friendName.setText(nameWithUsername);
        holder.logName.setText(logNameHeading);
        holder.status.setText(statusHeading);
    }

    @NonNull
    @Override
    public OutboxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //tell the adapter which layout it has to inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_outbox_item,parent, false);
        return new OutboxHolder(v);
    }
    //adapter gets data from data source into recyclerview

    class OutboxHolder extends RecyclerView.ViewHolder{

        TextView friendName, logName, status;

        public OutboxHolder(@NonNull View itemView) {
            super(itemView);

            friendName = itemView.findViewById(R.id.friendName);
            logName = itemView.findViewById(R.id.logName);
            status = itemView.findViewById(R.id.status);
        }
    }
//    //interface required for clicks to be sent to the underlying class
//    public interface OnItemClickListener {
//        void onItemClick(DocumentSnapshot documentSnapshot, int position);
//    }
//
//    public void setOnItemClickListener(InboxAdapter.OnItemClickListener listener) {
//        this.listener = listener;
//    }
}
