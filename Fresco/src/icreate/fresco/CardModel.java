package icreate.fresco;

import android.content.Context;

public class CardModel {
	String _id;
	Type _frontType;
	String _frontContent;
	Type _backType;
	String _backContent;
	String _deck_id;
	
	private Database _db;
	
	public enum Type {
		TEXT,
		IMAGE,
		CAMERA,
		DOODLE
	}

	public CardModel(Context context){
		_db = new Database(context);
		_id="";
		_frontType = Type.TEXT;
		_frontContent = "";
		_backType = Type.TEXT;
		_backContent = "";
		_deck_id = "";
	}
	
	//Setter getter methods for card properties
	public String id(){
		return _id;
	}
	public void id(String id){
		_id = id;
	}
	
	public Type frontType(){
		return _frontType;
	}
	public void frontType(Type frontType){
		_frontType = frontType;
	}
	public void frontType(String frontType){
		_frontType = stringToType(frontType);
	}

	public String frontContent(){
		return _frontContent;
	}
	public void frontContent(String frontContent){
		_frontContent = frontContent;
	}

	public Type backType(){
		return _backType;
	}
	public void backType(Type backType){
		_backType = backType;
	}
	public void backType(String backType){
		_backType = stringToType(backType);
	}
	public String backContent(){
		return _backContent;
	}
	public void backContent(String backContent){
		_backContent = backContent;
	}
	
	private Type stringToType(String input){
		if(input.equalsIgnoreCase("text")){
			return Type.TEXT;
		}else if(input.equalsIgnoreCase("image")){
			return Type.IMAGE;
		}else if(input.equalsIgnoreCase("doodle")){
			return Type.DOODLE;
		}
		return Type.TEXT;
	}
}
