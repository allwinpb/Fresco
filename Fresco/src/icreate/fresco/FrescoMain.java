package icreate.fresco;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Message;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
public class FrescoMain extends ListActivity {
	private ArrayList<Deck> listDeck = new ArrayList<Deck>();
	//private ArrayAdapter<String> adapter;
	//private static ArrayList<Deck> listDeck;
	private Runnable viewParts;
	private ItemAdapter m_adapter;
	private static SqliteHelper database;
	MenuItem searchItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);;
		database = new SqliteHelper(this);
		listDeck = database.getDecks();
		m_adapter = new ItemAdapter(this, R.layout.list_deck, listDeck);
		setListAdapter(m_adapter);
		viewParts = new Runnable(){
			public void run(){
				handler.sendEmptyMessage(0);
			}
		};
		Thread thread =  new Thread(null, viewParts, "MagentoBackground");
        thread.start();
	}
	 private Handler handler = new Handler()
	 {
		public void handleMessage(Message msg)
		{
			// create some objects
			// here is where you could also request data from a server
			// and then create objects from that data.
			//createDeckList();
			m_adapter = new ItemAdapter(FrescoMain.this, R.layout.list_deck, listDeck);

			// display the list.
	        setListAdapter(m_adapter);
		}
	};
	private void createDeckList() {
		// TODO Auto-generated method stub
		Card card = new Card();
		card._frontContent = "Front One";
		card._backContent = "Back One";
		Deck deck = new Deck("one");

		deck._cards.add(card);
		listDeck.add(deck);
		Card card1 = new Card();
		card._frontContent = "Front Two";
		card._backContent = "Back Two";
		Deck deck1 = new Deck("two");
		deck1._cards.add(card1);
		listDeck.add(deck1);
		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, CardsViewPager.class);
		Deck deck = listDeck.get(position);
		intent.putExtra(Constant.DECK_ID, deck.getDeckID());
		intent.putExtra(Constant.DECK_NAME, deck.getDeckName());
		startActivity(intent);
	}
	
    public static SqliteHelper getDatabase() {
    	return database;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
    	searchItem = menu.findItem(R.id.search_icon);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
		case R.id.add_icon:
			Intent i = new Intent(this, AddDeck.class);
			startActivityForResult(i, 1);
			break;
		case R.id.search_icon:
			searchItem.getActionView();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Deck tmp = new Deck(data.getStringExtra("category"));
		database.insertDeck(data.getStringExtra("category"));
		listDeck.add(tmp);
		m_adapter.notifyDataSetChanged();

	}
	/**
	 * A placeholder fragment containing a simple view.

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fresco_main, container, false);
            return rootView;
        }
    }
	 */
}
