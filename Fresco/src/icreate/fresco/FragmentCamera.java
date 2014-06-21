package icreate.fresco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentCamera extends Fragment {
	static ImageView iv;
	
	public static FragmentCamera createFragment(String content) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);
		
		FragmentCamera fragment = new FragmentCamera();
		fragment.setArguments(bundle);
		
		return fragment;
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.camera, container, false);
		
		String content = this.getArguments().getString(Constant.CONTENT);
		
		iv = (ImageView) view.findViewById(R.id.camera);
		Intent image;
        return view;
    }
	
	public static String saveContent() {
		return iv.toString();
	}

}
