package icreate.fresco;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MatchingGame extends Activity {
	private ImageButton imageButtonFront1, imageButtonFront2, imageButtonFront3, imageButtonFront4;
	private ImageButton imageButtonBack1, imageButtonBack2, imageButtonBack3, imageButtonBack4;
	
	private Button buttonFront1, buttonFront2, buttonFront3, buttonFront4;
	private Button buttonBack1, buttonBack2, buttonBack3, buttonBack4;
	
	private Bitmap bmpFront1 = null, bmpFront2 = null, bmpFront3 = null, bmpFront4 = null; 
	private Bitmap bmpBack1 = null, bmpBack2 = null, bmpBack3 = null, bmpBack4 = null;
	
	private String front1 = "", front2 = "", front3 = "", front4 = "";
	private String back1 = "", back2 = "", back3 = "", back4 = "";
	
	
	
	private Card one, two, three, four;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		initializingImageButtons();
		initializingButtons();
	}
	private void initializingButtons() {
		// TODO Auto-generated method stub
		buttonFront1 = (Button)findViewById(R.id.cardFront1);
		buttonFront2 = (Button)findViewById(R.id.cardFront2);
		buttonFront3 = (Button)findViewById(R.id.cardFront3);
		buttonFront4 = (Button)findViewById(R.id.cardFront4);
		
		buttonBack1  = (Button)findViewById(R.id.cardBack1);
		buttonBack2  = (Button)findViewById(R.id.cardBack2);
		buttonBack3  = (Button)findViewById(R.id.cardBack3);
		buttonBack4  = (Button)findViewById(R.id.cardBack4);
	}
	private void initializingImageButtons() {
		// TODO Auto-generated method stub
		imageButtonFront1 = (ImageButton)findViewById(R.id.cardImageFront1);
		imageButtonFront2 = (ImageButton)findViewById(R.id.cardImageFront2);
		imageButtonFront3 = (ImageButton)findViewById(R.id.cardImageFront3);
		imageButtonFront4 = (ImageButton)findViewById(R.id.cardImageFront4);
		
		imageButtonBack1  = (ImageButton)findViewById(R.id.cardImageBack1);
		imageButtonBack2  = (ImageButton)findViewById(R.id.cardImageBack2);
		imageButtonBack3  = (ImageButton)findViewById(R.id.cardImageBack3);
		imageButtonBack4  = (ImageButton)findViewById(R.id.cardImageBack4);
	}
	public void getCards(){
		//Get random four cards from all the decks
		
		
	}
	public void getCardFromADeck(Deck deck){
		//Get four cards randomly front a specific deck
		
		
	}
	
	public int matchCards(){
		
		//match the card's fronts and back and give the results out of 4
		
		return 0;
	}
	
	public void setCardBack(){
		//Display the cards' back on the game layout
		if(bmpBack1 != null)
			imageButtonBack1.setImageBitmap(bmpBack1);
		if(bmpBack2 != null)
			imageButtonBack2.setImageBitmap(bmpBack2);
		if(bmpBack3 != null)
			imageButtonBack3.setImageBitmap(bmpBack3);
		if(bmpBack4 != null)
			imageButtonBack4.setImageBitmap(bmpBack4);
		
		if(back1 != "")
			buttonBack1.setText(back1);
		if(back2 != "")
			buttonBack2.setText(back2);
		if(back3 != "")
			buttonBack3.setText(back3);
		if(back4 != "")
			buttonBack4.setText(back4);
		
	}
	
	public void setCardFront(){
		//Display the cards' front on the game layout
		if(bmpFront1 != null)
			imageButtonFront1.setImageBitmap(bmpFront1);
		if(bmpFront2 != null)
			imageButtonFront2.setImageBitmap(bmpFront2);
		if(bmpFront3 != null)
			imageButtonFront3.setImageBitmap(bmpFront3);
		if(bmpFront4 != null)
			imageButtonFront4.setImageBitmap(bmpFront4);
		
		if(front1 != "")
			buttonFront1.setText(front1);
		if(front2 != "")
			buttonFront2.setText(front2);
		if(front3 != "")
			buttonFront3.setText(front3);
		if(front4 != "")
			buttonFront4.setText(front4);
			
	}
	
	public void shuffle(){
		//Shuffle the four cards' back randomly
		
		
		
	}

}
