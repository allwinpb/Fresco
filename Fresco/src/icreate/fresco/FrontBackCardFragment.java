package icreate.fresco;

import icreate.fresco.Card.Type;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class FrontBackCardFragment extends Fragment implements TabHost.OnTabChangeListener{
	
	public static final int GREEN  = 0xFF27AE60;
	public static final int ORANGE = 0xFFD35400;
	
	private static final String TEXT = "Text";
	private static final String DOODLE = "Doodle";
	private static final String GALLERY = "Gallery";
	private static final String CAMERA = "Camera";
	
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
		
		tabHost.addTab(newTab(TEXT, "", R.id.tab_text));
		tabHost.addTab(newTab(DOODLE, "", R.id.tab_doodle));
		tabHost.addTab(newTab(GALLERY, "", R.id.tab_gallery));
		tabHost.addTab(newTab(CAMERA, "", R.id.tab_camera));
		
		switch(type) {
			case TEXT:
				updateTab(TEXT);
				changeTabColor(TEXT);
				break;
			case DOODLE:
				updateTab(DOODLE);
				changeTabColor(DOODLE);
				break;
			case IMAGE:
				updateTab(GALLERY);
				changeTabColor(GALLERY);
				break;
			case CAMERA:
				updateTab(CAMERA);
				changeTabColor(CAMERA);
				break;
		}
		
		tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.text);
		tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.edit);
		tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.gallery);
		tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.camera);
		
		tabHost.setOnTabChangedListener(this);
	}
	
	private void updateTab(String tag) {
		FragmentManager manager = getChildFragmentManager();
		
		switch(tag) {
			case TEXT:
				FragmentText textFragment = FragmentText.createFragment(textContent);
				manager.beginTransaction()
				.replace(R.id.tab_text, textFragment, TEXT)
				.commit();
				
				break;
			case DOODLE:
				FragmentDoodle doodleFragment = FragmentDoodle.createFragment(doodleContent);
				manager.beginTransaction()
				.replace(R.id.tab_doodle, doodleFragment, DOODLE)
				.commit();
				
				break;
			case GALLERY:
				FragmentGallery galleryFragment = FragmentGallery.createFragment(galleryContent);
				manager.beginTransaction()
				.replace(R.id.tab_gallery, galleryFragment, GALLERY)
				.commit();
				
				break;
			case CAMERA:
				FragmentText cameraFragment = FragmentText.createFragment(cameraContent);
				manager.beginTransaction()
				.replace(R.id.tab_camera, cameraFragment, CAMERA)
				.commit();
				
				break;
		}
	}

	private TabSpec newTab(String tag, String tagLabel, int contentId) {
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(tagLabel);
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

	@Override
	public void onTabChanged(String tag) {
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
		
		tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.text);
		tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.edit);
		tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.gallery);
		tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.camera);
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
	
}
