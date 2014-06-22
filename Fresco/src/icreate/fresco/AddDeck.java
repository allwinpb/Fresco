package icreate.fresco;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDeck extends Activity {
	EditText et;
	final private static int DISMISS = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_deck);
		et = (EditText)findViewById(R.id.deck);
	}
	@SuppressWarnings("deprecation")
	public void Finish(View v){
		String s = et.getText().toString();
		Intent i = getIntent();
		//String msg = i.getStringExtra("numbers");
		if(!s.equals("")){
			i.putExtra("deck", s);
			setResult(RESULT_OK,i);
			finish();
		}else{
			showDialog(0, null);
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog dialogDetails = null;
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = inflater.inflate(R.layout.dialog_no_deck_name, null);
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
}
