package icreate.fresco;

import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CardsFragment extends Fragment {
	
	Deck deck;
	Card currentCard;
	int cardID;
	
	private SqliteHelper database;
	
	TextView deckTextView;
	ImageButton addButton;
	ImageButton deleteButton;
	ImageButton editButton;
	ImageButton reviewButton;
	ImageButton returnButton;
	
	View rootLayout;
	LinearLayout cardFace;
	LinearLayout cardBack;
	
	public static CardsFragment createFragment(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.CARD_ID, index);
		
		CardsFragment fragment = new CardsFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = FrescoMain.getDatabase();
		
		deck = CardsViewPager.getDeck();
		cardID = getArguments().getInt(Constant.CARD_ID);
		currentCard = deck.getCard(cardID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_cards, container, false);
		
		initializeWidgets(view);
		setUpListener();
		setCardsView(view);
		
		View front = (View) view.findViewById(R.id.card_face);
		View back = (View) view.findViewById(R.id.card_back);
		
		front.setOnClickListener(cardListener);
		back.setOnClickListener(cardListener);
		
		return view;
	}

	private void setUpListener() {
		addButton.setOnClickListener(listener);
		deleteButton.setOnClickListener(listener);
		editButton.setOnClickListener(listener);
		reviewButton.setOnClickListener(listener);
		returnButton.setOnClickListener(listener);
	}

	private void initializeWidgets(View view) {
		
		deckTextView = (TextView) view.findViewById(R.id.deckTextView);
		addButton = (ImageButton) view.findViewById(R.id.addButton);
		deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
		editButton = (ImageButton) view.findViewById(R.id.editButton);
		reviewButton = (ImageButton) view.findViewById(R.id.reviewButton);
		returnButton = (ImageButton) view.findViewById(R.id.returnButton);
		
		deckTextView.setText(deck.getDeckName());
	}
	
	private void setCardsView(View view) {
		rootLayout = (View) view.findViewById(R.id.main_activity_root);
		cardFace = (LinearLayout) view.findViewById(R.id.card_face);
	    cardBack = (LinearLayout) view.findViewById(R.id.card_back);
	    
	    View front = setUpTextImageView(currentCard.getContent(Side.FRONT), currentCard.getType(Side.FRONT));
	    View back = setUpTextImageView(currentCard.getContent(Side.BACK), currentCard.getType(Side.BACK));
	    
	    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
	    cardFace.addView(front, lp);
	    cardBack.addView(back, lp);
	}
	
	public void flipCard() {	 
	    FlippingAnimation flipAnimation = new FlippingAnimation(cardFace, cardBack);
	 
	    if (cardFace.getVisibility() == View.GONE)
	    {
	        flipAnimation.reverse();
	    }
	    rootLayout.startAnimation(flipAnimation);
	}
	
	private View setUpTextImageView(String content, Type type) {
		
		View view = null;
		
		switch(type) {
			case TEXT:
				view = addText(content);
				break;
				
			case IMAGE:
				view = addImage(content);
				break;
				
			case DOODLE:
				view = addDoodle(content);
				break;
		}
		
		return view;
	}
	
	private View addText(String text) {
		TextView textView = new TextView(this.getActivity());
		textView.setText(text);
		textView.setTextSize(25);
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER);
		
		return textView;
	}
	
	private View addImage(String path) {
		ImageView image = new ImageView(this.getActivity());
		
		File imgFile = new  File(path);
		if(imgFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			image.setImageBitmap(bitmap);
			return image;
		}
		
		return null; 
	}
	
	private View addDoodle(String jsonString) {
		
		ImageView doodle = new ImageView(this.getActivity()); 
		Bitmap bitmap = convertFromJSONToImage(jsonString);
		
		if(bitmap != null) {
			doodle.setImageBitmap(bitmap);
			return doodle;
		}
		
		return null;
	}
	
	private Bitmap convertFromJSONToImage(String jsonString) {
		try {
	        byte[] encodeByte = Base64.decode(jsonString, Base64.DEFAULT);
	        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
	                encodeByte.length);
	        return bitmap;
		} catch (Exception e) {
	        e.getMessage(); //TODO: send to log
	        return null;
		}
	}
	
	private int getType(Card currentCard, Side side) {
		switch(currentCard.getType(side)) {
			case TEXT:
				return 0;
			case IMAGE:
				return 1;
			case DOODLE:
				return 2;
		}
		
		return 0;
	}
	
	private OnClickListener cardListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			flipCard();
		}
		
	};
	
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.addButton:
					addCard();
					break;
					
				case R.id.deleteButton:
					deleteCard(currentCard);
					break;
					
				case R.id.editButton:
					editCard(currentCard);
					break;
					
				case R.id.reviewButton:
					reviewDeck();
					break;
					
				case R.id.returnButton:
					returnToFrescoMain();
			
			}
		}};
	
	private void addCard() {
		Intent intent = new Intent(getActivity(), AddEditActivity.class);
		intent.putExtra(Constant.NEW_EDIT, false);
		intent.putExtra(Constant.DECK_NAME, deck.getDeckName());
		intent.putExtra(Constant.DECK_ID, deck.getDeckID());
		intent.putExtra(Constant.CARD_ID, -1);
		
		startActivity(intent);
	}
	
	private void deleteCard(Card currentCard) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		DialogInterface.OnClickListener popListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
					case Dialog.BUTTON_POSITIVE:
						database.deleteCard(cardID);
						Intent i = new Intent(getActivity(), CardsViewPager.class);
						i.putExtra(Constant.DECK_ID, deck.getDeckID());
						i.putExtra(Constant.DECK_NAME, deck.getDeckName());
						startActivity(i);
						break;
					case Dialog.BUTTON_NEGATIVE:
						dialog.cancel();
						break;
				}
			}
		};
		
		
		builder.setMessage("Are you sure you want to delete this card");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes" , popListener);
		builder.setNegativeButton("No", popListener);
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void editCard(Card currentCard) {
		Intent intent = new Intent(getActivity(), AddEditActivity.class);
		intent.putExtra(Constant.NEW_EDIT, true);
		intent.putExtra(Constant.DECK_NAME, deck.getDeckName());
		intent.putExtra(Constant.DECK_ID, deck.getDeckID());
		intent.putExtra(Constant.CARD_ID, cardID);
		
		startActivity(intent);
	}
	
	private void reviewDeck() {
		//TODO
	}
	
	private void returnToFrescoMain() {
		Intent i = new Intent(getActivity(), FrescoMain.class);
		startActivity(i);
	}

}
