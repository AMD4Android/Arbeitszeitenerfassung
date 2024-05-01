package com.example.arbeitszeitenerfassung;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDate;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "arbeitszeiten_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "arbeitszeiten_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_date = "datum";
    private static final String COLUMN_arbeitsschicht = "schicht";
    private static final String COLUMN_arbeitsbeginn = "von";
    private static final String COLUMN_arbeitsende = "bis";
    private static final String COLUMN_arbeitsdauer = "dauer";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_date + " TEXT,"
                + COLUMN_arbeitsschicht + " TEXT,"
                + COLUMN_arbeitsbeginn + " TEXT,"
                + COLUMN_arbeitsende + " TEXT,"
                + COLUMN_arbeitsdauer + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
