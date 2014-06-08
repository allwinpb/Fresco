package icreate.fresco;

import icreate.fresco.Card.Side;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;

public class CardsActivity extends Fragment {
	
	Deck deck;
	Card currentCard;
	int index;
	
	TextView deckTextView;
	TextView lastUpdatedTextView;
	Button addButton;
	Button deleteButton;
	Button editButton;
	Button reviewButton;
	
	public static CardsActivity createFragment(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.CARD_ID, index);
		
		CardsActivity fragment = new CardsActivity();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		deck = new Deck("oh la la"); //TODO: get deck from database by its deck id
		index = getArguments().getInt(Constant.CARD_ID);
		currentCard = deck.get(index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_cards, container, false);
		
		initializeWidgets(view);
		setUpListener();
		setUpFlippingAnimation();
		
		return view;
	}

	private void setUpListener() {
		addButton.setOnClickListener(listener);
		deleteButton.setOnClickListener(listener);
		editButton.setOnClickListener(listener);
		reviewButton.setOnClickListener(listener);
	}

	private void initializeWidgets(View view) {
		
		deckTextView = (TextView) view.findViewById(R.id.deckTextView);
		lastUpdatedTextView = (TextView) view.findViewById(R.id.lastUpdatedTextView);
		addButton = (Button) view.findViewById(R.id.addButton);
		deleteButton = (Button) view.findViewById(R.id.deleteButton);
		editButton = (Button) view.findViewById(R.id.editButton);
		reviewButton = (Button) view.findViewById(R.id.reviewButton);
		
		deckTextView.setText(deck.getName());
		//TODO: lastupdated
	}
	
	private void setUpFlippingAnimation() {
		final ViewAnimator viewAnimator1 = (ViewAnimator)getActivity().findViewById(R.id.viewFlipper1);
		
		 if (getActivity().findViewById(R.id.viewFlipper1) != null) {

			String frontContent = currentCard.getContent(Side.FRONT);
            Fragment frontFragment = TextImageFragment.createFragment(frontContent, getType(currentCard, Side.FRONT));
            
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.viewFlipper1, frontFragment).commit();
            
            String backContent = currentCard.getContent(Side.BACK);
            Fragment backFragment = TextImageFragment.createFragment(backContent, getType(currentCard, Side.BACK));
            
            getActivity().getSupportFragmentManager().beginTransaction()
            	.add(R.id.viewFlipper1, backFragment).commit();
	        
	    }
		
		viewAnimator1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				AnimationFactory.flipTransition(viewAnimator1, FlipDirection.LEFT_RIGHT);
			}
		});
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
			
			}
		}};
	
	private void addCard() {
		//TODO
	}
	
	private void deleteCard(Card currentCard) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		DialogInterface.OnClickListener popListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
					case Dialog.BUTTON_POSITIVE:
						//TODO: delete that shit
						dialog.cancel();
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
		//TODO
	}
	
	private void reviewDeck() {
		//TODO
	}

}
