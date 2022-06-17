package com.example.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Note> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, List<Note> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = mData.get(position).getContent();
        String createdAt = getSimpleDate(mData.get(position).getCreatedAt());
        holder.createdAtTextView.setText(createdAt);
        holder.contentTextView.setText(content);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView createdAtTextView;
        TextView contentTextView;

        ViewHolder(View itemView) {
            super(itemView);
            createdAtTextView = itemView.findViewById(R.id.tvTitle);
            contentTextView = itemView.findViewById(R.id.tvContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(mData.get(id));
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // convert Date to simpler format
    public String getSimpleDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormatter.format(date);
    }
}
