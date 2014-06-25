package icreate.fresco;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class FragmentGallery extends Fragment{
	ImageView iv;
	final static int RESULT_LOAD_IMAGE = 0;
	Bitmap bmp;
	Button takePic;
	private Uri selectedImage;
	public static final int MEDIA_TYPE_IMAGE = 1;
	//private String Gallery;

	public static FragmentGallery createFragment(String content) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);

		FragmentGallery fragment = new FragmentGallery();
		fragment.setArguments(bundle);

		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//onRestoreInstanceState(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_gallery, container, false);
		iv = (ImageView)view.findViewById(R.id.gallery);
		//String content = this.getArguments().getString(Constant.CONTENT);
		Button gallery = (Button)view.findViewById(R.id.goGallery);
		gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		return view;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			int w = 300; int h = 384; // size that does not lead to OutOfMemoryException on Nexus One
			Bitmap b = BitmapFactory.decodeFile(picturePath);

			// Hack to determine whether the image is rotated
			boolean rotated = b.getWidth() > b.getHeight();

			bmp = null;

			// If not rotated, just scale it
			if (!rotated) {
				bmp = Bitmap.createScaledBitmap(b, w, h, true);
				iv.setImageBitmap(bmp);
				b.recycle();
				b = null;

				// If rotated, scale it by switching width and height and then rotated it
			} else {
				Bitmap scaledBmp = Bitmap.createScaledBitmap(b, h, w, true);
				b.recycle();
				b = null;

				Matrix mat = new Matrix();
				mat.postRotate(90);
				bmp = Bitmap.createBitmap(scaledBmp, 0, 0, h, w, mat, true);
				iv.setImageBitmap(bmp);
				// Release image resources
				scaledBmp.recycle();
				scaledBmp = null;
			}	
			//iv.setImageBitmap(resultBmp);
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
		outState.putSerializable("Gallery", Gallery);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState){
		
		if(savedInstanceState != null){
           Gallery = savedInstanceState.getString("Gallery");            
        }
	}*/
	public String getContent(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);   
		byte[] byteArrayImage = baos.toByteArray(); 
		
		return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
	}

}
