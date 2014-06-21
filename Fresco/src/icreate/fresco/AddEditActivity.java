package icreate.fresco;

import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class AddEditActivity extends FragmentActivity implements OnTabChangeListener {
	
	public static final int GREEN  = 0xFF27AE60;
	public static final int ORANGE = 0xFFD35400;

	public static final String FRONT = "Front";
	public static final String BACK = "Back";

	private int deckID;
	private Card card;
	private boolean newEdit;
	private String deckName;
	
	private FrontBackCardFragment frontFragment;
	private FrontBackCardFragment backFragment;

	private SqliteHelper database;

	private Type cardFrontType = Type.TEXT;
	private String cardFrontString = "";
	private Type cardBackType = Type.TEXT;
	private String cardBackString  = "";

	private Side side = Side.FRONT;
	private TabHost tabHost;

	public void setContent(String content) {
		switch(side) {
		case FRONT:
			cardFrontString = content;
			break;
		case BACK:
			cardBackString = content;
			break;
		}
	}

	public String getContent(Side side) {
		switch(side) {
		case FRONT:
			return cardFrontString;
		case BACK:
			return cardBackString;
		}
		return cardFrontString;
	}

	public void setType(Type type) {
		switch(side) {
		case FRONT:
			cardFrontType = type;
			break;
		case BACK:
			cardBackType = type;
			break;
		}
	}

	public Type getType(Side side) {
		switch(side) {
		case FRONT:
			return cardFrontType;
		case BACK:
			return cardBackType;
		}
		return cardFrontType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit);

		database = FrescoMain.getDatabase();

		Intent receiveIntent = getIntent();
		newEdit = receiveIntent.getBooleanExtra(Constant.NEW_EDIT, false);
		deckName = receiveIntent.getStringExtra(Constant.DECK_NAME);
		deckID  = receiveIntent.getIntExtra(Constant.DECK_ID, 1);
		if( newEdit == true ) {
			int cardID = receiveIntent.getIntExtra(Constant.CARD_ID, 1);
			card = database.getCard(deckID, cardID);
		} else {
			card = new Card();
		} 

		initializeContent();
		initializeTabHost();
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(deckName);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}

	private void initializeContent() {
		cardFrontType = card.getType(Side.FRONT);
		cardFrontString = card.getContent(Side.FRONT);
		cardBackType = card.getType(Side.BACK);
		cardBackString  = card.getContent(Side.BACK);
	}

	private void initializeTabHost() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		tabHost.addTab(newTab(FRONT, FRONT, R.id.tab_front));
		tabHost.addTab(newTab(BACK, BACK, R.id.tab_back));

		updateTabs(FRONT);
		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(this);
	}

	private void updateTabs(String tag) {
		FragmentManager fm = getSupportFragmentManager();
		switch(tag) {
		case FRONT:
			frontFragment = FrontBackCardFragment.createFragment(cardFrontString, getIntType(cardFrontType));
			fm.beginTransaction()
			.replace(R.id.tab_front, frontFragment, FRONT)
			.commit();
			break;
		case BACK:
			backFragment = FrontBackCardFragment.createFragment(cardBackString, getIntType(cardBackType));
			fm.beginTransaction()
			.replace(R.id.tab_back, backFragment, BACK)
			.commit();
			break;
		}
		changeTabColor();
	}

	private TabSpec newTab(String tag, String tagLabel, int contentId) {
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(tagLabel);
		tabSpec.setContent(contentId);
		return tabSpec;
	
	}

	private int getIntType(Type type) {
		switch(type) {
		case TEXT:
			return 0;
		case DOODLE:
			return 1;
		case IMAGE:
			return 2;
		case CAMERA:
			return 3;
		}

		return 0;
	}

	@Override
	public void onTabChanged(String tabId) {
		switch(tabId) {
		case FRONT:
			side = Side.FRONT;
			updateTabs(FRONT);
			break;
		case BACK:
			side = Side.BACK;
			updateTabs(BACK);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_edit_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.done_icon:
				confirmSaving();
				return true;
			
			case android.R.id.home:
				confirmLeaving();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void confirmLeaving() {
		AlertDialog.Builder exitDialog = new AlertDialog.Builder(AddEditActivity.this);

		exitDialog
		.setTitle("Exit Confirmation")
		.setCancelable(true)
		.setMessage("Changes not saved will be discarded")
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				Intent sendIntent = new Intent(AddEditActivity.this, CardsViewPager.class);
				sendIntent.putExtra(Constant.DECK_NAME, deckName);
				sendIntent.putExtra(Constant.DECK_ID, deckID);
				startActivity(sendIntent);
				finish();
			}
		});
		exitDialog
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setIcon(android.R.drawable.ic_dialog_alert);

		AlertDialog dialog = exitDialog.create();
		dialog.show();
	}
	
	private void confirmSaving() {
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(AddEditActivity.this);

		saveDialog
		.setTitle("Save confirmation")
		.setMessage("Changes not saved will be discarded")
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				saveCardType();
				card.setType(Side.FRONT, cardFrontType);
				card.setType(Side.BACK, cardBackType);
				saveCardContent();
				card.setContent(Side.FRONT, cardFrontString);
				card.setContent(Side.BACK, cardBackString);

				database = FrescoMain.getDatabase();
				if(newEdit == false) {
					database.insertCard(deckID, card);
				} else {
					database.updateCard(deckID, card);
				}

				Intent sendIntent = new Intent(AddEditActivity.this, CardsViewPager.class);
				sendIntent.putExtra(Constant.DECK_NAME, deckName);
				sendIntent.putExtra(Constant.DECK_ID, deckID);
				startActivity(sendIntent);
				finish();
			}

		});

		saveDialog
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setIcon(android.R.drawable.ic_dialog_info);

		AlertDialog dialog = saveDialog.create();
		dialog.show();
	}
	
	private void saveCardContent() {
		cardFrontType = frontFragment.getType();
		cardBackType = backFragment.getType();
	}

	private void saveCardType() {
		cardFrontString = frontFragment.getContent();
		cardBackString = backFragment.getContent();
	}

	
	private void changeTabColor() {
		if(side == Side.FRONT) {
			tabHost.getTabWidget().getChildAt(0).setBackgroundColor(GREEN);
			tabHost.getTabWidget().getChildAt(1).setBackgroundColor(ORANGE);
		} else {
			tabHost.getTabWidget().getChildAt(1).setBackgroundColor(GREEN);
			tabHost.getTabWidget().getChildAt(0).setBackgroundColor(ORANGE);
		}
	}
	
}
