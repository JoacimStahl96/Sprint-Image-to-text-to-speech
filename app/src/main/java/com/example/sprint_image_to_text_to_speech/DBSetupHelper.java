package com.example.sprint_image_to_text_to_speech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBSetupHelper extends SQLiteOpenHelper {


    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TextFromImages.db";

    private static String TABLE_NAME = "Text_From_Images";
    private static String COLUMN_TEXT = "Saved_texts";


    public DBSetupHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TEXT + " TEXT);";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addTextFromImages(String text) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TEXT, text);

        long id = db.insert(TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.d("DB", "addTextFromImages: That didn't work");
            return false;
        } else {
            Log.d("DB", "addTextFromImages: text inserted into database");
            return true;
        }
    }

    public ArrayList<TextFromImageEntity> getValues(ArrayList<TextFromImageEntity> valuesInDb) {

        SQLiteDatabase db = this.getWritableDatabase();
        // Finns bara en kolumn i nuläget, men kanske byggs ut så känner att String namnet passar lite
        String selectAllColumns = "SELECT " + COLUMN_TEXT + " FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectAllColumns, null);

        while (cursor.moveToNext()) {
            String text = cursor.getString(0);

            TextFromImageEntity textBean = new TextFromImageEntity();

            textBean.setTextFromImage(text);

            valuesInDb.add(textBean);
        }

        cursor.close();
        db.close();
        return valuesInDb;
    }


    // kanske inte lägger till någon delete funktion i appen men bra att ha med där.
    public void deleteAllValuesInTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + TABLE_NAME;
        db.delete(TABLE_NAME, null, null);
        db.execSQL(deleteQuery);
        db.close();
    }
}
