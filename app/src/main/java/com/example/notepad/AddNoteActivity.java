package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputEditText et_Content;
    private TextView tv_Timestamp;
    private TextView tv_Content;
    private CheckBox cb_Type;
    private NoteDatabase noteDatabase;
    private Note note;
    boolean update = false;

    String TAG = "AddNoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        //recyclerNote = findViewById(R.id.recyclerNote);
        //recyclerNote.setLayoutManager(new LinearLayoutManager(this));


        et_Content = findViewById(R.id.et_content);
        //tv_Content = findViewById(R.id.tv_Content);
        tv_Timestamp = findViewById(R.id.tv_Timestamp);
        cb_Type = findViewById(R.id.cb_Type);
        noteDatabase = NoteDatabase.getInstance(AddNoteActivity.this);

        Button btn_Save = findViewById(R.id.btn_save);
        Button btn_Delete = findViewById(R.id.btn_delete);

        if ( (note = (Note) getIntent().getParcelableExtra("note")) != null ){

            //noteAdapter = new RecyclerNoteAdapter(this, note);
            //noteAdapter.setClickListener(this);
            //recyclerNote.setAdapter(noteAdapter);

            getSupportActionBar().setTitle("Update Note");
            update = true;
            btn_Save.setText("Update");
            btn_Delete.setEnabled(true);
            //cb_Type.setChecked();
            tv_Timestamp.setText(new StringBuilder().append("Last Modified: ").append(getSimpleDate(note.getUpdatedAt())).toString());
            et_Content.setText(note.getContent());
        }

        // create empty List
        else {
            //noteAdapter = new RecyclerNoteAdapter(this, new Note(null, false));
            //noteAdapter.setClickListener(this);
            //recyclerNote.setAdapter(noteAdapter);
        }

        // Save note
        btn_Save.setOnClickListener(view -> {

            if (update){
                // Update Note
                note.setContent(et_Content.getText().toString());
                note.setType(cb_Type.isChecked());

                new UpdateTask(AddNoteActivity.this, note).execute();

            } else {
                // Insert Node
                note = new Note(et_Content.getText().toString(), cb_Type.isChecked());
                new InsertTask(AddNoteActivity.this, note).execute();
            }
        });

        // Delete note
        btn_Delete.setOnClickListener(view -> {
            new DeleteTask(AddNoteActivity.this, note).execute();
        });
    }

    private void setResult(Note note, int flag){
        setResult(flag,new Intent().putExtra("note", (Parcelable) note));
        finish();
    }

    /*
    Worker Threads
     */
    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<AddNoteActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        InsertTask(AddNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().noteDatabase.getNoteDao().insert(note);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool){
                activityReference.get().setResult(note,1);
            }
        }

    }

    private static class UpdateTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<AddNoteActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        UpdateTask(AddNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().noteDatabase.getNoteDao().update(note);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(note, 1);
            }
        }
    }

    private static class DeleteTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<AddNoteActivity> activityReference;
        private Note note;

        // only retain a weak reference to the activity
        DeleteTask(AddNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().noteDatabase.getNoteDao().delete(note);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(note, 1);
            }
        }
    }

    // get Note type (checkbox)
    public String checkCheckbox(){
        if (cb_Type.isChecked()){
            return "checklist";
        } else {
            return "note";
        }
    }

    // convert Date to simpler format
    public String getSimpleDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.TimeDate));
        return dateFormatter.format(date);
    }

    // RecyclerView onClick Listener
//    @Override
//    public void onItemClick(View view, int position) {
//        CheckedTextView tv_Content = findViewById(R.id.tv_NoteContent);
//        tv_Content.setChecked(true);
//        //et_Content.setChecked(true);
//        //Toast.makeText(this, "You clicked " + noteAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
//    }

}

