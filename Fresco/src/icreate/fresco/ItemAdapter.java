package icreate.fresco;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Deck> {
	private ArrayList<Deck> objects;
	
	public ItemAdapter(Context context, int textViewResourceId, ArrayList<Deck> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}
	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;
		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_deck, null);
		}
		Deck i = objects.get(position);

		if (i != null) {

			TextView deck = (TextView) v.findViewById(R.id.deck);
			if (deck != null){
				String name = i.getDeckName();
				if(name.length() > 12) {
					name = name.substring(0, 10) + "...";
				}
				deck.setText(name);
			}
		}
		// the view must be returned to our activity
		return v;
	}
}
