package icreate.fresco;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DeckModel{
	private String _name;
	private String _id;
	
	private Database _db;
	private Context _context;
	private List<CardModel> _cards;
	
	public DeckModel(Context context){
		_id="";
		_name = "";
		_cards = new ArrayList<CardModel>();
		_context = context;
		_db = new Database(context);
	}
	
	public void refreshDeck(){
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
	
	public boolean save(){
		if(_id.equals("")){
			//This is a create operation
		}else{
			//This is an update operation
		}
		return true;
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
