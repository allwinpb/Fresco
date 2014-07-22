package icreate.fresco;

import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchingGame extends Activity {
	private ImageButton imageButtonFrontList[] = new ImageButton[4];//For image content in the card
	private ImageButton imageButtonBackList[] = new ImageButton[4];//For image content in the card

	private Button buttonFrontList[] = new Button[4];//For text content in the card
	private Button buttonBackList[] = new Button[4];//For text content in the card

	private Bitmap bmpFrontList[] = new Bitmap[4];//For image content in the card 
	private Bitmap bmpBackList[] = new Bitmap[4];//For image content in the card

	private String frontList[] = new String[4];//For text content in the card
	private String backList[] = new String[4];//For text content in the card

	private List<Card> cardList = new ArrayList<Card>(4);
	private SqliteHelper database;
	ArrayList<Integer> list = new ArrayList<Integer>();
	
	int deckID;
	int positionColor;
	int size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		database = FrescoMain.getDatabase();
		
		initializingImageButtons();
		initializingButtons();
		
		Intent intent = getIntent();
		deckID = intent.getIntExtra(Constant.DECK_ID, -1);
		positionColor = intent.getIntExtra(Constant.POSITION_COLOR, 0);
		selectCards();
	}
	private void selectCards(){
		shuffle();
		if(deckID != -1) {
			setUpIconofDeckName(deckID);
			getCardFromADeck(deckID);
		} else {
			getCards();
		}
		
	}
	public void reset(View v){
		selectCards();
	}
	private void setUpIconofDeckName(int deckID) {
		Deck deck = database.getDeck(deckID);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(deck.getDeckName());
		int id = getResources().getIdentifier(deck.getDeckIcon(), "drawable", getPackageName());
		actionBar.setIcon(id);
		ImageView homeIcon = (ImageView) findViewById(android.R.id.home);
		homeIcon.setPadding(15, 0, 15, 0);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void initializingButtons() {
		// TODO Auto-generated method stub
		buttonFrontList[0] = (Button)findViewById(R.id.cardFront1);
		buttonFrontList[1] = (Button)findViewById(R.id.cardFront2);
		buttonFrontList[2] = (Button)findViewById(R.id.cardFront3);
		buttonFrontList[3] = (Button)findViewById(R.id.cardFront4);

		buttonBackList[0]  = (Button)findViewById(R.id.cardBack1);
		buttonBackList[1]  = (Button)findViewById(R.id.cardBack2);
		buttonBackList[2]  = (Button)findViewById(R.id.cardBack3);
		buttonBackList[3]  = (Button)findViewById(R.id.cardBack4);
	}

	private void initializingImageButtons() {
		// TODO Auto-generated method stub
		imageButtonFrontList[0] = (ImageButton)findViewById(R.id.cardImageFront1);
		imageButtonFrontList[1] = (ImageButton)findViewById(R.id.cardImageFront2);
		imageButtonFrontList[2] = (ImageButton)findViewById(R.id.cardImageFront3);
		imageButtonFrontList[3] = (ImageButton)findViewById(R.id.cardImageFront4);

		imageButtonBackList[0]  = (ImageButton)findViewById(R.id.cardImageBack1);
		imageButtonBackList[1]  = (ImageButton)findViewById(R.id.cardImageBack2);
		imageButtonBackList[2]  = (ImageButton)findViewById(R.id.cardImageBack3);
		imageButtonBackList[3]  = (ImageButton)findViewById(R.id.cardImageBack4);
	}

	public void getCards(){
		int size = database.getNumberOfCards();
		ArrayList<Integer> cardIndexList = getRandomList(size);
		cardList = database.getCards(cardIndexList);
		
		setCardFront();
		setCardBack();

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				returnBack();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void returnBack() {
		Intent intent = new Intent(this, CardsViewPager.class);
		intent.putExtra(Constant.DECK_ID, deckID);
		intent.putExtra(Constant.POSITION_COLOR, positionColor);
		startActivity(intent);
		finish();
	}

	public void getCardFromADeck(int deckID){
		//Get four cards randomly front a specific deck
		int sizeDeck = database.getNumberOfCards(deckID);
		ArrayList<Integer> cardIndexList = getRandomList(sizeDeck);
		cardList = database.getCards(deckID, cardIndexList);
		
		Log.d("getCardFromADeck", String.valueOf(cardList.size()));

		setCardFront();
		setCardBack();
	}

	public int matchCards(){

		//match the card's fronts and back and give the results out of 4


		return 0;
	}

	public void setCardFront(){
		for(int i = 0; i < 4; i++){
			String content = cardList.get(i).getContent(Side.FRONT);
			if(cardList.get(i).getType(Side.FRONT) == Type.TEXT){
				frontList[i] = content;
			}
			else{
				bmpFrontList[i] = convertFromJSONToImage(content);
			}
		}

		setFrontCardsContent();
	}

	public void setCardBack(){
		for(int i = 0; i < 4; i++){
			String content = cardList.get(list.get(i)).getContent(Side.BACK);
			if(cardList.get(i).getType(Side.BACK) == Type.TEXT){
				backList[i] = content;
			}
			else{
				bmpBackList[i] = convertFromJSONToImage(content);
			}
		}
		setBackCardsContent();
	}

	public void setBackCardsContent(){
		//Display the cards' back on the game layout
		for(int i = 0; i < 4; i++){
			if(bmpBackList[i] != null){
				imageButtonBackList[i].setImageBitmap(bmpBackList[i]);
				imageButtonBackList[i].setVisibility(View.VISIBLE);
				buttonBackList[i].setVisibility(View.GONE);
			}
			else{
				buttonBackList[i].setText(backList[i]);
				buttonBackList[i].setVisibility(View.VISIBLE);
				imageButtonBackList[i].setVisibility(View.GONE);
			}
		}
	}

	public void setFrontCardsContent(){
		//Display the cards' front on the game layout
		for(int i = 0; i < 4; i++){
			if(bmpFrontList[i] != null){
				imageButtonFrontList[i].setImageBitmap(bmpFrontList[i]);
				imageButtonFrontList[i].setVisibility(View.VISIBLE);
				buttonFrontList[i].setVisibility(View.GONE);
			}
			else{
				buttonFrontList[i].setText(frontList[i]);
				buttonFrontList[i].setVisibility(View.VISIBLE);
				imageButtonFrontList[i].setVisibility(View.GONE);
			}
		}

	}

	public void shuffle(){
		//Shuffle the four cards' back randomly
		list = getRandomList(4);
	}

	private Bitmap convertFromJSONToImage(String jsonString) {
		try {
			byte[] encodeByte = Base64.decode(jsonString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage(); 
			return null;
		}
	}

	private ArrayList<Integer> getRandomList(int size) {

		ArrayList<Integer> integerList = new ArrayList<Integer>();
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<size; i++)
			list.add(i);
		Collections.shuffle(list);
		
		for(int i=0; i<4; i++) {
			integerList.add(list.get(i));
			Log.d("getRandomList", String.valueOf(list.get(i)));
		}
		
		return integerList;
	}	 

}
