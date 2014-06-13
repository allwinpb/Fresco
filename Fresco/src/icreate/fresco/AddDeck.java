package icreate.fresco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDeck extends Activity {
	EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_deck);
		et = (EditText)findViewById(R.id.category);
	}
	public void start(View v){
		String s = et.getText().toString();
		Intent i = getIntent();
		String msg = i.getStringExtra("numbers");
		i.putExtra("category", s);
		setResult(RESULT_OK,i);
		finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, FrescoMain.class);
		startActivity(i);
	}
}
