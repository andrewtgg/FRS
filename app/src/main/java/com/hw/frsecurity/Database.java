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
        super(context, "EMPLOYEES.db", factory, version);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMPLOYEE_ID INTEGER UNIQUE, IMAGE BLOB, NAME TEXT, DEPARTMENT TEXT, LAST_SEEN TEXT);");
        Toast.makeText(ctx, "TABLE: " + TABLE_NAME + " VERSION: " + VERSION + " has been created.", Toast.LENGTH_LONG).show();
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
    /*
    public void insert(int id, byte[] img, String name, String department, String lastSeen) {
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("IMAGE", img);
        cv.put("NAME", name);
        cv.put("DEPARTMENT", department);
        cv.put("LAST_SEEN", lastSeen);

        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, cv);

        // insert into EMPLOYEES(id,image,name,department) values ("id", "img", "name", "department")
        db.execSQL("INSERT INTO " + TABLE_NAME + "(ID, IMAGE, NAME, DEPARTMENT, LAST_SEEN) VALUES" + "(\"" + id + "\", \"" + img + "\", \"" + name + "\" , \"" + department + "\", \"" + lastSeen + "\");");
    }
    */

    public void insertEmployee(Employee employee) {

        ContentValues cv = new ContentValues();
        cv.put("EMPLOYEE_ID", employee.getId());
        cv.put("IMAGE", employee.getImg());
        cv.put("NAME", employee.getName());
        cv.put("DEPARTMENT", employee.getDepartment());
        cv.put("LAST_SEEN", employee.getLastSeen());

        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, cv);

        db.execSQL("INSERT OR IGNORE INTO " + TABLE_NAME + "(EMPLOYEE_ID, IMAGE, NAME, DEPARTMENT, LAST_SEEN) VALUES" + "(\"" + employee.getId() + "\", \"" + employee.getImg() + "\", \"" + employee.getName() + "\" , \"" + employee.getDepartment() + "\", \"" + employee.getLastSeen() + "\");");
    }

    // return all employees
    public Cursor getEmployees() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
        return data;
    }

    // return employee with the matching ID
    public Cursor getEmployeeById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String idToStr = String.valueOf(id);
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?", new String[] { idToStr } );
        return data;
    }
}
