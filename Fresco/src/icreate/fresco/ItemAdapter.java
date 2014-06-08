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
		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Deck i = objects.get(position);

		if (i != null) {

			TextView deck = (TextView) v.findViewById(R.id.deck);
			if (deck != null){
				deck.setText(i.toString());
			}
		}

		// the view must be returned to our activity
		return v;

	}

}
