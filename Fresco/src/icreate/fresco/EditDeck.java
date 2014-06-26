package icreate.fresco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditDeck extends Activity implements OnClickListener {
	EditText et;
	Button done;
	Button delete;
	String editText;
	String icon = "";
	int pos;
	ImageButton people, hammer, cat, number, german, film, talk, key, home, book, picture, christmas;
	ImageButton newspaper, basketball, musics, hamburger;
	final private static int DISMISS = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_deck);


		delete = (Button)findViewById(R.id.delete);
		done = (Button)findViewById(R.id.done);
		et = (EditText)findViewById(R.id.deck);
		people = (ImageButton)findViewById(R.id.people);
		hammer = (ImageButton)findViewById(R.id.hammer);
		cat    = (ImageButton)findViewById(R.id.cat);
		number = (ImageButton)findViewById(R.id.number);
		german = (ImageButton)findViewById(R.id.german);
		film   = (ImageButton)findViewById(R.id.film);
		talk   = (ImageButton)findViewById(R.id.talk);
		key    = (ImageButton)findViewById(R.id.key);
		home   = (ImageButton)findViewById(R.id.home);
		book   = (ImageButton)findViewById(R.id.book);
		picture = (ImageButton)findViewById(R.id.picture);
		christmas = (ImageButton)findViewById(R.id.christmas);
		newspaper = (ImageButton)findViewById(R.id.newspaper);
		basketball = (ImageButton)findViewById(R.id.basketball);
		musics = (ImageButton)findViewById(R.id.musics);
		hamburger = (ImageButton)findViewById(R.id.hamburger);


		people.setOnClickListener(this);
		hammer.setOnClickListener(this);
		cat.setOnClickListener(this);
		number.setOnClickListener(this);
		german.setOnClickListener(this);
		film.setOnClickListener(this);
		talk.setOnClickListener(this);
		key.setOnClickListener(this);
		home.setOnClickListener(this);
		book.setOnClickListener(this);
		picture.setOnClickListener(this);
		christmas.setOnClickListener(this);
		newspaper.setOnClickListener(this);
		basketball.setOnClickListener(this);
		musics.setOnClickListener(this);
		hamburger.setOnClickListener(this);
		done.setOnClickListener(this);
		delete.setOnClickListener(this);

		retrieveDeck();


	}

	private void retrieveDeck() {
		// TODO Auto-generated method stub
		Intent receiveIntent = getIntent();
		et.setText(receiveIntent.getStringExtra(Constant.DECK_NAME));
		pos = receiveIntent.getIntExtra(Constant.POSITION_COLOR, 0);
		icon = receiveIntent.getStringExtra(Constant.DECK_ICON);
		switch(icon){
		case "people":
			people.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "hammer":
			hammer.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "cat":
			cat.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "number":
			number.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "german":
			german.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "film":
			film.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "talk":
			talk.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "key":
			key.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "home":
			home.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "book":
			book.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "picture":
			picture.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "christmas":
			christmas.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "newspaper":
			newspaper.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "basketball":
			basketball.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "musics":
			musics.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case "hamburger":
			hamburger.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		}

	}

	@SuppressWarnings("deprecation")
	public void Finish(){
		editText = et.getText().toString();
		Intent i = getIntent();
		//String msg = i.getStringExtra("numbers");
		if(!editText.equals("")){
			if(icon.equals("")){
				showDialog(0, null);
			}
			else{
				i.putExtra("deck", editText);
				i.putExtra("icon", icon);
				i.putExtra("position", pos);
				setResult(RESULT_OK,i);
				finish();
			}
		}else{
			showDialog(1, null);
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
			Finish();
			return true;

		case android.R.id.home:
			confirmLeaving();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void confirmLeaving() {
		AlertDialog.Builder exitDialog = new AlertDialog.Builder(EditDeck.this);

		exitDialog
		.setTitle("Exit Confirmation")
		.setCancelable(true)
		.setMessage("Changes not saved will be discarded")
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
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
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog dialogDetails = null;
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview;
		if(id == 1){
			dialogview = inflater.inflate(R.layout.dialog_no_deck_name, null);
		}else{
			dialogview = inflater.inflate(R.layout.dialog_no_icon, null);
		}

		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
		dialogbuilder.setView(dialogview);
		dialogDetails = dialogbuilder.create();
		return dialogDetails;
	}
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		final AlertDialog alertDialog = (AlertDialog) dialog;
		Button okay = (Button) alertDialog.findViewById(R.id.dismiss);
		okay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
	}
	public void Cancel(View v){
		Intent i = getIntent();
		setResult(RESULT_CANCELED, i);
		finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		setResult(RESULT_CANCELED, i);
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.people:
			icon = "people";
			setDefaultColor();
			people.setBackgroundColor(Color.rgb(39, 174, 96));
			break;
		case R.id.hammer:
			icon = "hammer";
			setDefaultColor();
			hammer.setBackgroundColor(Color.rgb(39, 174, 96));
			//hammer.invalidate();
			break;
		case R.id.cat:
			icon = "cat";
			setDefaultColor();
			cat.setBackgroundColor(Color.rgb(39, 174, 96));
			//cat.invalidate();
			break;
		case R.id.number:
			icon = "number";
			setDefaultColor();
			number.setBackgroundColor(Color.rgb(39, 174, 96));
			//number.invalidate();
			break;
		case R.id.german:
			icon = "german";
			setDefaultColor();
			german.setBackgroundColor(Color.rgb(39, 174, 96));
			//german.invalidate();
			break;
		case R.id.film:
			icon = "film";
			setDefaultColor();
			film.setBackgroundColor(Color.rgb(39, 174, 96));
			//film.invalidate();
			break;
		case R.id.talk:
			icon = "talk";
			setDefaultColor();
			talk.setBackgroundColor(Color.rgb(39, 174, 96));
			//talk.invalidate();
			break;
		case R.id.key:
			icon = "key";
			setDefaultColor();
			key.setBackgroundColor(Color.rgb(39, 174, 96));
			//key.invalidate();
			break;
		case R.id.home:
			icon = "home";
			setDefaultColor();
			home.setBackgroundColor(Color.rgb(39, 174, 96));
			//home.invalidate();
			break;
		case R.id.book:
			icon = "book";
			setDefaultColor();
			book.setBackgroundColor(Color.rgb(39, 174, 96));
			//book.invalidate();
			break;
		case R.id.picture:
			icon = "picture";
			setDefaultColor();
			picture.setBackgroundColor(Color.rgb(39, 174, 96));
			//picture.invalidate();
			break;
		case R.id.christmas:
			icon = "christmas";
			setDefaultColor();
			christmas.setBackgroundColor(Color.rgb(39, 174, 96));
			//christmas.invalidate();
			break;
		case R.id.newspaper:
			icon = "newspaper";
			setDefaultColor();
			newspaper.setBackgroundColor(Color.rgb(39, 174, 96));
			//newspaper.invalidate();
			break;
		case R.id.basketball:
			icon = "basketball";
			setDefaultColor();
			basketball.setBackgroundColor(Color.rgb(39, 174, 96));
			//basketball.invalidate();
			break;
		case R.id.musics:
			icon = "musics";
			setDefaultColor();
			musics.setBackgroundColor(Color.rgb(39, 174, 96));
			//musics.invalidate();
			break;
		case R.id.hamburger:
			icon = "hamburger";
			setDefaultColor();
			hamburger.setBackgroundColor(Color.rgb(39, 174, 96));
			//hamburger.invalidate();
			break;
		case R.id.done:
			Finish();
			break;
		case R.id.delete:
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditDeck.this);
			dialogBuilder.setTitle("Delete");
			dialogBuilder.setMessage("Do you want to delete this deck?");
			dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					Intent i = getIntent();
					pos = i.getIntExtra(Constant.POSITION_COLOR, 0);
					i.putExtra("position", pos);
					setResult(0, i);
					finish();	
				}

			});
			dialogBuilder.create().show();

			break;
		}
	}
	public void setDefaultColor(){
		people.setBackgroundColor(Color.rgb(255, 23, 108));
		hammer.setBackgroundColor(Color.rgb(255, 23, 108));
		cat.setBackgroundColor(Color.rgb(255, 23, 108));
		number.setBackgroundColor(Color.rgb(255, 23, 108));
		german.setBackgroundColor(Color.rgb(255, 23, 108));
		film.setBackgroundColor(Color.rgb(255, 23, 108));
		talk.setBackgroundColor(Color.rgb(255, 23, 108));
		key.setBackgroundColor(Color.rgb(255, 23, 108));
		home.setBackgroundColor(Color.rgb(255, 23, 108));
		book.setBackgroundColor(Color.rgb(255, 23, 108));
		picture.setBackgroundColor(Color.rgb(255, 23, 108));
		christmas.setBackgroundColor(Color.rgb(255, 23, 108));
		newspaper.setBackgroundColor(Color.rgb(255, 23, 108));
		basketball.setBackgroundColor(Color.rgb(255, 23, 108));
		musics.setBackgroundColor(Color.rgb(255, 23, 108));
		hamburger.setBackgroundColor(Color.rgb(255, 23, 108));
	}
	public int dpToPx(double dp) {
		DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
		int px =(int) Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
		return px;
	}
}
