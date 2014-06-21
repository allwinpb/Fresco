package icreate.fresco;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DeckModel{
	private String _name;
	private String _id;
	
	private Database _db;
	private Context _context;
	private List<CardModel> _cards;
	
	//Constructor
	public DeckModel(Context context){
		_id="";
		_name = "";
		_cards = new ArrayList<CardModel>();
		_context = context;
		_db = new Database(context);
	}
	
	//Get a list of all decks
	public static ArrayList<DeckModel> all(Context context){
		ArrayList<DeckModel> decks = new ArrayList<DeckModel>();
		Database db = new Database(context);
		SQLiteDatabase dbHandle = db.readOp();
		Cursor i = dbHandle.rawQuery("select * from decks", null);
		if(i.moveToFirst()){
			do {
				DeckModel deck = new DeckModel(context);
				deck.id(i.getString(0));
				decks.add(deck);
			} while(i.moveToNext());
		}
		return decks;
	}
	
	//Load a deck from the database into this instance
	public void load(String id){
		_id = id;
		reload();
	}
	public void load(Long id){
		load(Long.toString(id));
	}
	
	//Refresh instance with data from the database. Overwrites any changes after save() is called
	public void reload(){
		//Update id, name and the cards list
		if(_id.equals("")){
			//Deck is not pointing to anything
			return;
		}
		
		SQLiteDatabase dbHandle = _db.readOp();
		Cursor i = dbHandle.rawQuery("select * from decks where _id=? limit 1",new String[] {_id});
		if(i.moveToFirst()){
			_id = i.getString(0);
			_name = i.getString(1);
		}else{
			//Deck does not exist in database yet
		}
		//Update cards
		i = dbHandle.rawQuery("select * from cards where deck_id=?", new String[]{_id});
		_cards.clear();
		if(i.moveToFirst()){
			do {
				CardModel card = new CardModel(_context);
				card.id(i.getString(0));
				card.frontType(i.getString(1));
				card.frontContent(i.getString(2));
				card.backType(i.getString(3));
				card.backContent(i.getString(4));
				_cards.add(card);
			} while(i.moveToNext());
		}
		dbHandle.close();
	}
	
	//Commit changes to database. The entire row in the database is overwritten with the values in the instance
	public boolean save(){
		SQLiteDatabase dbHandle = _db.writeOp();
		ContentValues values = new ContentValues();
		values.put("name", _name);
		if(_id.equals("")){
			//This is a create operation
			_id = Long.toString(dbHandle.insert("decks", null, values));
			if(_id.equals("-1")){
				_id = "";
				return false;
			}
			return true;
		}else{
			//This is an update operation
			return dbHandle.update("decks", values, "_id=?", new String[]{_id}) > 0;
		}
	}
	
	//getter setter methods
	public String id(){
		return _id;
	}
	public void id(String id){
		_id = id;
	}
	
	public String name(){
		return _name;
	}
	public void name(String name){
		_name = name;
	}
	
	@Override
	public String toString(){
		return _name + " (" + _cards.size() + ")";
	}
}
