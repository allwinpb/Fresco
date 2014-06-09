package icreate.fresco;

import icreate.fresco.Card.Type;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextImageFragment extends Fragment {
	
	String content;
	Type type;
	
	public static TextImageFragment createFragment(String content, int type) {
		
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);
		bundle.putInt(Constant.TYPE, type);
		
		TextImageFragment fragment = new TextImageFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		content = getArguments().getString(Constant.CONTENT);
		type = getType(getArguments().getInt(Constant.TYPE));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = setUpTextImageView(content, type);
		if(view == null){
			TextView text = new TextView(getActivity());
			text.setText("Oh la la");
			view = text;
		}
		
		if(container == null) {
			LinearLayout linearLayout = new LinearLayout(getActivity());
			linearLayout.addView(view);
			return linearLayout;
		} else {
			container.addView(view);
			return container;
		}
		
	}

	private View setUpTextImageView(String content, Type type) {
		
		View view = null;
		
		switch(type) {
			case TEXT:
				view = addText(content);
				break;
				
			case IMAGE:
				view = addImage(content);
				break;
				
			case DOODLE:
				view = addDoodle(content);
				break;
		}
		
		view.setBackgroundResource(R.drawable.background);
		return view;
	}
	
	public View addText(String text) {
		TextView textView = new TextView(this.getActivity());
		textView.setText(text);
		
		return textView;
	}
	
	public View addImage(String path) {
		ImageView image = new ImageView(this.getActivity());
		
		File imgFile = new  File(path);
		if(imgFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			image.setImageBitmap(bitmap);
			return image;
		}
		
		return null; 
	}
	
	public View addDoodle(String jsonString) {
		
		ImageView doodle = new ImageView(this.getActivity()); 
		Bitmap bitmap = convertFromJSONToImage(jsonString);
		
		if(bitmap != null) {
			doodle.setImageBitmap(bitmap);
			return doodle;
		}
		
		return null;
	}
	
	private Bitmap convertFromJSONToImage(String jsonString) {
		try {
	        byte[] encodeByte = Base64.decode(jsonString, Base64.DEFAULT);
	        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
	                encodeByte.length);
	        return bitmap;
		} catch (Exception e) {
	        e.getMessage(); //TODO: send to log
	        return null;
		}
	}
	
	private String convertFromImageToJson(Bitmap bitmap) {
		ByteArrayOutputStream baos = new  ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        
        byte[] b = baos.toByteArray();
        
        String json = Base64.encodeToString(b, Base64.DEFAULT);
        return json;
	}
	
	private Type getType(int type) {
		switch(type) {
			case 0:
				return Type.TEXT;
			case 1:
				return Type.IMAGE;
			case 2:
				return Type.DOODLE;
				
		}
		
		return Type.TEXT;
	}
	
}
