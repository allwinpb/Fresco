package icreate.fresco;

import icreate.fresco.Card.Type;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class FrontBackCardFragment extends Fragment implements TabHost.OnTabChangeListener{
	
	public static final int GREEN  = 0xFF16A085;
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
	
	public static FrontBackCardFragment createFragment(String content, int type, boolean isFront) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);
		bundle.putInt(Constant.TYPE, type);
		bundle.putBoolean(Constant.SIDE, isFront);
		
		FrontBackCardFragment fragment = new FrontBackCardFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("FrontBackCard", "Oncreate");
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
		Log.d("FrontBackCard", "OncreateView");
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
		Log.d("FrontBackCard", "UpdateTab");
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
	

	@Override
	public void onTabChanged(String tag) {
		
		Log.d("FrontBackCard", "OnTabChanged");
		boolean isContentEmpty = true;
		
		switch(type) {
			case TEXT:
				isContentEmpty = textFragment.isEmpty();
				if(!isContentEmpty)
					textContent = textFragment.getContent();	
				
				break;
				
			case DOODLE:
				isContentEmpty = doodleFragment.isEmpty();
				if(!isContentEmpty)
					doodleContent = doodleFragment.getContent();
				break;
				
			case IMAGE:
				isContentEmpty =  galleryFragment.isEmpty();
				if(!isContentEmpty)
					galleryContent = galleryFragment.getContent();
				break;
				
			case CAMERA:
				isContentEmpty = cameraFragment.isEmpty();
				if(!isContentEmpty)
					cameraContent = cameraFragment.getContent();
				break;
		}
		
		changeTabColor(tag);
		View tabTitle = ((AddEditActivity) getActivity()).getTabTitleView();
		
		ImageView tabTitleImageView = (ImageView) tabTitle.findViewById(R.id.tabTitleImageView);
		switch(tag) {
			case TEXT:
				type = Type.TEXT;	
				((AddEditActivity)getActivity()).setType(Type.TEXT);
				if(!isContentEmpty)
					((AddEditActivity)getActivity()).setContent(textContent);
				updateTab(TEXT);
				tabTitleImageView.setImageResource(R.drawable.text_24_white);
				break;
			case DOODLE:
				type = Type.DOODLE;
				((AddEditActivity)getActivity()).setType(Type.DOODLE);
				if(!isContentEmpty)
					((AddEditActivity)getActivity()).setContent(doodleContent);
				updateTab(DOODLE);
				tabTitleImageView.setImageResource(R.drawable.edit_24_white);
				break;
			case GALLERY:
				type = Type.IMAGE;
				((AddEditActivity)getActivity()).setType(Type.IMAGE);
				if(!isContentEmpty)
					((AddEditActivity)getActivity()).setContent(galleryContent);
				updateTab(GALLERY);
				tabTitleImageView.setImageResource(R.drawable.gallery_24_white);
				break;
			case CAMERA:
				type = Type.CAMERA;
				((AddEditActivity)getActivity()).setType(Type.CAMERA);
				if(!isContentEmpty)
					((AddEditActivity)getActivity()).setContent(cameraContent);
				updateTab(CAMERA);
				tabTitleImageView.setImageResource(R.drawable.camera_24_white);
				break;
		}
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
		Log.d("FrontBackCard", "OnActivityResult");
		for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("FrontBackCardFragment", "onSaveInstanceState");
		
	}
}
