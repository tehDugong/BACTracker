package org.cs160.bactracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class DBAdapter {

	private static final String TAG = "DBAdapter"; //used for logging database version changes

	// Field Names:
	public static final String KEY_NAME = "_id";
	public static final String KEY_INGREDIENTS = "ingredients";
	public static final String KEY_ABV = "abv";
	public static final String KEY_CAL = "calories";
	public static final String KEY_CATEGORY = "category";

	public static final String[] ALL_KEYS = new String[] {KEY_NAME, KEY_INGREDIENTS, KEY_ABV, KEY_CAL, KEY_CATEGORY};

	// Column Numbers for each Field Name:
	public static final int COL_NAME = 0;
	public static final int COL_INGREDIENTS = 1;
	public static final int COL_CAL = 2;
	public static final int COL_ABV = 3;
	public static final int COL_CATEGORY = 4;

	// DataBase info:
	public static final String DATABASE_NAME = "DrinkDatabase";
	public static final String DATABASE_TABLE = "mainDrinkDatabase";
	public static final int DATABASE_VERSION = 5; // The version number must be incremented each time a change to DB structure occurs.

	//SQL statement to create database
	private static final String DATABASE_CREATE_SQL =
			"CREATE TABLE " + DATABASE_TABLE
					+ " (" + KEY_NAME + " TEXT, "
					+ KEY_INGREDIENTS + " TEXT, "
					+ KEY_CAL + " INT, "
					+ KEY_ABV + " DECIMAL(10,5), "
					+ KEY_CATEGORY + " TEXT "
					+ ");";

	private Context context;
	private static DatabaseHelper myDBHelper;
	private static SQLiteDatabase db;



	protected static final String[] genericDrinks = new String[] {"Merlot", "Chardonnay", "Guinness",
	"Heineken", "Long Island Iced Tea", "Margarita", "Vodka", "Whisky"};


	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
        db = myDBHelper.getWritableDatabase();



        this.insertRow("Merlot", 122, 14.5, "Wine", "Merlot grapes");
        this.insertRow("Chardonnay", 123, 14.5, "Wine", "Chardonnay grapes");
        this.insertRow("Guinness", 125, 4.1, "Beer", "Roasted unmalted barley");
        this.insertRow("Heineken", 150, 5, "Beer", "Barley malt, hops and the unique Heineken A-yeast");
        this.insertRow("Long Island Iced Tea", 780, 22, "Cocktail", "Gin, Tequila, Vodka, Run, Triple sec");
		this.insertRow("Margarita", 153, 22, "Cocktail", "Tequila, triple sec, lime, lemon");
        this.insertRow("Vodka", 64, 40, "Liquor", "Water, ethanol");
		this.insertRow("Whisky", 70, 40, "Liquor", "Fermented grain mash");
	}


	// Open the database connection.
	public DBAdapter open() {
		myDBHelper = new DatabaseHelper(context);
		db = myDBHelper.getWritableDatabase();
		this.insertRowNoDups("Merlot", 122, 14.5, "Wine", "Merlot grapes");
		this.insertRowNoDups("Chardonnay", 123, 14.5, "Wine", "Chardonnay grapes");
		this.insertRowNoDups("Guinness", 125, 4.1, "Beer", "Roasted unmalted barley");
		this.insertRowNoDups("Heineken", 150, 5, "Beer", "Barley malt, hops and the unique Heineken A-yeast");
		this.insertRowNoDups("Long Island Iced Tea", 780, 22, "Cocktail", "Gin, Tequila, Vodka, Run, Triple sec");
		this.insertRowNoDups("Margarita", 153, 22, "Cocktail", "Tequila, triple sec, lime, lemon");
		this.insertRowNoDups("Vodka", 64, 40, "Liquor", "Water, ethanol");
		this.insertRowNoDups("Whisky", 70, 40, "Liquor", "Fermented grain mash");
		return this;
	}
	public DBAdapter openToRead() throws SQLException {
		try {
			myDBHelper = new DatabaseHelper(context);
			db = myDBHelper.getReadableDatabase();
		} catch (Exception e){}
		return this;
	}

	public DBAdapter openToWrite() throws SQLException {
		try {
			myDBHelper = new DatabaseHelper(context);
			db = myDBHelper.getWritableDatabase();
		} catch (Exception e){}
		return this;
	}
	public void printAllRows(String tag) {
		Cursor c = getAllRows();
		while (!c.isAfterLast()) {
			Log.d(tag, c.getString(COL_NAME));
			c.moveToNext();
		}
	}

	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}

	// Add a new set of values to be inserted into the database.
	public long insertRow(String name, int cal, double abv, String category, String ingredients) {
//	public long insertRow(String name, double abv, int cal, String ingredients) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_INGREDIENTS, ingredients);
		initialValues.put(KEY_ABV, abv);
		initialValues.put(KEY_CAL, cal);
		initialValues.put(KEY_CATEGORY, category);
		initialValues.put(KEY_NAME, name);

		// Insert the data into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public void insertRowNoDups(String name, int cal, double abv, String category, String ingredients)
	{
		String where = KEY_NAME + "= '" + name + "' AND " + KEY_CATEGORY + " = '" +category +"'" ;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
				where, null, null, null, null, null);
		if (this.existsInDatabase(name, category))
			this.insertRow( name,  cal,  abv,  category,  ingredients);
	}
	public boolean existsInDatabase(String name, String category) {
		String where = KEY_NAME + "= '" + name + "' AND " + KEY_CATEGORY + " = '" +category +"'" ;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
				where, null, null, null, null, null);
		return c.getCount() > 0;
	}

	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(String rowId) {
		String where = KEY_NAME + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}

	public void deleteAll() {
        Log.d(TAG, "delete all");
        /*
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_NAME);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));
			} while (c.moveToNext());
		}
		c.close();
		*/
        db.delete(DATABASE_TABLE, null, null);
	}

	// Return all data in the database.
	public Cursor getAllRows() {
		try {
			this.openToRead();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		this.close();
		return c;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_NAME + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
				where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor getRowByName(String name){
		String predicate = KEY_NAME + "= '" + name + "'";

//		String predicate = "name = ?";
//		String[] predicate_values = {name};
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
				predicate, null, null, null, null, null);
//		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
//				predicate, predicate_values, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			Log.i(TAG, "cursor moved to first");
		}
		return c;	}

    public Cursor getRowByCategory(String cat){
		try {
			this.openToRead();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        String predicate = KEY_CATEGORY + "= '" + cat + "'";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                predicate, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            Log.i(TAG, "cursor moved to first");
        }
		close();
        return c;	}

    public Cursor getCategories(){
        String[] array = new String[]{KEY_NAME, KEY_CATEGORY};
        Cursor c = db.query(true, DATABASE_TABLE, array, null, null, "category", null, null, null);
        if (c != null){
            c.moveToFirst();

        }
        return c;
    }

	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String task, String date) {
		String where = KEY_NAME + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_INGREDIENTS, task);
		newValues.put(KEY_ABV, date);
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}


	protected static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
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

