package com.example.finanso.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SqLiteManager extends SQLiteOpenHelper {

    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

    private static final String DATABASE_NAME = "Finanso.db";
    private static final int DATABASE_VERSION = 12;

    private static final String TABLE_NAME = "Historia";
    private static final String KOL_ID = "IdHistoria";
    private static final String KOL_KWOTA = "Kwota";
    private static final String KOL_OPIS = "Opis";
    private static final String KOL_SZCZEGOL_OPIS = "Szczegol_opis";
    private static final String KOL_DATA = "Data";
    private static final String KOL_KATEGORIA_ID = "Kategoria";
    private static final String KOL_CZYWPLYW = "CzyWpływ";

    private static final String TABLE_NAME_2 = "Kategorie";
    private static final String KOL2_ID = "IdKategoria";
    private static final String KOL2_KOLOR = "Kolor";
    private static final String KOL2_NAZWA = "Nazwa";
    private static final String KOL2_OPIS = "Opis";

    private static final String TABLE_NAME_3 = "Paragony";
    private static final String KOL3_ID = "IdParagony";
    private static final String KOL3_OPIS = "Opis";
    private static final String KOL3_DATA = "DataGwarancji";
    private static final String KOL3_GWARANCJA = "CzyGwarancja";
    private static final String KOL3_ZDJECIE = "Zdjecie";


    public SqLiteManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE " + TABLE_NAME +" ("+ KOL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +KOL_KWOTA + " TEXT, " +KOL_OPIS +" TEXT, " +KOL_SZCZEGOL_OPIS +" TEXT, " +KOL_DATA+ " TEXT, " +KOL_KATEGORIA_ID+" INTEGER, " +KOL_CZYWPLYW+" TEXT);";
        db.execSQL(query);
        String query2= "CREATE TABLE " + TABLE_NAME_2 +" ("+ KOL2_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +KOL2_KOLOR + " TEXT, " +KOL2_NAZWA +" TEXT, " +KOL2_OPIS +" TEXT);";
        db.execSQL(query2);
        String query3= "CREATE TABLE " + TABLE_NAME_3 +" ("+ KOL3_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +KOL3_OPIS + " TEXT, " +KOL3_GWARANCJA +" TEXT, " +KOL3_DATA +" TEXT, "+KOL3_ZDJECIE +" TEXT);";
        db.execSQL(query3);
        String query4= "INSERT INTO "+TABLE_NAME_2+" ( "+KOL2_KOLOR+" , "+KOL2_NAZWA+" , "+KOL3_OPIS+") VALUES ('#585858','Brak','Brak kategorii');";
        db.execSQL(query4);
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
                + TABLE_NAME_2 + "." + KOL2_ID + " ORDER BY "+KOL_DATA+" DESC;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }
    public Cursor readAllKategorie() {
        String query = "SELECT * FROM " + TABLE_NAME_2;
        //SELECT '0' AS "+ KOL2_ID+" , '#585858' AS "+KOL2_KOLOR+" , 'Brak kategorii' AS "+KOL2_NAZWA+" , 'Brak kategorii' AS "+KOL2_OPIS+" UNION
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
    public void addWpis(String kwota, String opis, String szczegol_opis, String data, String kategoria_id,String czyWpis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KOL_KWOTA, kwota);
        cv.put(KOL_OPIS, opis);
        cv.put(KOL_SZCZEGOL_OPIS, szczegol_opis);
        cv.put(KOL_DATA, data);
        cv.put(KOL_KATEGORIA_ID, kategoria_id);
        cv.put(KOL_CZYWPLYW, czyWpis);
        long result = db.insert(TABLE_NAME, null, cv);
    }
        public void addParagon(String opis, String gwarancja, String data,String zdjecie){
            SQLiteDatabase db3=this.getWritableDatabase();
            ContentValues cv3= new ContentValues();
            cv3. put(KOL3_OPIS,opis);
            cv3. put(KOL3_GWARANCJA,gwarancja);
            cv3. put(KOL3_DATA,data);
            cv3. put(KOL3_ZDJECIE,zdjecie);
            long result= db3.insert(TABLE_NAME_3,null,cv3);

    }
    public void addKategoria(String kolor, String nazwa2, String opis2){
        SQLiteDatabase db2=this.getWritableDatabase();
        ContentValues cv2= new ContentValues();
        cv2. put(KOL2_KOLOR,kolor);
        cv2. put(KOL2_NAZWA,nazwa2);
        cv2. put(KOL2_OPIS,opis2);
        long result= db2.insert(TABLE_NAME_2,null,cv2);

    }

    public boolean deleteOneRowFromList(String row_id){
        this.close();
        SQLiteDatabase db =this.getWritableDatabase();
        long result =  db.delete(TABLE_NAME,KOL_ID +"=" + row_id,null);
        return result>0;
    }
    public boolean deleteOneRowFromCategory(String row_id){

        //dodać update na historii gdzie kategoria= usuwanej. ustawiam na brak

        this.close();
        SQLiteDatabase db =this.getWritableDatabase();

        long result =  db.delete(TABLE_NAME_2,KOL2_ID +"=" + row_id,null);

        updateDeletedKategory(row_id);

        return result>0;
    }
    public void updateDeletedKategory(String row_id) {
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KOL_KATEGORIA_ID, "1");

        long result = db.update(TABLE_NAME, cv, KOL_KATEGORIA_ID + "=" + row_id, null);

    }

    public boolean deleteOneRowFromReceipt(String row_id){
        this.close();
        SQLiteDatabase db =this.getWritableDatabase();

        long result =  db.delete(TABLE_NAME_3,KOL3_ID +"=" + row_id,null);

        return result>0;
    }
    public void updateListData(String row_id, String kwota, String opis, String szczegol_opis, String data, String kategoria_id, String czyWplyw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KOL_KWOTA, kwota);
        cv.put(KOL_OPIS, opis);
        cv.put(KOL_DATA, data);
        cv.put(KOL_SZCZEGOL_OPIS, szczegol_opis);
        cv.put(KOL_KATEGORIA_ID, kategoria_id);
        cv.put(KOL_CZYWPLYW, czyWplyw);

        long result = db.update(TABLE_NAME, cv, KOL_ID + "=" + row_id, null);

    }

    public void updateCategoryData(String row_id, String kolor, String nazwa2, String opis2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KOL2_KOLOR, kolor);
        cv.put(KOL2_NAZWA, nazwa2);
        cv.put(KOL2_OPIS, opis2);

        long result = db.update(TABLE_NAME_2, cv, KOL2_ID + "=" + row_id, null);

    }
    public void updateReceiptData(String row_id, String opis, String gwarancja, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KOL3_OPIS, opis);
        cv.put(KOL3_GWARANCJA, gwarancja);
        cv.put(KOL3_DATA, data);

        long result = db.update(TABLE_NAME_3, cv, KOL3_ID + "=" + row_id, null);
    }
