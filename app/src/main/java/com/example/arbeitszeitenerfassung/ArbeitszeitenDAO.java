package com.example.arbeitszeitenerfassung;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.security.AppUriAuthenticationPolicy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ArbeitszeitenDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    public ArbeitszeitenDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addArbeitstag(Arbeitszeit arbeitszeiten,boolean Teil2){
        ContentValues values = new ContentValues();
        String Arbeitstag = arbeitszeiten.getDatum().toString();
        boolean Pruefung = checkIfDate(Arbeitstag);
        if(Pruefung == true){
            values.put("datum",arbeitszeiten.getDatum().toString());
        }
        values.put("von",arbeitszeiten.getArbeitszeitstart().toString());


        if(!arbeitszeiten.getArbeitszeitende().toString().isEmpty() && !arbeitszeiten.getTagesarbeitDauer().toString().isEmpty() && Teil2 == false){
            values.put("bis",arbeitszeiten.getArbeitszeitende().toString());
            values.put("dauer",arbeitszeiten.getTagesarbeitDauer().toString());
            values.put("schicht",arbeitszeiten.getArbeitsschicht().toString());

            //database.update("arbeitszeiten_table", values, "datum = ?", new String[] { Arbeitstag });
            //database.delete("arbeitszeiten_table", "datum = ?", new String[] { Arbeitstag });
            database.insert("arbeitszeiten_table",null,values);
        }else if(!arbeitszeiten.getArbeitszeitstart().toString().isEmpty() && !arbeitszeiten.getArbeitszeitende().toString().isEmpty() && Teil2 == true){
            values.put("bis",arbeitszeiten.getArbeitszeitende().toString());
            values.put("dauer",arbeitszeiten.getTagesarbeitDauer().toString());
            values.put("schicht",arbeitszeiten.getArbeitsschicht().toString());

            database.update("arbeitszeiten_table", values, "datum = ?", new String[] { Arbeitstag });
        }
        else if (arbeitszeiten.getArbeitszeitende().toString().isEmpty()) {
            values.put("schicht",arbeitszeiten.getArbeitszeitstart().toString());
            values.put("bis","");
            values.put("dauer","");
            values.put("schicht",arbeitszeiten.getArbeitsschicht().toString());

            database.insert("arbeitszeiten_table",null,values);
        }

    }

    private boolean checkIfDate(String arbeitstag) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(arbeitstag);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public void deleteArbeitstag(String tag){
        database.delete("arbeitszeiten_table", "datum = ?", new String[] { tag });
    }

    public List<Arbeitszeit> getAllTage(){
        List<Arbeitszeit> arbeitstage = new ArrayList<>();
        Cursor cursor = database.query("arbeitszeiten_table", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") String datum = cursor.getString(cursor.getColumnIndex("datum"));
            @SuppressLint("Range") String schicht = cursor.getString(cursor.getColumnIndex("schicht"));
            @SuppressLint("Range") String von = cursor.getString(cursor.getColumnIndex("von"));
            @SuppressLint("Range") String bis = cursor.getString(cursor.getColumnIndex("bis"));
            @SuppressLint("Range") String dauer = cursor.getString(cursor.getColumnIndex("dauer"));
            arbeitstage.add(new Arbeitszeit(datum,schicht,von, bis, dauer));
            cursor.moveToNext();
        }
        cursor.close();
        return arbeitstage;
    }

}
