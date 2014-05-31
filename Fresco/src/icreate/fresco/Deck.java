package icreate.fresco;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	private String _name;
	List<Card> _cards;
	
	public Deck(String name){
		_name = name;
		_cards = new ArrayList<Card>();
	}
	
	public int getCardCount(){
		return _cards.size();
	}
	
	public Card get(int index){
		return _cards.get(index);
	}
	
	public List<Card> getAll(){
		return _cards;
	}
	
	public void addCard(Card card){
		_cards.add(card);
	}
	
	@Override
	public String toString(){
		return _name + " (" + getCardCount() + ")";
	}
}