/*    public void addKategoriaZero() {

        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues cv2 = new ContentValues();
        cv2.put(KOL2_KOLOR, "#585858");
        cv2.put(KOL2_NAZWA, "Brak kategorii");
        cv2.put(KOL2_OPIS, "Brak kategorii");
        long result = db2.insert(TABLE_NAME_2, null, cv2);
    }*/
public Cursor readSumOfIncomeForStatistics(String dataOd, String dataDo) {
    String query = "SELECT SUM("+KOL_KWOTA+") AS SUMA FROM " + TABLE_NAME+
            " WHERE "+KOL_CZYWPLYW+" ='tak' AND "+KOL_DATA+" BETWEEN '"+dataOd+
            "' AND '"+dataDo+"' ";
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
        cursor = db.rawQuery(query, null);

    }
    return cursor;
}
public Cursor readSumOfExpensesForStatistics(String dataOd, String dataDo) {
    String query = "SELECT SUM("+KOL_KWOTA+") AS SUMA FROM " + TABLE_NAME+
            " WHERE "+KOL_CZYWPLYW+" ='nie' AND "+KOL_DATA+" BETWEEN '"+dataOd+
            "' AND '"+dataDo+"' ";
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
        cursor = db.rawQuery(query, null);

    }
    return cursor;
}

    public Cursor readExpensesDataForBarChartStatistics(String dataOd, String dataDo){

             String   query = "SELECT sum("+KOL_KWOTA+"),"+KOL_DATA+" FROM " + TABLE_NAME +
                        " WHERE " + KOL_CZYWPLYW + " ='nie' AND " + KOL_DATA + " BETWEEN '" + dataOd +
                        "' AND '" + dataDo + "' GROUP BY "+KOL_DATA+";";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }
    public Cursor readIncomeDataForBarChartStatistics(String dataOd, String dataDo){

             String   query = "SELECT sum("+KOL_KWOTA+"),"+KOL_DATA+" FROM " + TABLE_NAME +
                        " WHERE " + KOL_CZYWPLYW + " ='tak' AND " + KOL_DATA + " BETWEEN '" + dataOd +
                        "' AND '" + dataDo + "' GROUP BY "+KOL_DATA+";";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    } public Cursor readDataForPieChartStatistics(String dataOd, String dataDo){

        String query = "SELECT "+TABLE_NAME_2+"."+KOL2_NAZWA+", SUM("+TABLE_NAME+"."+KOL_KWOTA+") FROM " + TABLE_NAME + " INNER JOIN "
                + TABLE_NAME_2 + " ON " + TABLE_NAME + "." + KOL_KATEGORIA_ID + " = "
                + TABLE_NAME_2 + "." + KOL2_ID+" WHERE "+ TABLE_NAME+"."+KOL_DATA + " BETWEEN '" + dataOd +
                "' AND '" + dataDo + "' GROUP BY "+TABLE_NAME_2+"."+KOL2_NAZWA+" ORDER BY SUM("+TABLE_NAME+"."+KOL_KWOTA+") DESC LIMIT 6;";
        /* " WHERE " +TABLE_NAME+"."+ KOL_CZYWPLYW + " ='tak' AND " +*/
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }

}