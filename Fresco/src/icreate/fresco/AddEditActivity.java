package icreate.fresco;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AddEditActivity extends ActionBarActivity {
	
	public static final int GREEN  = 0xFF27AE60;
	public static final int ORANGE = 0xFFD35400;
	
	int deckID;
	int cardID;
	boolean newEdit;
	
	Button frontBtn;
	Button backBtn;
	
	ImageButton returnBtn;
	ImageButton doneBtn;
	
	ImageButton textBtn;
	ImageButton editBtn;
	ImageButton galleryBtn;
	ImageButton cameraBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit);
		
		Intent receiveIntent = getIntent();
		deckID  = receiveIntent.getIntExtra(Constant.DECK_ID, 1);
		cardID  = receiveIntent.getIntExtra(Constant.CARD_ID, 1);
		newEdit = receiveIntent.getBooleanExtra(Constant.NEW_EDIT, false); 
		
		frontBtn 	= (Button) findViewById(R.id.frontBtn);
		backBtn  	= (Button) findViewById(R.id.backBtn);
		
		returnBtn  	= (ImageButton) findViewById(R.id.returnBtn);
		doneBtn 	= (ImageButton) findViewById(R.id.doneBtn);
		
		textBtn  	= (ImageButton) findViewById(R.id.textBtn);
		editBtn  	= (ImageButton) findViewById(R.id.editBtn);
		galleryBtn	= (ImageButton) findViewById(R.id.galleryBtn);
		cameraBtn  	= (ImageButton) findViewById(R.id.cameraBtn);
		
				
		frontBtn.setOnClickListener(frontHandler);
		backBtn.setOnClickListener(backHandler);
		
		returnBtn.setOnClickListener(returnHandler);
		doneBtn.setOnClickListener(doneHandler);
		
		textBtn.setOnClickListener(textHandler);
		editBtn.setOnClickListener(editHandler);
		galleryBtn.setOnClickListener(galleryHandler);
		cameraBtn.setOnClickListener(cameraHandler);
	}
	
	View.OnClickListener frontHandler = new View.OnClickListener(){ 
		public void onClick(View v) {
			frontBtn.setBackgroundColor(GREEN);
			backBtn.setBackgroundColor(ORANGE);
		}
	};

	View.OnClickListener backHandler = new View.OnClickListener() {
		public void onClick(View v) {
			frontBtn.setBackgroundColor(ORANGE);
			backBtn.setBackgroundColor(GREEN);
		}
	};
	
	View.OnClickListener returnHandler = new View.OnClickListener() {
		public void onClick(View v) {
			AlertDialog.Builder exitDialog = new AlertDialog.Builder(AddEditActivity.this);
			
			exitDialog
				.setTitle("Exit Confirmation")
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
					
				}
			});
			
			saveDialog
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					}
			})
			.setIcon(android.R.drawable.ic_dialog_info);
			
			AlertDialog dialog = saveDialog.create();
			dialog.show();
		}
	};
	
	View.OnClickListener textHandler = new View.OnClickListener() {
		public void onClick(View v) {
			setButtonsColorOrange(editBtn, galleryBtn, cameraBtn);
			textBtn.setBackgroundColor(GREEN);
		}
	};
	
	View.OnClickListener editHandler = new View.OnClickListener() {
		public void onClick(View v) {
			setButtonsColorOrange(textBtn, galleryBtn, cameraBtn);
			editBtn.setBackgroundColor(GREEN);
		}
	};
	
	View.OnClickListener galleryHandler = new View.OnClickListener() {
		public void onClick(View v) {
			setButtonsColorOrange(textBtn, editBtn, cameraBtn);
			galleryBtn.setBackgroundColor(GREEN);
		}
	};
	
	View.OnClickListener cameraHandler = new View.OnClickListener() {
		public void onClick(View v) {
			setButtonsColorOrange(textBtn, editBtn, galleryBtn);
			cameraBtn.setBackgroundColor(GREEN);
		}
	};
	
	public void setButtonsColorOrange(ImageButton b1, ImageButton b2, ImageButton b3){
		b1.setBackgroundColor(ORANGE);
		b2.setBackgroundColor(ORANGE);
		b3.setBackgroundColor(ORANGE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
	}

}
