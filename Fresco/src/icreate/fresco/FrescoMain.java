package icreate.fresco;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.AlertDialog;

public class FrescoMain extends ListActivity {
	private ArrayList<Deck> listDeck = new ArrayList<Deck>();

	public static final int color[][] = {
		{142, 68 , 173},
		{192, 57, 43},
		{41, 128, 185},
		{211, 84, 0},
		{44, 62, 80},
		{230, 126, 34}}; 
	public static final String icon[]={"people", "hammer", "cat", "number", "german", "film", "talk", "key", "home",
		"book", "picture", "christmas", "newspaper",  "basketball", "musics", "hamburger"};
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


		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {

				Intent intent = new Intent(v.getContext(), EditDeck.class);
				Deck deck = listDeck.get(position);
				intent.putExtra(Constant.DECK_ID, deck.getDeckID());
				intent.putExtra(Constant.DECK_NAME, deck.getDeckName());
				intent.putExtra(Constant.POSITION_COLOR, position);
				startActivityForResult(intent, 2);
				return true;
			}	
		});



		viewParts = new Runnable(){
			public void run(){
				handler.sendEmptyMessage(0);
			}
		};
		Thread thread =  new Thread(null, viewParts, "MagentoBackground");
		thread.start();
	}
	@SuppressLint("HandlerLeak")
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
	/*
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

	}*/
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, CardsViewPager.class);
		Deck deck = listDeck.get(position);
		intent.putExtra(Constant.DECK_ID, deck.getDeckID());
		intent.putExtra(Constant.POSITION_COLOR, position);
		intent.putExtra(Constant.DECK_NAME, deck.getDeckName());
		startActivityForResult(intent, 1);

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
		if(requestCode == 1){
			if(resultCode == RESULT_OK){

				Deck tmp = new Deck(data.getStringExtra("deck"));
				database.insertDeck(data.getStringExtra("deck"));
				int id = getResources().getIdentifier(data.getStringExtra("icon"),"drawable",getPackageName());
				((ImageView)findViewById(R.id.category)).setImageResource(id);
				listDeck.add(tmp);
				m_adapter.notifyDataSetChanged();

			}
			else{

			}
		}
		if(requestCode == 2){

			Deck tmp = listDeck.get(data.getIntExtra("position", 0));
			database.deleteDeck(tmp.getDeckID());
			listDeck.remove(tmp);

			if(resultCode == RESULT_OK){

				Deck deck = new Deck(data.getStringExtra("deck"));
				listDeck.add(deck);
				database.insertDeck(data.getStringExtra("deck"));
				m_adapter.notifyDataSetChanged();

			}
			else{

				m_adapter.notifyDataSetChanged();

			}
		}

	}
}
