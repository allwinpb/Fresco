package icreate.fresco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class CardsViewPager extends FragmentActivity {

	private static Deck deck;
	private ViewPager viewPager;
	private SqliteHelper database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = FrescoMain.getDatabase();
		
		
		Intent intent = getIntent();
		String deckName = intent.getStringExtra(Constant.DECK_NAME);
		int deckID = intent.getIntExtra(Constant.DECK_ID, -1);
		
		viewPager = new ViewPager(this);
		viewPager.setId(R.id.viewPager);
		
		this.setContentView(viewPager);
		
		deck = database.getDeck(deckID, deckName);
		
		FragmentManager manager = getSupportFragmentManager();
		
		viewPager.setAdapter(new FragmentStatePagerAdapter(manager){

			@Override
			public Fragment getItem(int position) {
				Card card = deck.get(position);
				return CardsActivity.createFragment(card.getCardID());
			}

			@Override
			public int getCount() {
				return deck.getCardCount();
			}
		});
		
		viewPager.setCurrentItem(0);
		setTitle("Card #1");
		
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
	
	public static Deck getDeck() {
		return deck;
	}
}
