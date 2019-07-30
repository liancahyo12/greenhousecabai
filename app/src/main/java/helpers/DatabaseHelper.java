package helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ghc.db";
    public static final String TABLE_NAME = "data_kondisi";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "SUHU";
    public static final String COL_3 = "KTANAH";
    public static final String COL_4 = "DATE";
    public static final String TABLE_NAME1 = "data_kontrol";
    public static final String COL_12 = "ID";
    public static final String COL_22 = "LAMPU";
    public static final String COL_32 = "KIPAS";
    public static final String COL_42 = "KRAN";
    public static final String COL_52 = "DATE";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,SUHU INTEGER,KTANAH INTEGER,DATE INTEGER)");
        db.execSQL("create table " + TABLE_NAME1 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LAMPU INTEGER,KIPAS INTEGER,KRAN INTEGER,DATE INTEGER)");
        db.execSQL("insert into data_kontrol values (1,0,0,0,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        onCreate(db);
    }

    public boolean insertData(int suhu,int ktanah, long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,suhu);
        contentValues.put(COL_3,ktanah);
        contentValues.put(COL_4,date);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertDatac(Integer lampu ,Integer kipas,Integer kran, long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_22,lampu);
        contentValues.put(COL_32,kipas);
        contentValues.put(COL_42,kran);
        contentValues.put(COL_52,date);
        long result = db.insert(TABLE_NAME1,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getAllDatac() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME1,null);
        return res;
    }
    public Cursor getLastDatac() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME1+" ORDER BY ID DESC LIMIT 1",null);
        return res;
    }
    public Cursor getLastData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" ORDER BY ID DESC LIMIT 1",null);
        return res;
    }
    public Cursor getFirstData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" ORDER BY ID ASC LIMIT 1",null);
        return res;
    }

    public boolean updateData(String id,int suhu,int ktanah, long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,suhu);
        contentValues.put(COL_3,ktanah);
        contentValues.put(COL_4,date);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}