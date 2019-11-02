package com.hw.frsecurity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import android.content.Context;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {

    Context ctx;

    // DB Initilization variables
    static String TABLE_NAME = "EMPLOYEES";
    static int VERSION = 1;

    SQLiteDatabase db;

    public Database(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(content, "EMPLOYEES.db", factory, version);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, IMAGE BLOB, NAME TEXT, DEPARTMENT TEXT)");
        Toast.makeText(ctx, "TABLE: " + TABLE_NAME + " VERSION: " + VERSION + " has been created.", Toast.LENGTH_LONG).show()
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == VERSION) {
            // db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
            VERSION = newVersion;
        }
    }

    public void insert(int id, byte[] img, String name, String department) {
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("IMAGE", img);
        cv.put("NAME", name);
        cv.put("DEPARTMENT", department);

        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, cv);

        // insert into EMPLOYEES(id,image,name,department) values ("id", "img", "name", "department")
        db.execSQL("INSERT INTO " + TABLE_NAME + "(ID, IMAGE, NAME, DEPARTMENT) VALUES" + "(\"" + id + "\", \"" + img + "\", \"" + name + "\" , \"" + department + "\"");
    }

    // return all employees
    public Cursor getEmployees() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    // return employee with the matching ID
    public Cursor getEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAM + " WHERE ID = ?", id);
        return data;
    }
}
