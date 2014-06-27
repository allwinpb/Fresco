package icreate.fresco;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Deck> {
	private ArrayList<Deck> objects;
	
	public ItemAdapter(Context context, int textViewResourceId, ArrayList<Deck> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}
	public View getView(int position, View convertView, ViewGroup parent){
		
		parent.setBackgroundColor(Color.argb(255, 236, 240, 241));

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
			
			String iconName = i.getDeckIcon();
			int id = getContext().getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
			ImageView imageView = (ImageView) v.findViewById(R.id.category); 
			imageView.setImageResource(id);
			
		}
		// the view must be returned to our activity
		v.setBackgroundColor(Color.argb(255, Constant.color[position%Constant.COLOR_NUMBER][0], Constant.color[position%Constant.COLOR_NUMBER][1], Constant.color[position%Constant.COLOR_NUMBER][2]));
		return v;
	}
}
