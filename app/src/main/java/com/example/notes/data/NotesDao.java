package com.example.notes.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes ORDER BY updated_at")
    LiveData<List<NotesEntity>> loadAllNotes();

    @Insert
    void insertNote(NotesEntity notesEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(NotesEntity notesEntity);

    @Delete
    void deleteNote(NotesEntity notesEntity);

    @Query("SELECT * FROM notes WHERE id = :id")
    LiveData<NotesEntity> listNoteById(int id);

}
