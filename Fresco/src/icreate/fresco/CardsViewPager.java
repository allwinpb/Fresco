package icreate.fresco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

public class CardsViewPager extends FragmentActivity {

	private static Deck deck;
	private ViewPager viewPager;
	private SqliteHelper database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = FrescoMain.getDatabase();
		setContentView(R.layout.activity_cards);
		
		Intent intent = getIntent();
		String deckName = intent.getStringExtra(Constant.DECK_NAME);
		int deckID = intent.getIntExtra(Constant.DECK_ID, 0);
		
		deck = database.getDeck(deckID, deckName);
		
		FragmentManager manager = getSupportFragmentManager();
		
		if(deck.getCardCount() == 0){
			
			if(findViewById(R.id.FrameLayout1) != null) {
				NoCardsFragment fragment = NoCardsFragment.createFragment(deckID);
				manager.beginTransaction().add(R.id.FrameLayout1, fragment).commit();
			}
			
		} else {
		
			viewPager = new ViewPager(this);
			viewPager.setId(R.id.viewPager);
			
			this.setContentView(viewPager);
			
			viewPager.setAdapter(new FragmentStatePagerAdapter(manager){
	
				@Override
				public Fragment getItem(int position) {
					Card card = deck.get(position);
					return CardsFragment.createFragment(card.getCardID());
				}
	
				@Override
				public int getCount() {
					return deck.getCardCount();
				}
			});
			
			int cardId = getIntent().getIntExtra(Constant.CARD_ID, 0);
			
			for(int i=0; i<deck.getCardCount(); i++) {
				if(deck.get(i).getCardID() == cardId) {
					viewPager.setCurrentItem(i);
					setTitle("Note #" + i);
					break;
				}
			}
			
			viewPager.setOnPageChangeListener(new OnPageChangeListener(){
	
				@Override
				public void onPageScrollStateChanged(int arg0) {
					setTitle("Card #" + (arg0+1));
				}
	
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
					
				}
	
				@Override
				public void onPageSelected(int arg0) {
					
					
				}
				
			});
		}
	}	
	
	protected static Deck getDeck() {
		return deck;
	}
}
