package icreate.fresco;

import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageButton;

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
	Random random;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		database = FrescoMain.getDatabase();
		random = new Random();
		
		initializingImageButtons();
		initializingButtons();
		shuffle();
		
		int deckID = getIntent().getIntExtra(Constant.DECK_ID, -1);
		if(deckID != -1) {
			getCardFromADeck(deckID);
		} else {
			getCards();
		}

		database = FrescoMain.getDatabase();
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
		ArrayList<Card> cardList = database.getCards(cardIndexList);
		for(int i = 0; i < 4; i++){
			Card card = new Card();
			card = cardList.get(i);
			cardList.add(card);
		}
		setCardFront();
		setCardBack();

	}

	public void getCardFromADeck(int deckID){
		//Get four cards randomly front a specific deck
		int sizeDeck = database.getNumberOfCards(deckID);
		ArrayList<Integer> cardIndexList = getRandomList(sizeDeck);
		ArrayList<Card> cardList = database.getCards(deckID, cardIndexList);

		for(int i = 0; i < 4; i++){
			Card card = new Card();
			card = cardList.get(i);
			cardList.add(card);
		}
		setCardFront();
		setCardBack();
	}

	public int matchCards(){

		//match the card's fronts and back and give the results out of 4


		return 0;
	}

	public void setCardFront(){
		for(int i = 0; i < 4; i++){
			if(cardList.get(i).getType(Side.FRONT) == Type.TEXT){
				frontList[i] = cardList.get(i).getContent(Side.FRONT);
			}
			else{
				bmpFrontList[i] = convertFromJSONToImage(cardList.get(i).getContent(Side.FRONT));
			}
		}

		setFrontCardsContent();

	}

	public void setCardBack(){
		for(int i = 0; i < 4; i++){
			if(cardList.get(i).getType(Side.BACK) == Type.TEXT){
				backList[i] = cardList.get(list.get(i)).getContent(Side.BACK);
			}
			else{
				bmpBackList[i] = convertFromJSONToImage(cardList.get(list.get(i)).getContent(Side.BACK));
			}
		}
		setBackCardsContent();
	}

	public void setBackCardsContent(){
		//Display the cards' back on the game layout
		for(int i = 0; i < 4; i++){
			if(bmpBackList[i] != null){
				imageButtonBackList[i].setImageBitmap(bmpBackList[i]); 
			}
			else{
				buttonBackList[i].setText(backList[i]);
			}
		}
	}

	public void setFrontCardsContent(){
		//Display the cards' front on the game layout
		for(int i = 0; i < 4; i++){
			if(bmpFrontList[i] != null){
				imageButtonFrontList[i].setImageBitmap(bmpFrontList[i]);
			}
			else{
				buttonFrontList[i].setText(frontList[i]);
			}
		}

	}

	public void shuffle(){
		//Shuffle the four cards' back randomly
		int count = 0;
		while(count<4){
			int value = random.nextInt(4);
			if(count == 0){
				list.add(value);
				count++;
			}else{
				if(!list.contains(value)){
					list.add(value);
					count++;
				}
			}
		}
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
		Random random = new Random();

		for(int i=0; i<4; i++){
			int integer = random.nextInt(size-1);
			integerList.add(integer);
		}

		return integerList;
	}

}
