package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerListAdapter.ItemClickListener {

    private TextView textViewMsg;
    private ProgressBar progressBar;

    private NoteDatabase noteDatabase;
    private List<Note> notes;
    private List<Checklist> checklists;

    //private RecyclerViewAdapter adapter;
    private RecyclerListAdapter adapter;
    private RecyclerView recyclerView;

    public String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Lists
        notes = new ArrayList<>();
        checklists = new ArrayList<>();

        initializeViews();
        displayList();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume: ");

        // refresh RecyclerView
        displayList();
    }

    private void displayList(){
        // initialize database instance
        noteDatabase = NoteDatabase.getInstance(MainActivity.this);
        // fetch list of notes in background thread
        //new RetrieveTask(this).execute();
        new RetrieveTask(this).execute();
        //new RetrieveTask2(this).execute();

        //adapter = new RecyclerListAdapter(this, notes, checklists);
        //recyclerView.setAdapter(adapter);
    }

   /* private static class RetrieveTask extends AsyncTask<Void, Void, List<Note>> {

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }


        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().noteDatabase.getNoteDao().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes != null && notes.size() > 0) {
                activityReference.get().notes = notes;

                // create and set the adapter on RecyclerView instance to display list
                activityReference.get().adapter = new RecyclerViewAdapter(activityReference.get(), notes);
                activityReference.get().recyclerView.setAdapter(activityReference.get().adapter);
                activityReference.get().adapter.setClickListener(activityReference.get());
            }

            // hides empty text view
            activityReference.get().textViewMsg.setVisibility(View.GONE);
            activityReference.get().progressBar.setVisibility(View.GONE);
        }

    }*/


    private static class RetrieveTask extends AsyncTask<Void, Void, List<Note>> {

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }


        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().noteDatabase.getNoteDao().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes != null && notes.size() > 0) {
                activityReference.get().notes = notes;

                // create and set the adapter on RecyclerView instance to display list
                // activityReference.get().adapter = new RecyclerViewAdapter(activityReference.get(), notes);
                // activityReference.get().recyclerView.setAdapter(activityReference.get().adapter);
                // activityReference.get().adapter.setClickListener(activityReference.get());
            }

            // hides empty text view
            // activityReference.get().textViewMsg.setVisibility(View.GONE);
            // activityReference.get().progressBar.setVisibility(View.GONE);
            activityReference.get().progressBar.setProgress(50);

            // call second database fetch Task
            new RetrieveTask2(activityReference.get()).execute();
        }

    }

    private static class RetrieveTask2 extends AsyncTask<Void, Void, List<Checklist>> {

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask2(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Checklist> doInBackground(Void... voids) {
            if (activityReference.get()!=null)
                return activityReference.get().noteDatabase.getChecklistDao().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Checklist> checklists) {

            activityReference.get().progressBar.setProgress(100);

            if (checklists!=null && checklists.size()>0 ){
                activityReference.get().checklists = checklists;
            }

            // create and set the adapter on RecyclerView instance to display list
            activityReference.get().adapter = new RecyclerListAdapter(activityReference.get(), activityReference.get().notes, checklists);
            activityReference.get().recyclerView.setAdapter(activityReference.get().adapter);
            activityReference.get().adapter.setClickListener(activityReference.get());

            // hides empty text view
            activityReference.get().textViewMsg.setVisibility(View.GONE);
            //int count = activityReference.get().notes.size() + checklists.size();
            //activityReference.get().textViewMsg.setText(String.valueOf(count));
            activityReference.get().progressBar.setVisibility(View.GONE);
        }

    }


    private void initializeViews(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewMsg = findViewById(R.id.textViewMsg);

        // Action button to add note
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> addNote());

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Progress Bar / Spinner
        progressBar = findViewById(R.id.progressBar);
        textViewMsg.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Button btn_Checklist = findViewById(R.id.btn_Checklist);
        btn_Checklist.setOnClickListener(view -> {
            addChecklist();
        });
    }

    /*
    Intents
     */
    public void addNote(){
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }

    public void addChecklist(){
        Intent intent = new Intent(this, CheckedActivity.class);
        startActivity(intent);
    }

    public void editNote(int position){
        Intent intent = new Intent(this, AddNoteActivity.class);
        Note note1 = notes.get(position);
        intent.putExtra("note", note1);
        startActivity(intent);
    }

    public void editChecklist(int position){
        Intent intent = new Intent(this, CheckedActivity.class);
        Checklist checklist1 = checklists.get(position);
        intent.putExtra("checklist", checklist1);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
        //editNote(position);
        if (adapter.getItem(position) == "Note"){
            editNote(position);
        } else if (adapter.getItem(position) == "Checklist"){
            editChecklist(position - notes.size());
        }

    }
}

