package icreate.fresco;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class CardsViewPager extends FragmentActivity {

	private static Deck deck;
	private ViewPager viewPager;
	private SqliteHelper database;
	
	private int index;
	private int positionColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cards);
		database = FrescoMain.getDatabase();
		
		Intent intent = getIntent();
		int deckID = intent.getIntExtra(Constant.DECK_ID, 0);
		index = intent.getIntExtra(Constant.INDEX, -1);
		positionColor = intent.getIntExtra(Constant.POSITION_COLOR, 0);
		
		deck = database.getCards(deckID);
		Log.d("CardsViewPager", deck.getDeckIcon());
		
		FragmentManager manager = getSupportFragmentManager();
		
		if(deck.getCardCount() == 0){
			
			if(findViewById(R.id.FrameLayout1) != null) {
				NoCardsFragment fragment = new NoCardsFragment();
				
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
					return CardsFragment.createFragment(card.getCardID(), position, deck.getCardCount());
				}
	
				@Override
				public int getCount() {
					return deck.getCardCount();
				}
			});
			
			viewPager.setOnPageChangeListener(new OnPageChangeListener(){
	
				@Override
				public void onPageScrollStateChanged(int arg0) {
					
				}
	
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
					
				}
	
				@Override
				public void onPageSelected(int arg0) {
					
					
				}
				
			});
			
			if(index != -1) {
				viewPager.setCurrentItem(index);
			}
		}
	}	
	
	public int getColor() {
		return Color.argb(255, Constant.color[positionColor%Constant.COLOR_NUMBER][0], Constant.color[positionColor%Constant.COLOR_NUMBER][1], Constant.color[positionColor%Constant.COLOR_NUMBER][2]);
	}
	
	public int getPositionColor() {
		return positionColor;
	}
	
	protected static Deck getDeck() {
		return deck;
	}
	
	public void setCurrentItem(int index) {
		viewPager.setCurrentItem(index, true);
	}
}
