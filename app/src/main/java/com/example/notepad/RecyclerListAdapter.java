package com.example.notepad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_NOTE = 0;
    final int VIEW_TYPE_CHECKLIST = 1;

    Context context;
    List<Note> notes;
    List<Checklist> checklists;
    View itemView;
    ItemClickListener mClickListener;

    LayoutInflater inflater;

    String TAG = "RecyclerListAdapter";

    public RecyclerListAdapter(Context context, List<Note> notes, List<Checklist> checklists){
        this.context = context;
        this.notes = notes;
        this.checklists = checklists;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = inflater.inflate(R.layout.recyclerview_row, parent, false);

        if(viewType == VIEW_TYPE_NOTE){
            if (!notes.isEmpty()) {
                return new NoteViewHolder(itemView);
            }
        }

        if(viewType == VIEW_TYPE_CHECKLIST){
            if(!checklists.isEmpty()) {
                return new ChecklistViewHolder(itemView);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        if(viewHolder instanceof NoteViewHolder){
            ((NoteViewHolder) viewHolder).populate(notes.get(position));
        }

        if(viewHolder instanceof ChecklistViewHolder){
            ((ChecklistViewHolder) viewHolder).populate(checklists.get(position - notes.size()));
        }
    }

    @Override
    public int getItemCount(){
        return notes.size() + checklists.size();
    }

    @Override
    public int getItemViewType(int position){
        if(position < notes.size()){
            return VIEW_TYPE_NOTE;
        }

        if(position - notes.size() < checklists.size()){
            return VIEW_TYPE_CHECKLIST;
        }

        return -1;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckedTextView ctv_Content;
        TextView tv_Content;
        TextView tv_timeStamp;

        public NoteViewHolder(View itemView){
            super(itemView);
            ctv_Content = (CheckedTextView) itemView.findViewById(R.id.ctv_Content);
            tv_Content = (TextView) itemView.findViewById(R.id.tvContent);
            tv_timeStamp = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void populate(Note notes){
                ctv_Content.setVisibility(View.GONE);
                tv_Content.setText(notes.getContent());
                tv_timeStamp.setText(getSimpleDate(notes.getUpdatedAt()));
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + view);
            if (mClickListener != null) mClickListener.onItemClick(view, getAbsoluteAdapterPosition());
        }
    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckedTextView ctv_Content;
        TextView tv_Content;
        TextView tv_timeStamp;

        public ChecklistViewHolder(View itemView){
            super(itemView);

            ctv_Content = (CheckedTextView) itemView.findViewById(R.id.ctv_Content);
            tv_Content = (TextView) itemView.findViewById(R.id.tvContent);
            tv_timeStamp = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void populate(Checklist checklists){
            String content = checklists.getContent().get(0).getContentRow();
            ctv_Content.setText(content);
            ctv_Content.setChecked(checklists.getContent().get(0).isChecked());
            tv_Content.setVisibility(View.GONE);
            tv_timeStamp.setText(getSimpleDate(checklists.getUpdatedAt()));
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAbsoluteAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    String getItem(int id) {
        if(id < notes.size()){
            //return String.valueOf(notes.get(id));
            return "Note";
        }

        if(id - notes.size() < checklists.size()){
            //return String.valueOf(checklists.get(id));
            return "Checklist";
        }

        return "null";
        //return String.valueOf(mData.get(id));
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
