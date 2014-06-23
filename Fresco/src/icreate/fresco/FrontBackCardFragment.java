package icreate.fresco;

import icreate.fresco.Card.Type;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class FrontBackCardFragment extends Fragment implements TabHost.OnTabChangeListener{
	
	public static final int GREEN  = 0xFF27AE60;
	public static final int ORANGE = 0xFFD35400;
	
	private static final String TEXT = "Text";
	private static final String DOODLE = "Doodle";
	private static final String GALLERY = "Gallery";
	private static final String CAMERA = "Camera";
	
	private FragmentText textFragment;
	private FragmentDoodle doodleFragment;
	private FragmentGallery galleryFragment;
	private FragmentCamera cameraFragment;
	
	private TabHost tabHost;
	
	String textContent = "";
	String doodleContent = "";
	String galleryContent = "";
	String cameraContent = "";
	Type type = Type.TEXT;
	
	public static FrontBackCardFragment createFragment(String content, int type) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);
		bundle.putInt(Constant.TYPE, type);
		
		FrontBackCardFragment fragment = new FrontBackCardFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		type = getType(getArguments().getInt(Constant.TYPE));
		switch(type) {
			case TEXT:
				textContent = getArguments().getString(Constant.CONTENT);
				break;
			case DOODLE:
				doodleContent = getArguments().getString(Constant.CONTENT);
				break;
			case IMAGE:
				galleryContent = getArguments().getString(Constant.CONTENT);
				break;
			case CAMERA:
				cameraContent = getArguments().getString(Constant.CONTENT);
				break;
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.four_tabs, container, false);
		
		initializeTabHost(view);
		
		return view;
	}
	
	private void initializeTabHost(View view) {
		tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		tabHost.addTab(newTab(TEXT, R.drawable.text, R.id.tab_text));
		tabHost.addTab(newTab(DOODLE, R.drawable.edit, R.id.tab_doodle));
		tabHost.addTab(newTab(GALLERY, R.drawable.gallery, R.id.tab_gallery));
		tabHost.addTab(newTab(CAMERA, R.drawable.camera, R.id.tab_camera));
		
		switch(type) {
			case TEXT:
				updateTab(TEXT);
				changeTabColor(TEXT);
				tabHost.setCurrentTab(0);
				break;
			case DOODLE:
				updateTab(DOODLE);
				changeTabColor(DOODLE);
				tabHost.setCurrentTab(1);
				break;
			case IMAGE:
				updateTab(GALLERY);
				changeTabColor(GALLERY);
				tabHost.setCurrentTab(2);
				break;
			case CAMERA:
				updateTab(CAMERA);
				changeTabColor(CAMERA);
				tabHost.setCurrentTab(3);
				break;
		}
		
		tabHost.setOnTabChangedListener(this);
	}
	
	private void updateTab(String tag) {
		FragmentManager manager = getChildFragmentManager();
		
		switch(tag) {
			case TEXT:
				textFragment = FragmentText.createFragment(textContent);
				manager.beginTransaction()
				.replace(R.id.tab_text, textFragment, TEXT)
				.commit();
				
				break;
			case DOODLE:
				doodleFragment = FragmentDoodle.createFragment(doodleContent);
				manager.beginTransaction()
				.replace(R.id.tab_doodle, doodleFragment, DOODLE)
				.commit();
				
				break;
			case GALLERY:
				galleryFragment = FragmentGallery.createFragment(galleryContent);
				manager.beginTransaction()
				.replace(R.id.tab_gallery, galleryFragment, GALLERY)
				.commit();
				
				break;
			case CAMERA:
				cameraFragment = FragmentCamera.createFragment(cameraContent);
				manager.beginTransaction()
				.replace(R.id.tab_camera, cameraFragment, CAMERA)
				.commit();
				
				break;
		}
	}

	private TabSpec newTab(String tag, int id, int contentId) {
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator("", getResources().getDrawable(id));
		tabSpec.setContent(contentId);
		return tabSpec;
	}
	
	
	private Type getType(int type) {
		switch(type) {
			case 0:
				return Type.TEXT;
			case 1:
				return Type.DOODLE;
			case 2:
				return Type.IMAGE;
			case 3:
				return Type.CAMERA;
		}
		return Type.TEXT;
	}
	
	private String getStringType(Type type) {
		switch(type) {
			case TEXT:
				return "Text";
			case DOODLE:
				return "Doodle";
			case IMAGE:
				return "Gallery";
			case CAMERA:
				return "Camera";
		}
		return "Text";
	}
	

	@Override
	public void onTabChanged(String tag) {
		
		/*String content = getContent();
		if(content.isEmpty()) {*/
		
			changeTabColor(tag);
			switch(tag) {
				case TEXT:
					type = Type.TEXT;	
					((AddEditActivity)getActivity()).setType(Type.TEXT);
					updateTab(TEXT);
					break;
				case DOODLE:
					type = Type.DOODLE;
					((AddEditActivity)getActivity()).setType(Type.DOODLE);
					updateTab(DOODLE);
					break;
				case GALLERY:
					type = Type.IMAGE;
					((AddEditActivity)getActivity()).setType(Type.IMAGE);
					updateTab(GALLERY);
					break;
				case CAMERA:
					type = Type.CAMERA;
					((AddEditActivity)getActivity()).setType(Type.CAMERA);
					updateTab(CAMERA);
					break;
			}
			
		//} 
		
		/*else {
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.tab_warning_toast, null);
			
			TextView tabText = (TextView) view.findViewById(R.id.tabText);
			String tab = getStringType(type);
			String tabTextContent = tab + " is not empty. Changing tabs will automatically discard previous content";
			tabText.setText(tabTextContent);
			
			TextView returnBackText = (TextView) view.findViewById(R.id.returnBackText);
			SpannableString spanString = new SpannableString(returnBackText.getText().toString());
			spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
			spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
			returnBackText.setText(spanString);
			
			returnBackText.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					switch(type) {
						case TEXT:
							tabHost.setCurrentTab(0);
							break;
						case DOODLE:
							tabHost.setCurrentTab(1);
							break;
						case IMAGE:
							tabHost.setCurrentTab(2);
							break;
						case CAMERA:
							tabHost.setCurrentTab(3);
							break;
				}
				}}
			); 
			
			Toast toast = new Toast(getActivity());
			toast.setView(view);
	        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
	                0, 0);
	        toast.setDuration(Toast.LENGTH_SHORT);
	        toast.show();

		}*/
	}
	
	private void changeTabColor(String tag) {
		for(int i=0; i<4; i++) 
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(ORANGE);
		
		switch(tag) {
			case TEXT:
				tabHost.getTabWidget().getChildAt(0).setBackgroundColor(GREEN);
				break;
			case DOODLE:
				tabHost.getTabWidget().getChildAt(1).setBackgroundColor(GREEN);
				break;
			case GALLERY:
				tabHost.getTabWidget().getChildAt(2).setBackgroundColor(GREEN);
				break;
			case CAMERA:
				tabHost.getTabWidget().getChildAt(3).setBackgroundColor(GREEN);
				break;		
		}
	}
	
	public Type getType() {
		return type;
	}
	
	public String getContent() {
		switch(type) {
			case TEXT:
				return textFragment.getContent();
			case DOODLE:
				return doodleFragment.getContent();
			case IMAGE:
				return galleryFragment.getContent();
			default:
				return cameraFragment.getContent();
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
	}
}
