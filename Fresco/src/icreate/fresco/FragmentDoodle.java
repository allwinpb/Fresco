package icreate.fresco;

import java.util.concurrent.atomic.AtomicBoolean;

import icreate.doodle.DoodleView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDoodle extends Fragment {

	private DoodleView doodle;
	
	String jsonString = "";
	
	private float acceleration; 
	private float currentAcceleration; 
	private float lastAcceleration; 
	
	private static final int ACCELERATION_THRESHOLD = 15000;
	
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
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_doodle, container, false);
		doodle = (DoodleView) view.findViewById(R.id.doodleView);
		if(!jsonString.isEmpty()) {
			doodle.setBitmap(jsonString);
		}
        
        return view;
    }
	
	@Override
	public void onPause() {
		super.onPause();
		
		disableAccelerometerListening();
	}
	
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
				
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	};
	
	public String getContent() {
		return doodle.getContent();
	}


}
