 package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.notes.data.NotesDatabase;
import com.example.notes.data.NotesEntity;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    public static final String INSTANCE_NOTE_ID = "instance_note_id";
    public static final String EXTRA_NOTE_ID = "extraTaskId";
    public static final int DEFAULT_NOTE_ID = -1;

    EditText title;
    EditText body;
    TextView length;
    Button copy;
    NotesDatabase mDb;
    private int mNotesId = DEFAULT_NOTE_ID;
    Intent intent;
    String defaultHead = "";
    String defaultBody = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mDb = NotesDatabase.getInstance(getApplicationContext());

        init();

        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_NOTE_ID)){
            mNotesId = savedInstanceState.getInt(INSTANCE_NOTE_ID, DEFAULT_NOTE_ID);
        }

        body.addTextChangedListener(new TextWatcher() {

            @SuppressLint("SetTextI18n")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length.setText("0 words \t 0 letters");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int size = countWords(body.getText().toString());
                length.setText(size + " words \t\t " + s.length() + " letters");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("EditText", body.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Text Copied..!!", Toast.LENGTH_SHORT).show();
        });

        intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_NOTE_ID)){
            if(mNotesId == DEFAULT_NOTE_ID){
                mNotesId = intent.getIntExtra(EXTRA_NOTE_ID, DEFAULT_NOTE_ID);

                AddNoteViewModelFactory factory = new AddNoteViewModelFactory(mDb, mNotesId);
                AddNoteViewModel model = new ViewModelProvider(this, factory).get(AddNoteViewModel.class);
                model.getTask().observe(this, this::populateUi);

            }
        }

    }

    private void init(){
        title = findViewById(R.id.heading_edit);
        body = findViewById(R.id.body_edit);
        length = findViewById(R.id.text_length);
        copy = findViewById(R.id.copy_content);
    }

    private int countWords(String s){
        int l = 0;
        s = s.trim();
        if(s.length() == 0) return 0;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == ' ') l++;
            if(s.charAt(i) == '\n') l++;
        }
        return l+1;
    }

    private void populateUi(NotesEntity entity){
        if(entity == null) return;
        title.setText(entity.getHeading());
        body.setText(entity.getBody());
        defaultBody = entity.getBody();
        defaultHead = entity.getHeading();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INSTANCE_NOTE_ID, mNotesId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(intent != null && intent.hasExtra(EXTRA_NOTE_ID)){
            getMenuInflater().inflate(R.menu.add_menu, menu);
        }else {
            getMenuInflater().inflate(R.menu.add_menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            saveNote();
            finish();
            return true;
        }else if(menuItem.getItemId() == R.id.check){
            saveNote();
            finish();
            return true;
        }else if(menuItem.getItemId() == R.id.share){
            shareText();
            return true;
        }else if(menuItem.getItemId() == R.id.delete){
            delete();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void saveNote(){
        String title_string = title.getText().toString().trim();
        String body_string = body.getText().toString().trim();
        Date date = new Date();

        if(!title_string.equals(defaultHead) || !body_string.equals(defaultBody)) {
            final NotesEntity notesEntity = new NotesEntity(title_string, body_string, date);
            NoteExecutor.getInstance().diskIO().execute(() -> {
                if (title_string.isEmpty() && body_string.isEmpty()) {
                    notesEntity.setId(mNotesId);
                    mDb.notesDao().deleteNote(notesEntity);
                } else {
                    if (mNotesId == DEFAULT_NOTE_ID) {
                        mDb.notesDao().insertNote(notesEntity);
                    } else {
                        notesEntity.setId(mNotesId);
                        mDb.notesDao().updateNote(notesEntity);
                    }
                }
            });
        }
    }

    private void shareText() {
        String mimeType = "text/plain";
        String title_text = "Learn to share text";
        String text = title.getText() + "\n" + body.getText();

        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title_text)
                .setType(mimeType)
                .setText(text)
                .startChooser();
    }

    private void delete(){
        final NotesEntity notesEntity = new NotesEntity(null, null, null);
        NoteExecutor.getInstance().diskIO().execute(() -> {
            notesEntity.setId(mNotesId);
            mDb.notesDao().deleteNote(notesEntity);
        });
        finish();
    }
    @Override
    public void onBackPressed() {
        saveNote();
        finish();
        super.onBackPressed();
    }

}