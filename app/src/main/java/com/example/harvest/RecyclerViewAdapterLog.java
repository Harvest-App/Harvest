package com.example.harvest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapterLog extends RecyclerView.Adapter<RecyclerViewAdapterLog.MyViewHolder> {

    Context context;
    ArrayList<OurLog> logArrayList;
    private RecyclerViewAdapterLog.OnItemClickListener myListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(RecyclerViewAdapterLog.OnItemClickListener listener){
        myListener = listener;
    }

    public RecyclerViewAdapterLog(Context context, ArrayList<OurLog> logArrayList) {
        this.context = context;
        this.logArrayList = logArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterLog.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.class_profile_log,parent,false);
        return new MyViewHolder(v,myListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterLog.MyViewHolder holder, int position) {

        OurLog log = logArrayList.get(position);

        holder.logName.setText(log.getLogName());
        holder.timeCreated.setText(log.getTimeCreated());
    }

    @Override
    public int getItemCount() {
        return logArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView logName, timeCreated;


        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            logName=itemView.findViewById(R.id.logNameDisplay);
            timeCreated=itemView.findViewById(R.id.timeCreatedLog);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                    if (listener != null){//makes listener final
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){//makes sure that an item exists at that position
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}