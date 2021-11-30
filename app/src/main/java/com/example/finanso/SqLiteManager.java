package com.example.finanso;

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
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "Historia";
    private static final String KOL_ID = "Id";
    private static final String KOL_KWOTA = "Kwota";
    private static final String KOL_OPIS = "Opis";
    private static final String KOL_SZCZEGOL_OPIS = "Szczegol_opis";
    private static final String KOL_DATA = "Data";
    private static final String KOL_KATEGORIA_ID = "Kategoria";

    private static final String TABLE_NAME_2 = "Kategorie";
    private static final String KOL2_ID = "Id2";
    private static final String KOL2_KOLOR = "Kolor";
    private static final String KOL2_NAZWA = "Nazwa";
    private static final String KOL2_OPIS = "Opis";


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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }

    void addWpis(String kwota, String opis, String szczegol_opis, String data, int kategoria_id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv. put(KOL_KWOTA,kwota);
        cv. put(KOL_OPIS,opis);
        cv. put(KOL_SZCZEGOL_OPIS,szczegol_opis);
        cv. put(KOL_DATA,data);
        cv. put(KOL_KATEGORIA_ID,kategoria_id);
       long result= db.insert(TABLE_NAME,null,cv);
       if(result==-1){
           Toast.makeText(context,"BŁĄD",Toast.LENGTH_SHORT).show();
       }
       else{
            Toast.makeText(context,"POPRAWNIE DODANO!",Toast.LENGTH_SHORT).show();
        }

    }
    void addKategoria(String kolor, String nazwa2, String opis2){
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

    Cursor readAllHistoria(){
        String query ="SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = null;
        if(db!=null){
            cursor=db.rawQuery(query,null);

        }
        return cursor;
    }
}