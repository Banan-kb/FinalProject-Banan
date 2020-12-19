package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Mitch on 2016-05-13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="Users.db";
    public static final String TABLE_NAME = "users_info";
    public static final String COL1 = "USERID";
    public static final String COL2 = "FIRST_NAME";
    public static final String COL3 = "LAST_NAME";
    public static final String COL4 = "PHONE_NUMBER";
    public static final String COL5 = "EMAIL_ADDRESS";

    /* Constructor */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /* Code runs automatically when the dB is created */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (" + COL1 + " INTEGER PRIMARY KEY, "
                + COL2 + "  TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, "
                + COL5 + " TEXT )";

        db.execSQL(createTable);
    }

    /* Every time the dB is updated (or upgraded) */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /* Basic function to add data. REMEMBER: The fields
       here, must be in accordance with those in
       the onCreate method above.
    */
    public boolean addUser(int id, String fName, String lName, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1, id);
        contentValues.put(COL2, fName);
        contentValues.put(COL3, lName);
        contentValues.put(COL4, phone);
        contentValues.put(COL5, email);


        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data are inserted incorrectly, it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Integer deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();


        return db.delete(TABLE_NAME, "USERID = ?", new String[]{String.valueOf(id)});
    }

    public Integer updateUser(int id, String col, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(col, data);



        return db.update(TABLE_NAME, contentValues, "USERID = ?", new String[]{String.valueOf(id)} );
    }
    /* Returns only one result */
    public Cursor structuredQuery(int id) {
        SQLiteDatabase db = this.getReadableDatabase(); // No need to write

        Cursor cursor = db.query(TABLE_NAME, new String[]{COL1, COL2, COL3, COL4, COL5},
                COL1 + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }


    public Cursor getSpecificResult(int id) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor data = db.query(TABLE_NAME, new String[]{COL1,
                COL2, COL3, COL4, COL5}, COL1 + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (data != null)
            data.moveToFirst();

        return data;
    }



    // Return everything inside the dB
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
