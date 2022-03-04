package com.example.finanso.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SqLiteManager extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Finanso.db";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_NAME = "Historia";
    private static final String KOL_ID = "IdHistoria";
    private static final String KOL_KWOTA = "Kwota";
    private static final String KOL_OPIS = "Opis";
    private static final String KOL_SZCZEGOL_OPIS = "Szczegol_opis";
    private static final String KOL_DATA = "Data";
    private static final String KOL_KATEGORIA_ID = "Kategoria";

    private static final String TABLE_NAME_2 = "Kategorie";
    private static final String KOL2_ID = "IdKategoria";
    private static final String KOL2_KOLOR = "Kolor";
    private static final String KOL2_NAZWA = "Nazwa";
    private static final String KOL2_OPIS = "Opis";

    private static final String TABLE_NAME_3 = "Paragony";
    private static final String KOL3_ID = "IdParagony";
    private static final String KOL3_OPIS = "Opis";
    private static final String KOL3_DATA = "DataGwarancni";
    private static final String KOL3_GWARANCJA = "CzyGwarancja";
    private static final String KOL3_ZDJECIE = "Zdjecie";


    public SqLiteManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE " + TABLE_NAME +" ("+ KOL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +KOL_KWOTA + " TEXT, " +KOL_OPIS +" TEXT, " +KOL_SZCZEGOL_OPIS +" TEXT, " +KOL_DATA+ " TEXT, " +KOL_KATEGORIA_ID+" INTEGER);";
        db.execSQL(query);
        String query2= "CREATE TABLE " + TABLE_NAME_2 +" ("+ KOL2_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +KOL2_KOLOR + " TEXT, " +KOL2_NAZWA +" TEXT, " +KOL2_OPIS +" TEXT);";
        db.execSQL(query2);
        String query3= "CREATE TABLE " + TABLE_NAME_3 +" ("+ KOL3_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +KOL3_OPIS + " TEXT, " +KOL3_GWARANCJA +" TEXT, " +KOL3_DATA +" TEXT, "+KOL3_ZDJECIE +" TEXT);";
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
    }

    public Cursor readAllHistoryWithKategorie() {
        String query = "SELECT * FROM " + TABLE_NAME + " INNER JOIN "
                + TABLE_NAME_2 + " ON " + TABLE_NAME + "." + KOL_KATEGORIA_ID + " = "
                + TABLE_NAME_2 + "." + KOL2_ID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }
    public Cursor readAllKategorie() {
        String query = "SELECT * FROM " + TABLE_NAME_2;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }
    public Cursor readAllParagony() {
        String query = "SELECT * FROM " + TABLE_NAME_3;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }
    public void addWpis(String kwota, String opis, String szczegol_opis, String data, String kategoria_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KOL_KWOTA, kwota);
        cv.put(KOL_OPIS, opis);
        cv.put(KOL_SZCZEGOL_OPIS, szczegol_opis);
        cv.put(KOL_DATA, data);
        cv.put(KOL_KATEGORIA_ID, kategoria_id);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "BŁĄD", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "POPRAWNIE DODANO!", Toast.LENGTH_SHORT).show();
        }
    }
        public void addParagon(String opis, String gwarancja, String data,String zdjecie){
            SQLiteDatabase db3=this.getWritableDatabase();
            ContentValues cv3= new ContentValues();
            cv3. put(KOL3_OPIS,opis);
            cv3. put(KOL3_GWARANCJA,gwarancja);
            cv3. put(KOL3_DATA,data);
            cv3. put(KOL3_ZDJECIE,zdjecie);
            long result= db3.insert(TABLE_NAME_3,null,cv3);
            if(result==-1){
                Toast.makeText(context,"BŁĄD",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"POPRAWNIE DODANO!",Toast.LENGTH_SHORT).show();
            }

    }
    public void addKategoria(String kolor, String nazwa2, String opis2){
        SQLiteDatabase db2=this.getWritableDatabase();
        ContentValues cv2= new ContentValues();
        cv2. put(KOL2_KOLOR,kolor);
        cv2. put(KOL2_NAZWA,nazwa2);
        cv2. put(KOL2_OPIS,opis2);
        long result= db2.insert(TABLE_NAME_2,null,cv2);
        if(result==-1){
            Toast.makeText(context,"BŁĄD",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"POPRAWNIE DODANO!",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean deleteOneRowFromList(String row_id){
        this.close();
        SQLiteDatabase db =this.getWritableDatabase();

        //long result =  db.delete(TABLE_NAME,KOL_ID +"=?", new String[]{String.valueOf(row_id)});
        long result =  db.delete(TABLE_NAME,KOL_ID +"=" + row_id,null);
        if(result ==-1){
            Toast.makeText(context,"BŁĄD",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"POPRAWNIE USUNIĘTO!",Toast.LENGTH_SHORT).show();
        }
        return result>0;
    }
    public boolean deleteOneRowFromCategory(String row_id){
        this.close();
        SQLiteDatabase db =this.getWritableDatabase();

        //long result =  db.delete(TABLE_NAME,KOL_ID +"=?", new String[]{String.valueOf(row_id)});
        long result =  db.delete(TABLE_NAME_2,KOL2_ID +"=" + row_id,null);
        if(result ==-1){
            Toast.makeText(context,"BŁĄD",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"POPRAWNIE USUNIĘTO!",Toast.LENGTH_SHORT).show();
        }
        return result>0;
    }
    public void updateListData(String row_id, String kwota, String opis, String szczegol_opis, String data, String kategoria_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KOL_KWOTA, kwota);
        cv.put(KOL_OPIS, opis);
        cv.put(KOL_DATA, data);
        cv.put(KOL_SZCZEGOL_OPIS, szczegol_opis);
        cv.put(KOL_KATEGORIA_ID, kategoria_id);

        long result = db.update(TABLE_NAME, cv, KOL_ID + "=" + row_id, null);
        if (result == -1) {
            Toast.makeText(context, "Błąd!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "POPRAWNIE ZEDYTOWANO!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCategoryData(String row_id, String kolor, String nazwa2, String opis2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KOL2_KOLOR, kolor);
        cv.put(KOL2_NAZWA, nazwa2);
        cv.put(KOL2_OPIS, opis2);

        long result = db.update(TABLE_NAME_2, cv, KOL2_ID + "=" + row_id, null);
        if (result == -1) {
            Toast.makeText(context, "Błąd!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "POPRAWNIE ZEDYTOWANO!", Toast.LENGTH_SHORT).show();
        }
    }
}