package com.example.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.notes.data.NotesDatabase;
import com.example.notes.data.NotesEntity;

public class AddNoteViewModel extends ViewModel {

    private final LiveData<NotesEntity> mTask;

    public AddNoteViewModel(NotesDatabase mDb, int noteId) {
        mTask = mDb.notesDao().listNoteById(noteId);
    }

    public LiveData<NotesEntity> getTask(){
        return mTask;
    }


}
