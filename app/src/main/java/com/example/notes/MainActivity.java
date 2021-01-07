package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener{

    RecyclerView recyclerView;
    NoteAdapter mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoteAdapter = new NoteAdapter(this, this);
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mNoteAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getNotes().observe(this, notesEntities -> mNoteAdapter.setTasks(notesEntities));

    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra(AddNoteActivity.EXTRA_NOTE_ID, itemId);
        startActivity(intent);
    }
}
