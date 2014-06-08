package icreate.fresco;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.os.Build;

public class AddEditActivity extends ActionBarActivity {
	
	public static final int GREEN  = 0xFF27AE60;
	public static final int ORANGE = 0xFFD35400;
	
	Button frontBtn;
	Button backBtn;
	
	ImageButton textBtn;
	ImageButton editBtn;
	ImageButton galleryBtn;
	ImageButton cameraBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit);
		
		frontBtn 	= (Button) findViewById(R.id.frontBtn);
		backBtn  	= (Button) findViewById(R.id.backBtn);
		
		textBtn  	= (ImageButton) findViewById(R.id.textBtn);
		editBtn  	= (ImageButton) findViewById(R.id.editBtn);
		galleryBtn	= (ImageButton) findViewById(R.id.galleryBtn);
		cameraBtn  	= (ImageButton) findViewById(R.id.cameraBtn);
		
				
		frontBtn.setOnClickListener(frontHandler);
		backBtn.setOnClickListener(backHandler);
		
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
