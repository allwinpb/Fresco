package icreate.fresco;

import icreate.fresco.Card.Type;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class FrontBackCardFragment extends Fragment implements TabHost.OnTabChangeListener{
	
	public static final int GREEN  = 0xFF27AE60;
	public static final int ORANGE = 0xFFD35400;
	
	private TabHost tabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo lastTab = null;
	
	String content;
	Type type;
	
	private class TabInfo {
		 private String tag;
         private Class clss;
         private Bundle args;
         private String content = "";
         private Fragment fragment;
         TabInfo(String tag, Class clazz, Bundle args) {
             this.tag = tag;
             this.clss = clazz;
             this.args = args;
         }
         
         public void addContent(String content) {
        	 this.content = content;
        	 args.putString(Constant.CONTENT, this.content);
         }
	}
	
	class TabFactory implements TabContentFactory {
		 
        private final Context mContext;
 
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
	
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
		
		content = getArguments().getString(Constant.CONTENT);
		type = getType(getArguments().getInt(Constant.TYPE));
		
		if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
	}
	
	public void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", tabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.four_tabs, container, false);
		
		initializeTabHost(view, savedInstanceState);
		
		return view;
	}
	
	private void initializeTabHost(View view, Bundle savedInstanceState) {
		tabHost = (TabHost)view.findViewById(android.R.id.tabhost);
		tabHost.setup();
		if(savedInstanceState == null) {
			savedInstanceState = new Bundle();
		}
		
		TabInfo textTabInfo = new TabInfo("Text", FragmentText.class, savedInstanceState);
		TabSpec textTabSpec = tabHost.newTabSpec("Text");
		textTabSpec.setIndicator("");
		
		TabInfo doodleTabInfo = new TabInfo("Doodle", FragmentDoodle.class, savedInstanceState);
		TabSpec doodleTabSpec = tabHost.newTabSpec("Doodle");
		doodleTabSpec.setIndicator("");
		
		TabInfo cameraTabInfo = new TabInfo("Camera", FragmentCamera.class, savedInstanceState);
		TabSpec cameraTabSpec = tabHost.newTabSpec("Camera");
		cameraTabSpec.setIndicator("");
		
		TabInfo galleryTabInfo = new TabInfo("Gallery", FragmentGallery.class, savedInstanceState);
		TabSpec galleryTabSpec = tabHost.newTabSpec("Gallery");
		galleryTabSpec.setIndicator("");
		
		switch(type) {
    		case TEXT:
    			textTabInfo.addContent(content);
    			break;
    		case DOODLE:
    			doodleTabInfo.addContent(content);
    			break;
    		case IMAGE:
    			galleryTabInfo.addContent(content);
    			break;	
		}
		
		//Text tab
		addTab((AddEditActivity)getActivity(), tabHost, textTabSpec, textTabInfo);
        mapTabInfo.put(textTabInfo.tag, textTabInfo);
        
        //Doodle tab
        addTab((AddEditActivity)getActivity(), tabHost, doodleTabSpec, doodleTabInfo);
        mapTabInfo.put(doodleTabInfo.tag, doodleTabInfo);
        
        //Camera tab
		addTab((AddEditActivity)getActivity(), tabHost, galleryTabSpec, galleryTabInfo);
        mapTabInfo.put(galleryTabInfo.tag, galleryTabInfo);
        
        //Gallery tab
		addTab((AddEditActivity)getActivity(), tabHost, cameraTabSpec, cameraTabInfo);
        mapTabInfo.put(cameraTabInfo.tag, cameraTabInfo);
        
		switch(type) {
    		case TEXT:
    			this.onTabChanged("Text");
    			changeTabColor("Text");
    			break;
    		case DOODLE:
    			this.onTabChanged("Doodle");
    			changeTabColor("Doodle");
    			break;
    		case IMAGE:
    			this.onTabChanged("Gallery");
    			changeTabColor("Gallery");
    			break;	
    		case CAMERA:
    			this.onTabChanged("Camera");
    			changeTabColor("Camera");
		}
		
		tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.icon_config_text);
		tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.icon_config_doodle);
		tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.icon_config_gallery);
		tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.icon_config_camera);
        
        tabHost.setOnTabChangedListener(this);
	}
	
	private void addTab(AddEditActivity activity, TabHost tabHost,
			TabSpec tabSpec, TabInfo tabInfo) {
		
		tabSpec.setContent(this.new TabFactory(activity));
	    String tag = tabSpec.getTag();
	 
	    // Check to see if we already have a fragment for this tab, probably
	    // from a previously saved state.  If so, deactivate it, because our
	    // initial state is that a tab isn't shown.
	    tabInfo.fragment = getChildFragmentManager().findFragmentByTag(tag);
	    if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
	        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
	        ft.detach(tabInfo.fragment);
	        ft.commit();
	        getChildFragmentManager().executePendingTransactions();
	    }
	 
	    tabHost.addTab(tabSpec);
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
	
	private void changeTabColor(String tag) {
		
		for(int i=0; i<4; i++) 
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(ORANGE);
		
		switch(tag.toUpperCase()) {
			case "TEXT":
				tabHost.getTabWidget().getChildAt(0).setBackgroundColor(GREEN);
				break;
			case "DOODLE":
				tabHost.getTabWidget().getChildAt(1).setBackgroundColor(GREEN);
				break;
			case "GALLERY":
				tabHost.getTabWidget().getChildAt(2).setBackgroundColor(GREEN);
				break;
			case "CAMERA":
				tabHost.getTabWidget().getChildAt(3).setBackgroundColor(GREEN);
				break;
					
		}
	}

	@Override
	public void onTabChanged(String tag) {
		TabInfo newTab = this.mapTabInfo.get(tag);
		
		if (lastTab != newTab) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if (lastTab != null) {
                if (lastTab.fragment != null) {
                    ft.detach(lastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(getActivity(),
                            newTab.clss.getName(), newTab.args);
                    ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }
 
            lastTab = newTab;
            ft.commit();
            getChildFragmentManager().executePendingTransactions();
        }
	}
	
}
