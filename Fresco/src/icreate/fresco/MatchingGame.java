package icreate.fresco;

import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sonyericsson.util.ScalingUtilities;
import com.sonyericsson.util.ScalingUtilities.ScalingLogic;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MatchingGame extends Activity implements OnClickListener {
	private ImageButton imageButtonFrontList[] = new ImageButton[4];//For image content in the card
	private ImageButton imageButtonBackList[] = new ImageButton[4];//For image content in the card

	private Button buttonFrontList[] = new Button[4];//For text content in the card
	private Button buttonBackList[] = new Button[4];//For text content in the card

	private Bitmap bmpFrontList[] = new Bitmap[4];//For image content in the card 
	private Bitmap bmpBackList[] = new Bitmap[4];//For image content in the card

	private String frontList[] = new String[4];//For text content in the card
	private String backList[] = new String[4];//For text content in the card

	private ImageView frontBarLeft[] = new ImageView[4];
	private ImageView frontBarRight[] = new ImageView[4];

	private ImageView backBarLeft[] = new ImageView[4];
	private ImageView backBarRight[] = new ImageView[4];

	private ImageView showAnswer;
	private ImageButton submit;


	private List<Card> cardList = new ArrayList<Card>(4);
	private SqliteHelper database;
	ArrayList<Integer> list = new ArrayList<Integer>();
	int order[] = new int[4];

	int match;//To keep track of which front card is chosen to match the back card 
	int deckID;
	int positionColor;
	int size;

	private int mDstWidth;
	private int mDstHeight;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		database = FrescoMain.getDatabase();
		frontBarLeft[0] = (ImageView)findViewById(R.id.frontBarLeft1);
		frontBarLeft[1] = (ImageView)findViewById(R.id.frontBarLeft2);
		frontBarLeft[2] = (ImageView)findViewById(R.id.frontBarLeft3);
		frontBarLeft[3] = (ImageView)findViewById(R.id.frontBarLeft4);

		frontBarRight[0] = (ImageView)findViewById(R.id.frontBarRight1);
		frontBarRight[1] = (ImageView)findViewById(R.id.frontBarRight2);
		frontBarRight[2] = (ImageView)findViewById(R.id.frontBarRight3);
		frontBarRight[3] = (ImageView)findViewById(R.id.frontBarRight4);

		backBarLeft[0] = (ImageView)findViewById(R.id.backBarLeft1);
		backBarLeft[1] = (ImageView)findViewById(R.id.backBarLeft2);
		backBarLeft[2] = (ImageView)findViewById(R.id.backBarLeft3);
		backBarLeft[3] = (ImageView)findViewById(R.id.backBarLeft4);

		backBarRight[0] = (ImageView)findViewById(R.id.backBarRight1);
		backBarRight[1] = (ImageView)findViewById(R.id.backBarRight2);
		backBarRight[2] = (ImageView)findViewById(R.id.backBarRight3);
		backBarRight[3] = (ImageView)findViewById(R.id.backBarRight4);

		showAnswer = (ImageView)findViewById(R.id.show_answer);
		submit     = (ImageButton)findViewById(R.id.match);
		initializingImageButtons();
		initializingButtons();
		shuffle();
		match = 0;//Originally no front card is selected

		mDstWidth = getResources().getDimensionPixelSize(R.dimen.game_card_size);
		mDstHeight = getResources().getDimensionPixelSize(R.dimen.game_card_size);


		for(int i = 0; i < 4; i++){

			buttonFrontList[i].setOnClickListener(this);
			buttonBackList[i].setOnClickListener(this);
			imageButtonFrontList[i].setOnClickListener(this);
			imageButtonBackList[i].setOnClickListener(this);

			order[i] = -1;
		}

		Intent intent = getIntent();
		deckID = intent.getIntExtra(Constant.DECK_ID, -1);
		positionColor = intent.getIntExtra(Constant.POSITION_COLOR, 0);
		selectCards();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		//Front press
		case R.id.cardFront1:
		case R.id.cardImageFront1:
			frontBarLeft[0].setBackgroundColor(Color.GREEN);
			frontBarRight[0].setBackgroundColor(Color.GREEN);
			match = 1;
			break;
		case R.id.cardFront2:
		case R.id.cardImageFront2:
			frontBarLeft[1].setBackgroundColor(Color.BLUE);
			frontBarRight[1].setBackgroundColor(Color.BLUE);
			match = 2;
			break;

		case R.id.cardFront3:
		case R.id.cardImageFront3:
			frontBarLeft[2].setBackgroundColor(Color.YELLOW);
			frontBarRight[2].setBackgroundColor(Color.YELLOW);
			match = 3;
			break;

		case R.id.cardFront4:
		case R.id.cardImageFront4:
			frontBarLeft[3].setBackgroundColor(Color.GRAY);
			frontBarRight[3].setBackgroundColor(Color.GRAY);
			match = 4;
			break;


			//Back press	
		case R.id.cardBack1:
		case R.id.cardImageBack1:
			matchColor(match, 0);
			order[0] = match-1;
			removeDuplicate(0, match-1);
			match = 0;
			break;
		case R.id.cardBack2:
		case R.id.cardImageBack2:
			matchColor(match, 1);
			order[1] = match-1;
			removeDuplicate(1, match-1);
			match = 0;
			break;
		case R.id.cardBack3:
		case R.id.cardImageBack3:
			matchColor(match, 2);
			order[2] = match-1;
			removeDuplicate(2, match-1);
			match = 0;
			break;
		case R.id.cardBack4:
		case R.id.cardImageBack4:
			matchColor(match, 3);
			order[3] = match-1;
			removeDuplicate(3, match-1);
			match = 0;
			break;
		}
	}
	private void removeDuplicate(int currentPosition, int color){
		for(int i = 0; i < 4; i++){
			if(i != currentPosition && order[i] == color){
				order[i] = -1;
				backBarLeft[i].setBackgroundColor(Color.WHITE);
				backBarRight[i].setBackgroundColor(Color.WHITE);
			}

		}

	}
	private void matchColor(int match, int pos){
		switch(match){
		case 1:
			backBarLeft[pos].setBackgroundColor(Color.GREEN);
			backBarRight[pos].setBackgroundColor(Color.GREEN);
			break;
		case 2:
			backBarLeft[pos].setBackgroundColor(Color.BLUE);
			backBarRight[pos].setBackgroundColor(Color.BLUE);
			break;
		case 3:
			backBarLeft[pos].setBackgroundColor(Color.YELLOW);
			backBarRight[pos].setBackgroundColor(Color.YELLOW);
			break;
		case 4:
			backBarLeft[pos].setBackgroundColor(Color.GRAY);
			backBarRight[pos].setBackgroundColor(Color.GRAY);
			break;
		default:
		}
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
		resetSideBarColor();
		selectCards();
		enableButtons();
		showAnswer.setVisibility(View.INVISIBLE);
		submit.setVisibility(View.VISIBLE);
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

	private void getCards(){
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

	private void getCardFromADeck(int deckID){
		//Get four cards randomly front a specific deck
		int sizeDeck = database.getNumberOfCards(deckID);
		ArrayList<Integer> cardIndexList = getRandomList(sizeDeck);
		cardList = database.getCards(deckID, cardIndexList);

		Log.d("getCardFromADeck", String.valueOf(cardList.size()));

		setCardFront();
		setCardBack();
	}

	public int matchCards(){//Return the number of correct matchings, between 0 and 4

		//match the card's fronts and back and give the results out of 4
		int count = 0;
		for(int i = 0; i < 4; i++){
			if(list.get(i) == order[i])
				count++;
		}

		return count;
	}
	private boolean hasMatchedAll(){
		for(int i = 0; i < 4; i++){
			if(order[i] == -1)
				return false;
		}
		return true;
	}
	public void match(View view){
		//showDialog(1, null);
		if(hasMatchedAll()){
			AlertDialog.Builder exitDialog = new AlertDialog.Builder(MatchingGame.this);

			exitDialog
			.setTitle("Matching Result")
			.setCancelable(true)
			.setMessage("You've got "+matchCards()+" matchings correct!")
			.setPositiveButton("Next Set", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					resetSideBarColor();
					selectCards();
				}	
			});
			exitDialog
			.setNegativeButton("Show Answer", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					showAnswer();
				}
			})
			.setIcon(android.R.drawable.ic_dialog_alert);

			AlertDialog dialog = exitDialog.create();
			dialog.show();
		}
		else{
			Toast.makeText(this, "!!! You haven't matched all !!!", Toast.LENGTH_SHORT).show();
		}
	}
	private void showAnswer(){
		showAnswer.setVisibility(View.VISIBLE);
		submit.setVisibility(View.INVISIBLE);
		setCorrectCardBack();
		setCorrectBackBarColor();
		disableButtons();

	}
	private void disableButtons() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 4; i++){

			buttonFrontList[i].setEnabled(false);
			buttonBackList[i].setEnabled(false);
			imageButtonFrontList[i].setEnabled(false);
			imageButtonBackList[i].setEnabled(false);
			buttonFrontList[i].setTextColor(Color.RED);
			buttonBackList[i].setTextColor(Color.RED);

		}

	}
	private void enableButtons() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 4; i++){

			buttonFrontList[i].setEnabled(true);
			buttonBackList[i].setEnabled(true);
			imageButtonFrontList[i].setEnabled(true);
			imageButtonBackList[i].setEnabled(true);
			buttonFrontList[i].setTextColor(Color.BLACK);
			buttonBackList[i].setTextColor(Color.BLACK);

		}

	}

	private void resetSideBarColor() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 4; i++){
			frontBarLeft[i].setBackgroundColor(Color.WHITE);
			frontBarRight[i].setBackgroundColor(Color.WHITE);
			backBarLeft[i].setBackgroundColor(Color.WHITE);
			backBarRight[i].setBackgroundColor(Color.WHITE);
		}
	}

	private void setCardFront(){
		for(int i = 0; i < 4; i++){
			String content = cardList.get(i).getContent(Side.FRONT);
			if(cardList.get(i).getType(Side.FRONT) == Type.TEXT){
				frontList[i] = content;
			}
			else{
				Bitmap unscaledBitmap = convertFromJSONToImage(content);

				// Part 2: Scale image
				bmpFrontList[i] = ScalingUtilities.createScaledBitmap(unscaledBitmap, mDstWidth,
						mDstHeight, ScalingLogic.FIT);
				unscaledBitmap.recycle();

			}
		}

		setFrontCardsContent();
	}

	private void setCardBack(){
		for(int i = 0; i < 4; i++){
			String content = cardList.get(list.get(i)).getContent(Side.BACK);
			if(cardList.get(list.get(i)).getType(Side.BACK) == Type.TEXT){
				backList[i] = content;
			}
			else{
				Bitmap unscaledBitmap = convertFromJSONToImage(content);

				// Part 2: Scale image
				bmpBackList[i] = ScalingUtilities.createScaledBitmap(unscaledBitmap, mDstWidth,
						mDstHeight, ScalingLogic.FIT);
				unscaledBitmap.recycle();

			}
		}
		setBackCardsContent();
	}
	private void setCorrectBackBarColor(){
		backBarLeft[0].setBackgroundColor(Color.GREEN);
		backBarRight[0].setBackgroundColor(Color.GREEN);

		backBarLeft[1].setBackgroundColor(Color.BLUE);
		backBarRight[1].setBackgroundColor(Color.BLUE);

		backBarLeft[2].setBackgroundColor(Color.YELLOW);
		backBarRight[2].setBackgroundColor(Color.YELLOW);

		backBarLeft[3].setBackgroundColor(Color.GRAY);
		backBarRight[3].setBackgroundColor(Color.GRAY);
	}
	private void setCorrectCardBack(){
		for(int i = 0; i < 4; i++){
			String content = cardList.get(i).getContent(Side.BACK);
			if(cardList.get(i).getType(Side.BACK) == Type.TEXT){
				backList[i] = content;
			}
			else{
				Bitmap unscaledBitmap = convertFromJSONToImage(content);

				// Part 2: Scale image
				bmpBackList[i] = ScalingUtilities.createScaledBitmap(unscaledBitmap, mDstWidth,
						mDstHeight, ScalingLogic.FIT);
				unscaledBitmap.recycle();

			}
		}

		setBackCardsContent();
	}
	private void setBackCardsContent(){
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
			bmpBackList[i] = null;
		}
	}

	private void setFrontCardsContent(){
		//Display the cards' front on the game layout
		for(int i = 0; i < 4; i++){
			if(bmpFrontList[i] != null){
				imageButtonFrontList[i].setImageBitmap(bmpFrontList[i]);
				imageButtonFrontList[i].setVisibility(View.VISIBLE);
				buttonFrontList[i].setVisibility(View.GONE);
				frontList[i] = "";
			}
			else{
				buttonFrontList[i].setText(frontList[i]);
				buttonFrontList[i].setVisibility(View.VISIBLE);
				imageButtonFrontList[i].setVisibility(View.INVISIBLE);
			}
			bmpFrontList[i] = null;
		}

	}

	private void shuffle(){
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
