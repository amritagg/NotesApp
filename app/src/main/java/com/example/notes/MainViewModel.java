package com.example.notes;

import com.example.notes.data.NotesDatabase;
import com.example.notes.data.NotesEntity;
import java.util.List;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private final LiveData<List<NotesEntity>> notes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        NotesDatabase database = NotesDatabase.getInstance(this.getApplication());
        notes = database.notesDao().loadAllNotes();
    }

    public LiveData<List<NotesEntity>> getNotes() {
        return notes;
    }
}
