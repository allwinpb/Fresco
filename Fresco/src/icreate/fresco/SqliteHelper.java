package icreate.fresco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "fresco.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_DECK = "decks";
	public static final String DECK_ID = "_id";
	public static final String DECK_NAME = "name";
	public static final String TABLE_CARD = "cards";
	public static final String CARD_ID = "_id";
	public static final String CARD_FRONT_TYPE = "front_type";
	public static final String CARD_FRONT_CONTENT = "front_content";
	public static final String CARD_BACK_TYPE = "back_type";
	public static final String CARD_BACK_CONTENT = "name";
	public static final String CARD_DECK_ID = "deck_id";

	// Database creation sql statement
	private static final String TABLE_DECK_CREATE = "create table "
			+ TABLE_DECK + "(" + 
			DECK_ID + " integer primary key autoincrement, " + 
			DECK_NAME + " text not null);";
	private static final String TABLE_CARD_CREATE = "create table "
			+ TABLE_CARD + "(" + 
			CARD_ID + " integer primary key autoincrement, " + 
			CARD_FRONT_TYPE + " text not null, "+
			CARD_FRONT_CONTENT + " text not null, " +
			CARD_BACK_TYPE + " text not null, " +
			CARD_BACK_CONTENT + " text not null, " +
			CARD_DECK_ID + " integer" +
			");";

	public SqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_DECK_CREATE);
		database.execSQL(TABLE_CARD_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SqliteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);
		onCreate(db);
	}
}