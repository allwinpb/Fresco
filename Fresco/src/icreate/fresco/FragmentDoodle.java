package icreate.fresco;

import icreate.doodle.DoodleView;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class FragmentDoodle extends Fragment {

	private DoodleView doodle;
	
	String jsonString = "";
	
	private static final int DEFAULT_WIDTH = 15;
	
	private SharedPreferences prefs;
	
	private float acceleration; 
	private float currentAcceleration; 
	private float lastAcceleration; 
	
	private AlertDialog dialog;
	private SeekBar widthSeekBar;
	private ImageView imageView;
	private TextView progressTextView;
	private ImageButton penImageButton;
	private ImageButton eraserImageButton;
	private ImageButton sizeImageButton;
	private ImageButton clearAllImageButton;
	private Button defaultButton;
	
	/*private enum CanvasTool {
		PENCIL, ERASER
	};*/
	
	//private CanvasTool canvasTool = CanvasTool.PENCIL;
	//private int pencilWidth = 15;
	//private int eraserWidth = 15;
	
	private int width = 15;
	private static final int ACCELERATION_THRESHOLD = 10000;
	
	private SensorManager sensorManager; 
	private AtomicBoolean dialogIsDisplayed = new AtomicBoolean();
	
	public static FragmentDoodle createFragment(String content) {
		Bundle bundle = new Bundle();
		bundle.putString(Constant.CONTENT, content);
		
		FragmentDoodle fragment = new FragmentDoodle();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		jsonString = getArguments().getString(Constant.CONTENT);
		
		acceleration = 0.0f;
		currentAcceleration = SensorManager.GRAVITY_EARTH;
		lastAcceleration = SensorManager.GRAVITY_EARTH;
		
		enableAccelerometerListening();
		getSharedPreferences();
	}

	private void getSharedPreferences() {
		prefs = getActivity().getSharedPreferences(
			      Constant.DOODLE_SHARED_PREFS, Context.MODE_PRIVATE);
		
		if(prefs != null) {
			width = prefs.getInt(Constant.DOODLE_WIDTH, 15);
		} 
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_doodle, container, false);
		doodle = (DoodleView) view.findViewById(R.id.doodleView);
		
		penImageButton = (ImageButton) view.findViewById(R.id.penImageButton);
		eraserImageButton = (ImageButton) view.findViewById(R.id.eraserImageButton);
		sizeImageButton = (ImageButton) view.findViewById(R.id.sizeImageButton);
		clearAllImageButton = (ImageButton) view.findViewById(R.id.clearAllImageButton);
		
		penImageButton.setOnClickListener(listener);
		eraserImageButton.setOnClickListener(listener);
		sizeImageButton.setOnClickListener(listener);
		clearAllImageButton.setOnClickListener(listener);
		
		/*if(!jsonString.isEmpty()) {
			doodle.setBitmap(jsonString);
		}*/
        
        return view;
    }
	
	@Override
	public void onPause() {
		super.onPause();
		disableAccelerometerListening();
		
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(Constant.DOODLE_WIDTH, width);
		editor.commit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		enableAccelerometerListening();
		
		width = prefs.getInt(Constant.DOODLE_WIDTH, 15);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		disableAccelerometerListening();
		
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(Constant.DOODLE_WIDTH, width);
		editor.commit();
	}
	
	private OnClickListener listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.penImageButton:
					//canvasTool = CanvasTool.PENCIL;
					doodle.setDrawingColor(Color.WHITE);
					penImageButton.setBackgroundResource(R.drawable.border);
					eraserImageButton.setBackgroundResource(0);
					break;
					
				case R.id.eraserImageButton:
					//canvasTool = CanvasTool.ERASER;
					doodle.setDrawingColor(Color.argb(255, 39, 174, 96));
					eraserImageButton.setBackgroundResource(R.drawable.border);
					penImageButton.setBackgroundResource(0);
					break;
					
				case R.id.sizeImageButton:
					createSizeDialog();
					break;
				
				case R.id.clearAllImageButton:
					showClearAllDialog();
					break;
					
				default:
					
					break;
			}
		}
	};
	
	private void enableAccelerometerListening() {
		sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(sensorEventListener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	private void disableAccelerometerListening() {
		if(sensorManager != null) {
			
			sensorManager.unregisterListener(sensorEventListener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			
			sensorManager = null;
		
		}
	}
	
	private SensorEventListener sensorEventListener =
			new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			
			if(!dialogIsDisplayed.get()) {
				
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				
				lastAcceleration = currentAcceleration;
				currentAcceleration = x*x + y*y + z*z;
				acceleration = currentAcceleration * (currentAcceleration - lastAcceleration);
				
				//if shake hard enough, display a dialog to confirm if user wants to clear all
				if(acceleration > ACCELERATION_THRESHOLD) {
					showClearAllDialog();
				}
				
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	};
	
	private void showClearAllDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.message_erase);
		builder.setCancelable(false);
		
		Dialog.OnClickListener listener = new Dialog.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
					case Dialog.BUTTON_POSITIVE:
						dialogIsDisplayed.set(false);
						doodle.clear();
						break;
						
					case Dialog.BUTTON_NEGATIVE:
						dialogIsDisplayed.set(false);
						dialog.cancel();
						break;
				}
			}
			
		};
		
		builder.setPositiveButton(R.string.button_erase, listener);
		builder.setNegativeButton(R.string.button_cancel, listener);
		
		dialogIsDisplayed.set(true);
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void createSizeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Set Width");
		builder.setCancelable(false);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				width = widthSeekBar.getProgress();
				doodle.setLineWidth(width);

				dialog.cancel();
			}
		});
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.canvas_size_custom_dialog, null);
		
		widthSeekBar = (SeekBar) view.findViewById(R.id.widthSeekBar);
		widthSeekBar.setOnSeekBarChangeListener(seekBarListener);
		
		imageView = (ImageView) view.findViewById(R.id.widthImageView);
		
		progressTextView = (TextView) view.findViewById(R.id.progressTextView);
		
		widthSeekBar.setProgress(width);
			
		/*cancelButton = (Button) dialog.findViewById(R.id.cancelButton);	
		cancelButton.setOnClickListener(dialogListener);
		
		saveButton = (Button) dialog.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(dialogListener); */
		
		defaultButton = (Button) view.findViewById(R.id.defaultButton);
		defaultButton.setOnClickListener(dialogListener);
		
		builder.setCustomTitle(view);
		
		dialog = builder.create();
		dialog.show();
	}
	
	private OnClickListener dialogListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {	
				case R.id.defaultButton:
					
					widthSeekBar.setProgress(DEFAULT_WIDTH);
					setImageView(DEFAULT_WIDTH);
					
					break;
			}
		}

	};
	
	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			setImageView(progress);
			progressTextView.setText(String.valueOf(progress));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void setImageView(int width) {
		Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(width);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		
		bitmap.eraseColor(Color.argb(255, 39, 174, 96));
		canvas.drawLine(50, 50, 370, 50, paint);
		
		imageView.setImageBitmap(bitmap);
		
	}
	
	public String getContent() {
		return doodle.getContent();
	}

	public boolean isEmpty() {
		return doodle.isEmpty();
	}

	

}
