package icreate.fresco;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class NoCardsFragment extends Fragment{

	int deckID;
	TextView deckNameTextView;
	ImageButton addButton;
	
	String deckName;
	
	public static NoCardsFragment createFragment(int deckID) {
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.DECK_ID, deckID);
		bundle.putString(Constant.DECK_NAME, CardsViewPager.getDeck().getDeckName());
		
		NoCardsFragment fragment = new NoCardsFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		deckID = this.getArguments().getInt(Constant.DECK_ID);
		deckName = this.getArguments().getString(Constant.DECK_NAME);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.no_cards_in_deck, container, false);
		final Deck deck = CardsViewPager.getDeck();
		
		OnClickListener listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				switch(v.getId()) {
				
					case R.id.addButton:
						
						Intent newCardIntent = new Intent(getActivity(), AddEditActivity.class);
						newCardIntent.putExtra(Constant.NEW_EDIT, false);
						newCardIntent.putExtra(Constant.DECK_ID, deck.getDeckID());
						newCardIntent.putExtra(Constant.CARD_ID, -1);
						newCardIntent.putExtra(Constant.DECK_NAME, deck.getDeckName());
						newCardIntent.putExtra(Constant.POSITION_COLOR, ((CardsViewPager)getActivity()).getPositionColor());
						
						startActivity(newCardIntent);
						getActivity().finish();
						break;
						
				}
			}
			
		};
		
		addButton = (ImageButton) view.findViewById(R.id.addButton);
		addButton.setOnClickListener(listener);
		
		
		deckNameTextView = (TextView) view.findViewById(R.id.deckNameTextView);
		String text = deck.getDeckName() + " " + getResources().getString(R.string.no_cards);
		deckNameTextView.setText(text);
		
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(deck.getDeckName());
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		view.setBackgroundColor(((CardsViewPager)getActivity()).getColor());
		
		return view;
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
		Intent intent = new Intent(getActivity(), FrescoMain.class);
		startActivity(intent);
	}
}
