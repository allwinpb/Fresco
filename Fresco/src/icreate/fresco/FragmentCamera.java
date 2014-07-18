package icreate.fresco;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class FragmentCamera extends Fragment {
	ImageView iv;
	final static int cameraData = 0;
	Bitmap bmp;
	Button takePic;
	boolean isUploaded = false;
	//private Uri selectedImage;
	public static final int MEDIA_TYPE_IMAGE = 1;

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
		takePic = (Button)view.findViewById(R.id.takePic);
		iv = (ImageView) view.findViewById(R.id.camera);
		String content = this.getArguments().getString(Constant.CONTENT);
		if(!content.isEmpty()){
			isUploaded = true;
			bmp = convertFromJSONToImage(content);
			iv.setImageBitmap(bmp);
		}
		

		takePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isUploaded = true;
				Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				getParentFragment().startActivityForResult(image, cameraData);
				
			}
		});
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("camera", bmp);
		Log.d("FragmentCamera", "onSaveInstanceState");
		getParentFragment().onSaveInstanceState(outState);

	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null){
			bmp = savedInstanceState.getParcelable("camera");
			iv.setImageBitmap(bmp);
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			bmp = null;
			Bundle extras = data.getExtras();
			bmp = (Bitmap)extras.get("data");
			iv.setImageBitmap(bmp);
		}
	}
	/*
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		onSaveInstanceState(new Bundle());
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}*/

	public String getContent() {
		if(bmp != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);   
			byte[] byteArrayImage = baos.toByteArray(); 

			return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
		} else {
			return  "";
		}
	}
	
	private Bitmap convertFromJSONToImage(String jsonString) {
		try {
	        byte[] encodeByte = Base64.decode(jsonString, Base64.DEFAULT);
	        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
	                encodeByte.length);
	        return bitmap;
		} catch (Exception e) {
	        e.getMessage(); 
	        return null;
		}
	}


	public boolean isEmpty() {
		return !isUploaded;
	}	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("FragmentCamera", "onDestroy");
	}
}
