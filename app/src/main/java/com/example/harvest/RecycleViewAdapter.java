package com.example.harvest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private ArrayList<ProduceItem> mProduceList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView foodItemView;
        public TextView supertypeView;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            foodItemView = itemView.findViewById(R.id.foodItem);
            supertypeView = itemView.findViewById(R.id.supertype);

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

    public RecycleViewAdapter(ArrayList<ProduceItem> produceList) {
        mProduceList = produceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_produce_item,
                parent, false);
        ViewHolder vh = new ViewHolder(v,mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProduceItem currentItem = mProduceList.get(position);
        holder.foodItemView.setText(currentItem.getFoodType());
        holder.supertypeView.setText(currentItem.getSuperType());
    }

    @Override
    public int getItemCount() {
        return mProduceList.size();
    }

    public void filterList(ArrayList<ProduceItem> filteredList) {
        mProduceList = filteredList;
        notifyDataSetChanged();
    }
}