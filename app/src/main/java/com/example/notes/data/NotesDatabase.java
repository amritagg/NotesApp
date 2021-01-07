package com.example.notes.data;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {NotesEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String LOG = NotesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "noteslist.db";
    private static NotesDatabase sNotesDatabase;

    public static NotesDatabase getInstance(Context context){
        if(sNotesDatabase == null){
            synchronized (LOCK){
                Log.e(LOG, "Creating a new Database");
                sNotesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        NotesDatabase.class,
                        NotesDatabase.DATABASE_NAME)
                        .build();
            }
        }

        Log.e(LOG, "Getting the database Instance");
        return sNotesDatabase;
    }

    public abstract NotesDao notesDao();

}
