package icreate.fresco;

import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class FragmentGallery extends Fragment{
	ImageView iv;
	final static int RESULT_LOAD_IMAGE = 0;
	Bitmap bmp;
	Button takePic;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;

	public static FragmentGallery createFragment(String content) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);

		FragmentGallery fragment = new FragmentGallery();
		fragment.setArguments(bundle);

		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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
		Uri selectedImageUri = data.getData(); 
		iv.setImageURI(selectedImageUri);
	}
	public String getContent() {
		return bmp.toString();
	}

}
