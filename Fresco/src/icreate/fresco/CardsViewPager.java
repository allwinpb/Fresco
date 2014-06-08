package icreate.fresco;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class CardsViewPager extends FragmentActivity {

	Deck deck;
	ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		viewPager = new ViewPager(this);
		viewPager.setId(R.id.viewPager);
		
		this.setContentView(viewPager);
		
		deck = new Deck("oh la la"); //TODO: get deck from database
		
		FragmentManager manager = getSupportFragmentManager();
		
		viewPager.setAdapter(new FragmentStatePagerAdapter(manager){

			@Override
			public Fragment getItem(int position) {
				Card card = deck.get(position);
				return CardsActivity.createFragment(position);
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
	
	
}
