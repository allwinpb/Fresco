package icreate.fresco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CardModel {
	String _id;
	Type _frontType;
	String _frontContent;
	Type _backType;
	String _backContent;
	String _deck_id;

	private Database _db;

	public enum Type {
		TEXT, IMAGE, CAMERA, DOODLE
	}

	public CardModel(Context context) {
		_db = new Database(context);
		_id = "";
		_frontType = Type.TEXT;
		_frontContent = "";
		_backType = Type.TEXT;
		_backContent = "";
		_deck_id = "";
	}

	public void load(String id) {
		_id = id;
		reload();
	}

	public void load(Long id) {
		load(Long.toString(id));
	}

	public void reload() {
		// Update id, name and the cards list
		if (_id.equals("")) {
			// This card is not in the database
			return;
		}

		SQLiteDatabase dbHandle = _db.readOp();
		Cursor i = dbHandle.rawQuery("select * from decks where _id=? limit 1",
				new String[] { _id });
		if (i.moveToFirst()) {
			_id = i.getString(0);
			frontType(i.getString(1));
			frontContent(i.getString(2));
			backType(i.getString(3));
			backContent(i.getString(4));
			_deck_id = i.getString(5);
		}
		dbHandle.close();
	}

	public boolean save() {
		SQLiteDatabase dbHandle = _db.writeOp();
		ContentValues values = new ContentValues();
		values.put("front_type", typeToString(_frontType));
		values.put("front_content", _frontContent);
		values.put("back_type", typeToString(_backType));
		values.put("back_content", _backContent);
		values.put("deck_id", _deck_id);
		if (_id.equals("")) {
			// This is a create operation
			_id = Long.toString(dbHandle.insert("cards", null, values));
			if (_id.equals("-1")) {
				_id = "";
				return false;
			}
			return true;
		} else {
			// This is an update operation
			return dbHandle.update("cards", values, "_id=?",
					new String[] { _id }) > 0;
		}
	}

	// Removes the current card from the database.
	public boolean destroy() {
		SQLiteDatabase dbHandle = _db.writeOp();
		boolean result = dbHandle
				.delete("cards", "_id=?", new String[] { _id }) != 0;
		_id = "";
		return result;
	}

	// Setter getter methods for card properties
	public String id() {
		return _id;
	}

	public void id(String id) {
		_id = id;
	}

	public Type frontType() {
		return _frontType;
	}

	public void frontType(Type frontType) {
		_frontType = frontType;
	}

	public void frontType(String frontType) {
		_frontType = stringToType(frontType);
	}

	public String frontContent() {
		return _frontContent;
	}

	public void frontContent(String frontContent) {
		_frontContent = frontContent;
	}

	public Type backType() {
		return _backType;
	}

	public void backType(Type backType) {
		_backType = backType;
	}

	public void backType(String backType) {
		_backType = stringToType(backType);
	}

	public String backContent() {
		return _backContent;
	}

	public void backContent(String backContent) {
		_backContent = backContent;
	}

	private Type stringToType(String input) {
		if (input.equalsIgnoreCase("text")) {
			return Type.TEXT;
		} else if (input.equalsIgnoreCase("image")) {
			return Type.IMAGE;
		} else if (input.equalsIgnoreCase("doodle")) {
			return Type.DOODLE;
		} else if (input.equalsIgnoreCase("camera")) {
			return Type.CAMERA;
		}
		return Type.TEXT;
	}

	private String typeToString(Type input) {
		switch (input) {
		case TEXT:
			return "text";
		case IMAGE:
			return "image";
		case DOODLE:
			return "doodle";
		case CAMERA:
			return "camera";
		}
		return "text";
	}
}
