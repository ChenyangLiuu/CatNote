package com.example.catnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    private static final String[] columns = {
            com.example.catnote.NoteDatabase.ID,
            com.example.catnote.NoteDatabase.CONTENT,
            com.example.catnote.NoteDatabase.TIME,
            com.example.catnote.NoteDatabase.MODE
    };

    public CRUD(Context context){
        dbHandler = new com.example.catnote.NoteDatabase(context);
    }

    public void open(){
        db = dbHandler.getWritableDatabase();
    }

    public void close(){
        dbHandler.close();
    }

    public com.example.catnote.Note addNote(com.example.catnote.Note note){
        //add a note object to database
        ContentValues contentValues = new ContentValues();
        contentValues.put(com.example.catnote.NoteDatabase.CONTENT, note.getContent());
        contentValues.put(com.example.catnote.NoteDatabase.TIME, note.getTime());
        contentValues.put(com.example.catnote.NoteDatabase.MODE, note.getTag());
        long insertId = db.insert(com.example.catnote.NoteDatabase.TABLE_NAME, null, contentValues);
        note.setId(insertId);
        return note;
    }

    public com.example.catnote.Note getNote(long id){
        //get a note from database using cursor index
        Cursor cursor = db.query(com.example.catnote.NoteDatabase.TABLE_NAME,columns, com.example.catnote.NoteDatabase.ID + "=?",
                new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null) cursor.moveToFirst();
        com.example.catnote.Note e = new com.example.catnote.Note(cursor.getString(1),cursor.getString(2), cursor.getInt(3));
        return e;
    }

    public List<com.example.catnote.Note> getAllNotes(){
        Cursor cursor = db.query(com.example.catnote.NoteDatabase.TABLE_NAME,columns,null,null,null, null, null);

        List<com.example.catnote.Note> notes = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                com.example.catnote.Note note = new com.example.catnote.Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(com.example.catnote.NoteDatabase.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndex(com.example.catnote.NoteDatabase.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(com.example.catnote.NoteDatabase.TIME)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(com.example.catnote.NoteDatabase.MODE)));
                notes.add(note);
            }
        }
        return notes;
    }

    public int updateNote(com.example.catnote.Note note) {
        //update the info of an existing note
        ContentValues values = new ContentValues();
        values.put(com.example.catnote.NoteDatabase.CONTENT, note.getContent());
        values.put(com.example.catnote.NoteDatabase.TIME, note.getTime());
        values.put(com.example.catnote.NoteDatabase.MODE, note.getTag());
        // updating row
        return db.update(com.example.catnote.NoteDatabase.TABLE_NAME, values,
                com.example.catnote.NoteDatabase.ID + "=?",new String[] { String.valueOf(note.getId())});
    }

    public void removeNote(com.example.catnote.Note note) {
        //remove a note according to ID value
        db.delete(com.example.catnote.NoteDatabase.TABLE_NAME, com.example.catnote.NoteDatabase.ID + "=" + note.getId(), null);
    }

}
