package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CheckedActivity extends AppCompatActivity implements RecyclerChecklistAdapter.ItemClickListener {

    private EditText et_Input;
    private Button btn_DeleteChecklist;
    private RecyclerView recyclerChecklist;
    private RecyclerChecklistAdapter checklistAdapter;
    private CheckedTextView checkedTextView;
    private NoteDatabase noteDatabase;
    private Checklist checklist;
    private Boolean update = false;

    private Boolean type = true;       // Type: Checklist
    private ArrayList<Rows> contentRows;

    String TAG = "CheckedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked);

        // set Toolbar
        Toolbar tb_CheckedActivity = findViewById(R.id.tb_Checked);
        setSupportActionBar(tb_CheckedActivity);

        // bind Views
        et_Input = findViewById(R.id.et_Input);
        btn_DeleteChecklist = findViewById(R.id.btn_DeleteChecklist);

        // RecyclerView & Divider
        recyclerChecklist = findViewById(R.id.recyclerNote);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CheckedActivity.this);
        recyclerChecklist.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerChecklist.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recyclerChecklist.addItemDecoration(dividerItemDecoration);

        noteDatabase = NoteDatabase.getInstance(CheckedActivity.this);

        // get Extras (if available)
        if ((checklist = (Checklist) getIntent().getParcelableExtra("checklist")) != null) {

            getSupportActionBar().setTitle("Update Checklist");
            update = true;
            contentRows = checklist.getContent();

            displayList();
            //btn_Save.setText("Update");
            btn_DeleteChecklist.setEnabled(true);
            //cb_Type.setChecked();
            //tv_Timestamp.setText(new StringBuilder().append("Last Modified: ").append(getSimpleDate(note.getUpdatedAt())).toString());
            //et_Content.setText(note.getContent());
        } else {
            // create Checklist element
            contentRows = new ArrayList<Rows>();
            //Rows row = new Rows(false, "");
            //contentRows.add(row);

            // create new Checklist
            checklist = new Checklist(contentRows, true);
            displayList();
        }


        // Listener: add checklist entry on Enter
        et_Input.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Rows newRow = new Rows(false, et_Input.getText().toString());
                contentRows.add(newRow);
                checklist.setContent(contentRows);
                checklistAdapter.notifyDataSetChanged();
                et_Input.getText().clear();
                return true;
            }
            return false;
        });

        btn_DeleteChecklist.setOnClickListener(view -> {
            new DeleteTask(CheckedActivity.this, checklist).execute();
        });

    }

    private void saveState() {
        if (update) {
            Log.d(TAG, "saveState: " + "UPDATE");
            Toast.makeText(this, "Checklist updated.", Toast.LENGTH_SHORT).show();
            new CheckedActivity.UpdateTask(CheckedActivity.this, checklist).execute();
        } else {
            Log.d(TAG, "saveState: " + "INSERT");
            Toast.makeText(this, "Checklist saved.", Toast.LENGTH_SHORT).show();
            new CheckedActivity.InsertTask(CheckedActivity.this, checklist).execute();
        }
    }

    public void displayList() {
            checklistAdapter = new RecyclerChecklistAdapter(this, checklist);
            recyclerChecklist.setAdapter(checklistAdapter);
            checklistAdapter.setClickListener(this);
    }

    // Toggle Items (checked / unchecked)
    @Override
    public void onItemClick(View view, int position) {
        ((CheckedTextView) view).toggle();

        if (((CheckedTextView) view).isChecked()) {
            Rows toggleRows = new Rows(true, contentRows.get(position).getContentRow());
            contentRows.set(position, toggleRows);
        } else {
            Rows toggleRows = new Rows(false, contentRows.get(position).getContentRow());
            contentRows.set(position, toggleRows);
        }
    }

    private void setResult(Checklist checklist, int flag) {
        setResult(flag, new Intent().putExtra("checklist", (Parcelable) checklist));
        finish();
    }

    /*
    Worker Threads
    */

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<CheckedActivity> activityReference;
        private Checklist checklist;

        // only retain a weak reference to the activity
        InsertTask(CheckedActivity context, Checklist checklist) {
            activityReference = new WeakReference<>(context);
            this.checklist = checklist;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().noteDatabase.getChecklistDao().insert(checklist);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(checklist, 1);
            }
        }

    }

    private static class UpdateTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<CheckedActivity> activityReference;
        private Checklist checklist;

        // only retain a weak reference to the activity
        UpdateTask(CheckedActivity context, Checklist checklist) {
            activityReference = new WeakReference<>(context);
            this.checklist = checklist;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().noteDatabase.getChecklistDao().update(checklist);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(checklist, 1);
            }
        }
    }

    private static class DeleteTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<CheckedActivity> activityReference;
        private Checklist checklist;

        // only retain a weak reference to the activity
        DeleteTask(CheckedActivity context, Checklist checklist) {
            activityReference = new WeakReference<>(context);
            this.checklist = checklist;
            Log.d("DeleteTask", "DeleteTask: " + checklist.getId());
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            Log.d("doInBackground", "doInBackground: ");
            activityReference.get().noteDatabase.getChecklistDao().delete(checklist);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().onDeleted();
            }
        }
    }

    // Go Back ( no save)
    public void onDeleted() {
        Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        /*        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("EXIT", true);
        startActivity(intent);*/
        finish();
    }

    // Save to DB when pressing Back
    @Override
    public void onBackPressed() {
        if (checklist.getSize() > 0){
            saveState();
        } else {
            super.onBackPressed();
        }
    }

}