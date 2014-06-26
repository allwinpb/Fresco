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
import android.widget.ImageView;
import android.widget.TextView;

public class NoCardsFragment extends Fragment{

	TextView deckNameTextView;
	ImageButton addButton;
	
	Deck deck;
	
	String deckName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.no_cards_in_deck, container, false);
		deck = CardsViewPager.getDeck();
		
		OnClickListener listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				switch(v.getId()) {
				
					case R.id.addButton:
						
						Intent newCardIntent = new Intent(getActivity(), AddEditActivity.class);
						newCardIntent.putExtra(Constant.NEW_EDIT, false);
						newCardIntent.putExtra(Constant.DECK_ID, deck.getDeckID());
						newCardIntent.putExtra(Constant.CARD_ID, -1);
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
		
		view.setBackgroundColor(((CardsViewPager)getActivity()).getColor());
		
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(deck.getDeckName());
		int id = getResources().getIdentifier(deck.getDeckIcon(), "drawable", getActivity().getPackageName());
		actionBar.setIcon(id);
		ImageView homeIcon = (ImageView) getActivity().findViewById(android.R.id.home);
		homeIcon.setPadding(15, 0, 15, 0);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
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
