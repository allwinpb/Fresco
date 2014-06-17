package icreate.fresco;

import icreate.fresco.Card.Side;
import icreate.fresco.Card.Type;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class AddEditActivity extends FragmentActivity implements OnTabChangeListener {
	
	public static final String FRONT = "Front";
	public static final String BACK = "Back";
	
	private TextView deckTextView;
	
	private int deckID;
	private Card card;
	private boolean newEdit;
	private String deckName;
	
	private SqliteHelper database;
	
	private Type cardFrontType = Type.TEXT;
	private String cardFrontString = "";
	private Type cardBackType = Type.TEXT;
	private String cardBackString  = "";
	
	private ImageButton returnBtn;
	private ImageButton doneBtn;
	
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
		
		deckTextView = (TextView) findViewById(R.id.deckTextView);
		deckTextView.setText(deckName);
		
		returnBtn  	= (ImageButton) findViewById(R.id.returnBtn);
		doneBtn 	= (ImageButton) findViewById(R.id.doneBtn);
		
		returnBtn.setOnClickListener(returnHandler);
		doneBtn.setOnClickListener(doneHandler);
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
				FrontBackCardFragment frontFragment = FrontBackCardFragment.createFragment(cardFrontString, getIntType(cardFrontType));
				fm.beginTransaction()
					.replace(R.id.tab_front, frontFragment, FRONT)
					.commit();
				break;
			case BACK:
				FrontBackCardFragment backFragment = FrontBackCardFragment.createFragment(cardBackString, getIntType(cardBackType));
				fm.beginTransaction()
					.replace(R.id.tab_back, backFragment, BACK)
					.commit();
				break;
		}
	}

	private TabSpec newTab(String tag, String tagLabel, int contentId) {
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(tagLabel);
		tabSpec.setContent(contentId);
		return tabSpec;
	}
	
	View.OnClickListener returnHandler = new View.OnClickListener() {
		public void onClick(View v) {
			AlertDialog.Builder exitDialog = new AlertDialog.Builder(AddEditActivity.this);
			
			exitDialog
				.setTitle("Exit Confirmation")
				.setCancelable(true)
				.setMessage("Changes not saved will be discarded")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
						
						Intent sendIntent = new Intent(AddEditActivity.this, CardsViewPager.class);
						sendIntent.getIntExtra(Constant.DECK_ID, deckID);
						startActivity(sendIntent);
						
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
	};
	
	View.OnClickListener doneHandler = new View.OnClickListener() {
		public void onClick(View v) {
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(AddEditActivity.this);
			
			saveDialog
				.setTitle("Save confirmation")
				.setMessage("Changes not saved will be discarded")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					card.setType(Side.FRONT, cardFrontType);
					card.setType(Side.BACK, cardBackType);
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
	};
	
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
}
