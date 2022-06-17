package com.example.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerChecklistAdapter extends RecyclerView.Adapter<RecyclerChecklistAdapter.ViewHolder> {

    String TAG = "RecyclerNoteAdapter";

    private Checklist mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerChecklistAdapter(Context context, Checklist data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerchecklist_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //String updatedAt = getSimpleDate(mData.getUpdatedAt());

        ArrayList<Rows> content = mData.getContent();
        holder.contentTextView.setChecked(content.get(position).checked);
        holder.contentTextView.setText(content.get(position).contentRow);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.getSize();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckedTextView contentTextView;

        ViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.checkedTextView);
            contentTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAbsoluteAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return String.valueOf(mData.getId());
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
