package icreate.fresco;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;

public class FragmentCamera extends Fragment {
	ImageView iv;
	final static int cameraData = 0;
	Bitmap bmp;
	Button takePic;
	private Uri fileUri;
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
		InputStream is = getResources().openRawResource(R.drawable.ic_launcher);
		bmp = BitmapFactory.decodeStream(is);
		//String content = this.getArguments().getString(Constant.CONTENT);
		takePic = (Button)view.findViewById(R.id.takePic);
		iv = (ImageView) view.findViewById(R.id.camera);


		takePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent image = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(image, cameraData);
			}
		});
		return view;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
			Bundle extras = data.getExtras();
			bmp = (Bitmap)extras.get("data");
			iv.setImageBitmap(bmp);
	}
	
	public String getContent() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 80, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
}
