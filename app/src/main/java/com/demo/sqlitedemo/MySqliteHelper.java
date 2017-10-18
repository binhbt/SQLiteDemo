package com.demo.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by t430 on 10/18/2017.
 */

public class MySqliteHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "personexample.db";
    public static final int DATABASE_VERSION = 1;

    public static final String PERSON_TABLE_NAME = "person";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_NAME = "name";
    public static final String PERSON_COLUMN_GENDER = "gender";
    public static final String PERSON_COLUMN_AGE = "age";

    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //CREATE TABLE person (_id INTEGER PRIMARY KEY, name TEXT, gender TEXT, age INTEGER)
        sqLiteDatabase.execSQL(
                "CREATE TABLE " +PERSON_TABLE_NAME+
                        "("+PERSON_COLUMN_ID + " INTEGER PRIMARY KEY, "+
                        PERSON_COLUMN_NAME + " TEXT, "+
                        PERSON_COLUMN_GENDER+ " TEXT, "+
                        PERSON_COLUMN_AGE + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+PERSON_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertPerson(String name, String gender, int age){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PERSON_COLUMN_NAME, name);
            contentValues.put(PERSON_COLUMN_GENDER, gender);
            contentValues.put(PERSON_COLUMN_AGE, age);
            db.insert(PERSON_TABLE_NAME, null, contentValues);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public int numberOfRows(){
        SQLiteDatabase db = getWritableDatabase();
        int numrows = (int)DatabaseUtils.queryNumEntries(db, PERSON_TABLE_NAME);
        return numrows;
    }
    public boolean updatePerson(int id, String name, String gender, int age){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PERSON_COLUMN_NAME, name);
            contentValues.put(PERSON_COLUMN_GENDER, gender);
            contentValues.put(PERSON_COLUMN_AGE, age);
            db.update(PERSON_TABLE_NAME, contentValues, PERSON_COLUMN_ID+" = ?", new String[]{id+""});
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public int deletePerson(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSON_TABLE_NAME, PERSON_COLUMN_ID+" = ?", new String[]{id+""});
    }

    public Cursor getAllPerson(){
        //SELECT * FROM person
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+PERSON_TABLE_NAME, null);
        return  cursor;
    }
    public Cursor getPerson(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PERSON_TABLE_NAME+" WHERE "+PERSON_COLUMN_ID+" = ?", new String[]{id+""});
        return cursor;
    }
}
