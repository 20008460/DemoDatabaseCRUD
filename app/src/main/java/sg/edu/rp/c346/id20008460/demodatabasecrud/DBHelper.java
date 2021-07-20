package sg.edu.rp.c346.id20008460.demodatabasecrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simplenotes.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE_CONTENT = "note_content";

    public DBHelper(Context context) { // dont need user to pass in as variable set as global
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createNoteTableSql = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_CONTENT + " TEXT ) ";
        db.execSQL(createNoteTableSql);

        for (int i = 0; i< 4; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE_CONTENT, "Data number " + i);
            db.insert(TABLE_NOTE, null, values);
        }
        Log.i("info", "dummy records inserted");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // when there is a change in database format and NOT add in/update new data put inside
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE); // every data in it will be gone
        db.execSQL("ALTER TABLE " + TABLE_NOTE + " ADD COLUMN  module_name TEXT ");

        //onCreate(db);
    }

    public long insertNote(String noteContent) { // method where new data will be added
        // long is the longer version of INT
        // ID not inserted / cannot inserted due to AUTO INCREMENT
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, noteContent);
        long result = db.insert(TABLE_NOTE, null, values);

        if (result == -1) {
            Log.d("DBHelper", "Insert failed");
        }
        db.close();
        Log.d("SQL Insert","ID:"+ result); //id returned, shouldn’t be -1
        return result;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();

        String selectQuery = "SELECT " + COLUMN_ID + ","
                + COLUMN_NOTE_CONTENT + " FROM " + TABLE_NOTE;
        // do not use SELECT *

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public int updateNote(Note data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, data.getNoteContent());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_NOTE, values, condition, args);

        if (result < 1) {
            Log.d("DBHelper" , "Update failed");
        }
        db.close();
        return result;
    }

    public int deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_NOTE, condition, args);
        db.close();
        return result;
    }

    public ArrayList<Note> getAllNotes(String keyword) {
        ArrayList<Note> notes = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_NOTE_CONTENT};
        String condition = COLUMN_NOTE_CONTENT + " Like ?";
        String[] args = { "%" +  keyword + "%"};
        Cursor cursor = db.query(TABLE_NOTE, columns, condition, args,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }


}
