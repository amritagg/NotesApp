package com.example.notes.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "notes")
public class NotesEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String heading;
    private String body;
    @ColumnInfo(name = "updated_at")
    private Date lastUpdate;

    @Ignore
    public NotesEntity(String heading, String body, Date lastUpdate) {
        this.heading = heading;
        this.body = body;
        this.lastUpdate = lastUpdate;
    }

    public NotesEntity(int id, String heading, String body, Date lastUpdate) {
        this.id = id;
        this.heading = heading;
        this.body = body;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
