package icreate.fresco;

import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "fresco.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_DECK = "decks";
	public static final String DECK_ID = "_id";
	public static final String DECK_NAME = "name";
	public static final String DECK_ICON_NAME = "icon_name";
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
			DECK_NAME + " text not null, "+
			DECK_ICON_NAME + " text not null);";
	
	private static final String TABLE_CARD_CREATE = "create table "
			+ TABLE_CARD + "(" + 
			CARD_ID + " integer primary key autoincrement, " + 
			CARD_FRONT_TYPE + " text not null, "+
			CARD_FRONT_CONTENT + " text not null, " +
			CARD_BACK_TYPE + " text not null, " +
			CARD_BACK_CONTENT + " text not null, " +
			CARD_DECK_ID + " integer" +
			");";

	private static final String TABLE_CARD_LOAD = "SELECT * FROM " + TABLE_CARD + " ORDER BY " + CARD_ID;
	private static final String TABLE_DECK_LOAD = "SELECT * FROM " + TABLE_DECK + " ORDER BY " + DECK_ID;

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

	public ArrayList<Deck> getDecks() {

		ArrayList<Deck> listDeck = new ArrayList<Deck>();

		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(TABLE_DECK_LOAD, null);

		if(cursor.moveToFirst()) {
			do {

				int deckID = Integer.parseInt(cursor.getString(0));
				String deckName = cursor.getString(1);
				String iconName = cursor.getString(2);

				Deck deck = new Deck(deckName);
				deck.setDeckID(deckID);
				deck.setDeckIcon(iconName);

				listDeck.add(deck);

			} while(cursor.moveToNext());
		}

		database.close();
		Log.d("SqliteHelper", "getDecks() " + listDeck.size());

		return listDeck;
	}

	public void insertDeck(Deck deck) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(DECK_NAME, deck.getDeckName());
		values.put(DECK_ICON_NAME, deck.getDeckIcon());

		database.insert(TABLE_DECK, null, values);
		database.close();
		Log.d("SqliteHelper", "Deck saved");
	}
	
	public int getDeckID(Deck deck) {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(TABLE_DECK_LOAD, null);
		
		int deckID = 0;
		
		if(cursor.moveToFirst()) {
			do {
			
				int deckId = Integer.parseInt(cursor.getString(0));
				String deckName = cursor.getString(1);
				String iconName = cursor.getString(2);
				
				if(deck.getDeckName().equals(deckName) && deck.getDeckIcon().equals(iconName)) {
					deckID = deckId;
				}
				
			} while(cursor.moveToNext());
		}

		database.close();
		
		return deckID;
	}

	public int updateDeck(Deck deck) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(DECK_NAME, deck.getDeckName());
		values.put(DECK_ICON_NAME, deck.getDeckIcon());

		int result = database.update(TABLE_DECK, values, DECK_ID + " = ?", new String[]{
				String.valueOf(deck.getDeckID())
		});

		database.close();

		return result;
	}

	public void deleteDeck(int deckID) {
		SQLiteDatabase database = this.getWritableDatabase();

		String deckQuery = "DELETE FROM " + TABLE_DECK + " WHERE " + DECK_ID + " = " + deckID;
		database.execSQL(deckQuery);

		String cardQuery = "DELETE FROM " + TABLE_CARD + " WHERE " + CARD_DECK_ID + " = " + deckID;
		database.execSQL(cardQuery);

		database.close();

	}
	
	public Deck getDeck(int deckID) {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(TABLE_DECK_LOAD, null);
		Deck deck = new Deck();
		deck.setDeckID(deckID);

		if(cursor.moveToFirst()) {
			do {
				int deckId = Integer.parseInt(cursor.getString(0));
				if(deckId == deckID) {
					
					String deckName = cursor.getString(1);
					String iconName = cursor.getString(2);
					
					deck.setDeckName(deckName);
					deck.setDeckIcon(iconName);
				}

			} while(cursor.moveToNext());
		}

		database.close();

		return deck;
	}

	public Deck getCards(int deckID) {	
		Deck deck = getDeck(deckID); 		
	
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(TABLE_CARD_LOAD, null);

		if(cursor.moveToFirst()) {
			do {
				int id = Integer.parseInt(cursor.getString(5));

				if(id == deckID) {

					Card card = new Card();

					card.setCardID(Integer.parseInt(cursor.getString(0)));
					card.setType(Side.FRONT, getType(cursor.getString(1)));
					card.setContent(Side.FRONT, cursor.getString(2));
					card.setType(Side.BACK, getType(cursor.getString(3)));
					card.setContent(Side.BACK, cursor.getString(4));

					deck.addCard(card);
				}

			} while(cursor.moveToNext());
		}

		database.close();

		return deck;
	}

	public void insertCard(int deckID, Card card) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CARD_FRONT_TYPE, getTypeString(card.getType(Side.FRONT)));
		values.put(CARD_FRONT_CONTENT, card.getContent(Side.FRONT));
		values.put(CARD_BACK_TYPE, getTypeString(card.getType(Side.BACK)));
		values.put(CARD_BACK_CONTENT, card.getContent(Side.BACK));
		values.put(CARD_DECK_ID, String.valueOf(deckID));

		database.insert(TABLE_CARD, null, values);
		database.close();
	}

	public int updateCard(int deckID, Card card) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CARD_FRONT_TYPE, getTypeString(card.getType(Side.FRONT)));
		values.put(CARD_FRONT_CONTENT, card.getContent(Side.FRONT));
		values.put(CARD_BACK_TYPE, getTypeString(card.getType(Side.BACK)));
		values.put(CARD_BACK_CONTENT, card.getContent(Side.BACK));
		values.put(CARD_DECK_ID, String.valueOf(deckID));

		int result = database.update(TABLE_CARD, values, CARD_ID + " = ?", new String[]{
				String.valueOf(card.getCardID())
		});

		database.close();

		return result;
	}

	public void deleteCard(int cardID) {
		SQLiteDatabase database = this.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_CARD + " WHERE " + CARD_ID + " = " + cardID;

		database.execSQL(query);

		database.close();
	}

	public Card getCard(int deckID, int cardID) {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(TABLE_CARD_LOAD, null);

		Card card = new Card();

		if(cursor.moveToFirst()) {
			do {
				int deckId = Integer.parseInt(cursor.getString(5));
				int cardId = Integer.parseInt(cursor.getString(0));

				if(deckId == deckID && cardId == cardID) {

					card.setCardID(cardId);
					card.setType(Side.FRONT, getType(cursor.getString(1)));
					card.setContent(Side.FRONT, cursor.getString(2));
					card.setType(Side.BACK, getType(cursor.getString(3)));
					card.setContent(Side.BACK, cursor.getString(4));

				}

			} while(cursor.moveToNext());
		}

		database.close();

		return card;
	}

	private Type getType(String type) {
		switch(type.toUpperCase(Locale.getDefault())) {
		case "TEXT":
			return Type.TEXT;
		case "IMAGE":
			return Type.IMAGE;
		case "DOODLE":
			return Type.DOODLE;
		default:
			return Type.CAMERA;
		}
	}

	private String getTypeString(Type type) {
		switch(type) {
		case TEXT:
			return "TEXT";
		case IMAGE:
			return "IMAGE";
		case DOODLE:
			return "DOODLE";
		default:
			return "CAMERA";
		}
	}
}