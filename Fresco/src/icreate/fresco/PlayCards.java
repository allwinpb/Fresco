package icreate.fresco;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PlayCards extends Activity implements OnClickListener {
	Button w1, w2,w3,w4,w5,d1,d2,d3,d4,d5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_cards);
		w1 = (Button)findViewById(R.id.word1);
		w2 = (Button)findViewById(R.id.word2);
		w3 = (Button)findViewById(R.id.word3);
		w4 = (Button)findViewById(R.id.word4);
		w5 = (Button)findViewById(R.id.word5);
		
		d1 = (Button)findViewById(R.id.definition1);
		d2 = (Button)findViewById(R.id.definition2);
		d3 = (Button)findViewById(R.id.definition3);
		d4 = (Button)findViewById(R.id.definition4);
		d5 = (Button)findViewById(R.id.definition5);
		
		//w1.setBackgroundColor(Color.GREEN);
		
		
		
		
		w1.setOnClickListener(this);
		w2.setOnClickListener(this);
		w3.setOnClickListener(this);
		w4.setOnClickListener(this);
		w5.setOnClickListener(this);
		
		d1.setOnClickListener(this);
		d2.setOnClickListener(this);
		d3.setOnClickListener(this);
		d4.setOnClickListener(this);
		d5.setOnClickListener(this);
		
		
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.word1:
			w1.setBackgroundColor(Color.GREEN);
			w1.invalidate();
			break;
		case R.id.word2:
			w2.setBackgroundColor(Color.BLUE);
			w2.invalidate();
			break;
		case R.id.word3:
			w3.setBackgroundColor(Color.RED);
			w3.invalidate();
			break;
		case R.id.word4:
			w4.setBackgroundColor(Color.YELLOW);
			w4.invalidate();
			break;
		case R.id.word5:
			w5.setBackgroundColor(Color.DKGRAY);
			w5.invalidate();
			break;
		
		case R.id.definition1:

			break;
		case R.id.definition2:

			break;
		case R.id.definition3:

			break;
		case R.id.definition4:

			break;
		case R.id.definition5:

			break;

		}
	}
}
