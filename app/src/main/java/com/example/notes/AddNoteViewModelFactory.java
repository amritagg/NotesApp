package com.example.notes;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.notes.data.NotesDatabase;

public class AddNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final NotesDatabase mDb;
    private final int noteId;

    public AddNoteViewModelFactory(NotesDatabase mDb, int noteId) {
        this.mDb = mDb;
        this.noteId = noteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddNoteViewModel(mDb, noteId);
    }

}
