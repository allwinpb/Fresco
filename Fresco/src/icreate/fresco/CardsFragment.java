package icreate.fresco;

import icreate.animation.FlippingAnimation;
import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;
import android.app.ActionBar;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class CardsFragment extends Fragment {
	
	Deck deck;
	Card currentCard;
	int cardID;
	
	int index;
	int cardsCount;
	
	private SqliteHelper database;
	
	private TextView frontBackTextView;
	private TextView indexTextView;
	
	private ImageButton imageButtonBack;
	private ImageButton imageButtonTo;
	
	boolean isFrontCard = true;
	
	View rootLayout;
	LinearLayout cardFace;
	LinearLayout cardBack;
	
	public static CardsFragment createFragment(int cardId, int index, int cardsCount) {
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.CARD_ID, cardId);
		bundle.putInt(Constant.INDEX, index);
		bundle.putInt(Constant.NUMBER_OF_CARDS, cardsCount);
		
		CardsFragment fragment = new CardsFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = FrescoMain.getDatabase();
		Log.d("CardsFragment", "onCreate");
		deck = CardsViewPager.getDeck();
		cardID = getArguments().getInt(Constant.CARD_ID);
		currentCard = deck.getCard(cardID);
		index = getArguments().getInt(Constant.INDEX);
		cardsCount = getArguments().getInt(Constant.NUMBER_OF_CARDS);
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("CardsFragment", "onCreateView");
		View view = inflater.inflate(R.layout.fragment_cards, container, false);
		
		setCardsView(view);
		
		View front = (View) view.findViewById(R.id.card_face);
		View back = (View) view.findViewById(R.id.card_back);
		
		front.setOnClickListener(cardListener);
		back.setOnClickListener(cardListener);
		front.setOnLongClickListener(longClickListener);
		back.setOnLongClickListener(longClickListener);
		
		setBackAndForwardArrow(view);
		
		frontBackTextView = (TextView) view.findViewById(R.id.frontBackTextView);
		indexTextView = (TextView) view.findViewById(R.id.indexTextView);
		
		frontBackTextView.setText("Front");
		indexTextView.setText(String.format("%d of %d", (index+1), cardsCount));
		
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(deck.getDeckName());
		int id = getResources().getIdentifier(deck.getDeckIcon(), "drawable", getActivity().getPackageName());
		actionBar.setIcon(id);
		ImageView homeIcon = (ImageView) getActivity().findViewById(android.R.id.home);
		homeIcon.setPadding(15, 0, 15, 0);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		view.setBackgroundColor(((CardsViewPager)getActivity()).getColor());
		
		return view;
	}
	
	private void setBackAndForwardArrow(View view) {
		imageButtonBack = (ImageButton) view.findViewById(R.id.imageButtonBack);
		imageButtonTo = (ImageButton) view.findViewById(R.id.imageButtonTo);
		Log.d("CardsFragment", "setBackAndForwardArrow");
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch(v.getId()) {
					case R.id.imageButtonBack:
						((CardsViewPager) getActivity()).setCurrentItem(index-1);
						break;
					case R.id.imageButtonTo:
						((CardsViewPager) getActivity()).setCurrentItem(index+1);
						break;
					default:
						break;
				}
			}
		};
		
		if(index >= 1) {
			imageButtonBack.setVisibility(View.VISIBLE);
			imageButtonBack.setOnClickListener(listener);
		}
		
		if(index < cardsCount - 1) {
			imageButtonTo.setVisibility(View.VISIBLE);
			imageButtonTo.setOnClickListener(listener);
		}
	}

	private void setCardsView(View view) {
		Log.d("CardsFragment", "setCardsView");
		rootLayout = (View) view.findViewById(R.id.main_activity_root);
		cardFace = (LinearLayout) view.findViewById(R.id.card_face);
	    cardBack = (LinearLayout) view.findViewById(R.id.card_back);
	    
	    View front = setUpTextImageView(currentCard.getContent(Side.FRONT), currentCard.getType(Side.FRONT));
	    front.setPadding(15, 15, 15, 15);
	    View back = setUpTextImageView(currentCard.getContent(Side.BACK), currentCard.getType(Side.BACK));
	    back.setPadding(15, 15, 15, 15);
	    
	    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
	    cardFace.addView(front, lp);
	    cardBack.addView(back, lp);
	}
	
	public void flipCard() {	 
	    FlippingAnimation flipAnimation = new FlippingAnimation(cardFace, cardBack);
	    isFrontCard = !isFrontCard;
	 
	    if (cardFace.getVisibility() == View.GONE)
	    {
	        flipAnimation.reverse();
	    }
	    rootLayout.startAnimation(flipAnimation);
	    
	    if(isFrontCard == true) {
	    	frontBackTextView.setText("Front");
	    } else {
	    	frontBackTextView.setText("Back");
	    }
	}
	
	private View setUpTextImageView(String content, Type type) {
		Log.d("CardsFragment", "setUpTextImageView");
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
				
			case CAMERA:
				view = addImage(content);
				break;
		}
		
		return view;
	}
	
	private View addText(String text) {
		Log.d("CardsFragment", "addText");
		TextView textView = new TextView(this.getActivity());
		textView.setText(text);
		
		if(text.length() <= 50)
			textView.setTextSize(25);
		else if(text.length() <= 150)
			textView.setTextSize(20);
		else 
			textView.setTextSize(15);
		
		textView.setPadding(10, 10, 10, 10);
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		
		return textView;
	}
	
	/*private View addImage(String path) {
		ImageView image = new ImageView(this.getActivity());
		
		File imgFile = new  File(path);
		if(imgFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			image.setImageBitmap(bitmap);
			return image;
		}
		
		return null; 
	}*/

	private View addImage(String jsonString) {
		Log.d("CardsFragment", "addImage");
		ImageView doodle = new ImageView(this.getActivity()); 
		Bitmap bitmap = convertFromJSONToImage(jsonString);
		
		if(bitmap != null) {
			doodle.setImageBitmap(bitmap);
			return doodle;
		}
		
		return null;
	}
	
	/*
	private View addDoodle(String json) {
		JSONObject object = null;
		
		try {
			object = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String jsonString = null;
		
		try {
			jsonString = object.getString(Constant.JSON_BITMAP);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		ImageView doodle = new ImageView(this.getActivity()); 
		Bitmap bitmap = convertFromJSONToImage(jsonString);
		
		if(bitmap != null) {
			doodle.setImageBitmap(bitmap);
			return doodle;
		}
		
		return null;
	}*/
	
	private View addDoodle(String jsonString) {
		Log.d("CardsFragment", "addDoodle");
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
	        e.getMessage(); 
	        return null;
		}
	}	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_cards, menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.add_icon:
				addCard();
				return true;
			case R.id.delete_icon:
				deleteCard(currentCard);
				return true;
			case R.id.edit_icon:
				editCard(currentCard);
				return true;
			case R.id.review_icon:
				reviewDeck();
				return true;
			case android.R.id.home:
				returnBack();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private OnClickListener cardListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			flipCard();
		}
		
	};
	
	private OnLongClickListener longClickListener = new OnLongClickListener(){

		@Override
		public boolean onLongClick(View v) {
			
			Intent intent = new Intent(getActivity(), AddEditActivity.class);
			intent.putExtra(Constant.NEW_EDIT, true);
			intent.putExtra(Constant.DECK_ID, deck.getDeckID());
			intent.putExtra(Constant.CARD_ID, cardID);
			intent.putExtra(Constant.INDEX, index);
			intent.putExtra(Constant.POSITION_COLOR, ((CardsViewPager)getActivity()).getPositionColor());
			intent.putExtra(Constant.SIDE, isFrontCard);
			
			return false;
		}
	};
	
	private void addCard() {
		Intent intent = new Intent(getActivity(), AddEditActivity.class);
		intent.putExtra(Constant.NEW_EDIT, false);
		intent.putExtra(Constant.DECK_ID, deck.getDeckID());
		intent.putExtra(Constant.CARD_ID, -1);
		intent.putExtra(Constant.INDEX, cardsCount);
		intent.putExtra(Constant.POSITION_COLOR, ((CardsViewPager)getActivity()).getPositionColor());
		intent.putExtra(Constant.SIDE, true);
		startActivity(intent);
		//getActivity().finish();
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
						i.putExtra(Constant.POSITION_COLOR, ((CardsViewPager)getActivity()).getPositionColor());
						startActivity(i);
						break;
					case Dialog.BUTTON_NEGATIVE:
						dialog.cancel();
						break;
				}
			}
		};
		
		builder
		.setTitle("Delete confirmation")
		.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("Are you sure you want to delete this card?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes" , popListener);
		builder.setNegativeButton("No", popListener);
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void editCard(Card currentCard) {
		Intent intent = new Intent(getActivity(), AddEditActivity.class);
		intent.putExtra(Constant.NEW_EDIT, true);
		intent.putExtra(Constant.DECK_ID, deck.getDeckID());
		intent.putExtra(Constant.CARD_ID, cardID);
		intent.putExtra(Constant.INDEX, index);
		intent.putExtra(Constant.SIDE, isFrontCard);
		intent.putExtra(Constant.POSITION_COLOR, ((CardsViewPager)getActivity()).getPositionColor());
		
		startActivity(intent);
		//getActivity().finish();
	}
	
	private void reviewDeck() {
		//TODO
	}
	
	private void returnBack() {
		Intent intent = new Intent(getActivity(), FrescoMain.class);
		startActivity(intent);
		getActivity().finish();
	}

}
