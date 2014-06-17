package icreate.fresco;

public class Card {
	int _cardID;
	Type _frontType;
	String _frontContent;
	Type _backType;
	String _backContent;
	
	public enum Type {
		TEXT,
		IMAGE,
		CAMERA,
		DOODLE
	}
	
	public enum Side {
		FRONT,
		BACK
	}
	
	public Card(){
		_frontType = Type.TEXT;
		_frontContent = "";
		_backType = Type.TEXT;
		_backContent = "";
	}
	
	public int getCardID() {
		return _cardID;
	}
	
	public void setCardID(int id) {
		_cardID = id;
	}
	
	public Type getType(Side side){
		if(side == Side.FRONT){
			return _frontType;
		}else{
			return _backType;
		}
	}
	
	public String getContent(Side side){
		if(side == Side.FRONT){
			return _frontContent;
		}else{
			return _backContent;
		}
	}
	
	public void setType(Side side, Type type){
		if(side == Side.FRONT){
			_frontType = type;
		}else{
			_backType = type;
		}
	}
	
	public void setContent(Side side, String content){
		if(side == Side.FRONT){
			_frontContent = content;
		}else{
			_backContent = content;
		}
	}
	
	public void setFront(Type type, String content){
		_frontType = type;
		_frontContent = content;
	}
	
	public void setBack(Type type, String content){
		_backType = type;
		_backContent = content;
	}
}
