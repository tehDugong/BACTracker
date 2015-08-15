package org.cs160.bactracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class DBAdapterWearable {

    private static final String TAG = "DBAdapter"; //used for logging database version changes

    // Field Names:
    public static final String KEY_NAME = "_id";
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_ABV = "abv";
    public static final String KEY_CAL = "calories";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_COUNT = "count";
    public static final String KEY_IMAGE = "image";

    public static final String[] ALL_KEYS = new String[]{KEY_NAME, KEY_INGREDIENTS, KEY_ABV, KEY_CAL, KEY_COUNT, KEY_IMAGE, KEY_CATEGORY};

    // Column Numbers for each Field Name:
    public static final int COL_NAME = 0;
    public static final int COL_INGREDIENTS = 1;
    public static final int COL_ABV = 2;
    public static final int COL_CAL = 3;
    public static final int COL_COUNT = 4;
    public static final int COL_IMAGE = 5;
    public static final int COL_CATEGORY = 6;

    private static DBAdapterWearable sInstance;

    // DataBase info:
    public static final String DATABASE_NAME = "DrinkDatabase";
    public static final String DATABASE_TABLE = "databaseTable";
    public static final int DATABASE_VERSION = 7; // The version number must be incremented each time a change to DB structure occurs.

    //SQL statement to create database
    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " (" + KEY_NAME + " TEXT, "
                    + KEY_INGREDIENTS + " TEXT, "
                    + KEY_CAL + " INT, "
                    + KEY_ABV + " DECIMAL(10,5), "
                    + KEY_COUNT + " INT, "
                    + KEY_IMAGE + " TEXT, "
                    + KEY_CATEGORY + " TEXT "
                    + ");";

    private final Context context;
    private static DatabaseHelperWearable myDBHelper;
    private static SQLiteDatabase db;

    public DBAdapterWearable(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelperWearable(ctx);
        db = myDBHelper.getWritableDatabase();
    }
    public static synchronized DBAdapterWearable getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBAdapterWearable(context.getApplicationContext());
        }
        return sInstance;
    }


    public DBAdapterWearable openToRead() throws SQLException {
        try {
            myDBHelper = new DatabaseHelperWearable(context);
            db = myDBHelper.getReadableDatabase();
        } catch (Exception e) {
        }
        return this;
    }

    public DBAdapterWearable openToWrite() throws SQLException {
        try {
            myDBHelper = new DatabaseHelperWearable(context);
            db.execSQL(DATABASE_CREATE_SQL);
            db = myDBHelper.getWritableDatabase();
        } catch (Exception e) {
        }
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the database.
    public long insertRow(String name, String ingredients, int cal, double abv, int count, String image, String category) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_INGREDIENTS, ingredients);
        initialValues.put(KEY_ABV, abv);
        initialValues.put(KEY_CAL, cal);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_COUNT, count);
        initialValues.put(KEY_IMAGE, image);

        // Insert the data into the database.
        long ret = 0;

        ret = db.insert(DATABASE_TABLE, null, initialValues);
        return ret;
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(String rowId) {
        String where = KEY_NAME + "=" + rowId;
        boolean ret = db.delete(DATABASE_TABLE, where, null) != 0;
        return ret;
    }

    public void deleteAll() {
        Log.d(TAG, "delete all");
        db.delete(DATABASE_TABLE, null, null);
    }

    public boolean deleteTable() {
        db.execSQL("delete from " + DATABASE_TABLE);
        return true;
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public void printAllRows(String tag) {
        Cursor c = getAllRows();
        while(!c.isAfterLast()) {
            Log.d(tag, c.getString(COL_NAME));
            c.moveToNext();
        }
    }

    // Get a specific row (by rowId)
    public Cursor getRow(String name) {
        String where = KEY_NAME + " = '" + name +"'";
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRowByName(String name) {
        String predicate = KEY_NAME + " = '" + name + "'";
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                predicate, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            Log.i(TAG, "cursor moved to first");
        }
        return c;
    }

    public boolean checkExists(String dbfield, String fieldValue) {
        String Query = "Select * from " + DATABASE_TABLE + " where " + dbfield + " = '" + fieldValue +"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Cursor getRowByCategory(String cat) {
        String where = KEY_CATEGORY + " = '" + cat + "'";
        Cursor c = db.query(DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            Log.i(TAG, "cursor moved to first");
        }
        return c;
    }

    public Cursor getCategories() {
        String[] array = new String[]{KEY_NAME, KEY_CATEGORY};
        Cursor c = db.query(true, DATABASE_TABLE, array, null, null, "category", null, null, null);
        if (c != null) {
            c.moveToFirst();

        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(String name, int count) {
        String where = KEY_NAME + " = '" + name +"'";
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_COUNT, count);
        // Insert it into the database.
        boolean b = db.update(DATABASE_TABLE, newValues, where, null) != 0;
        return b;
    }

    protected static class DatabaseHelperWearable extends SQLiteOpenHelper {
        DatabaseHelperWearable(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            Log.d(TAG, DATABASE_CREATE_SQL);
            _db.execSQL(DATABASE_CREATE_SQL);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}

