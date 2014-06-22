package icreate.fresco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* This class is in charge of all database manipulations */
public class Database extends SQLiteOpenHelper {
	public static final String DB_NAME = "fresco.db";
	public static final int DB_VERSION = 1;
	
	//private Context _context;
	
	public Database(Context context){
		super(context, DB_NAME, null, DB_VERSION);
		//_context = context;
	}
	
	public SQLiteDatabase readOp(){
		return this.getReadableDatabase();
	}
	
	public SQLiteDatabase writeOp(){
		return this.getWritableDatabase();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlCreateDeck = "create table decks(" + 
				"_id integer primary key autoincrement, " + 
				"name text not null);";
		db.execSQL(sqlCreateDeck);
		String sqlCreateCard = "create table cards(" +
				"_id integer primary key autoincrement, " + 
				"front_type text not null, "+
				"front_content text not null, " +
				"back_type text not null, " +
				"back_content text not null, " +
				"deck_id integer" +
				");";
		db.execSQL(sqlCreateCard);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists cards");
		db.execSQL("drop table if exists decks");
		onCreate(db);
	}
}
