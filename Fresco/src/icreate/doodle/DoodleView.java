package icreate.doodle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DoodleView extends View {
	
	// used to determine whether user moved a finger enough to draw again
	private static final float TOUCH_TOLERANCE = 10;
	
	private Bitmap bitmap; // drawing area for display or saving
	private Canvas bitmapCanvas; // used to draw on bitmap
	private Paint paintScreen; // use to draw bitmap onto screen
	private Paint paintLine; // used to draw lines onto bitmap
	private HashMap<Integer, Path> pathMap; // current Paths being drawn
	private HashMap<Integer, Point> previousPointMap; // current Points
	
	private String previousDoodleContent;

	public DoodleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		paintScreen = new Paint();
		
		paintLine =  new Paint();
		paintLine.setAntiAlias(true);
		paintLine.setColor(Color.WHITE);
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setStrokeWidth(15);
		paintLine.setStrokeCap(Paint.Cap.ROUND);
		
		pathMap = new HashMap<Integer, Path>();
		previousPointMap = new HashMap<Integer, Point>();
	}
	
	public void setBackgroundOfPreviousDoodle(String jsonString) {
		//Bitmap previousDoodle = convertFromJSONToImage(previousDoodleContent);
		//setBackground(new BitmapDrawable(getResources(), previousDoodle));
		Log.d("DoodleView", "setBackgroundOfPreviousDoodle");
		
		previousDoodleContent = jsonString;
		changeBitmap();
	}
	
	private void changeBitmap() {
		if(!previousDoodleContent.isEmpty()) {
			Bitmap previousDoodle = convertFromJSONToImage(previousDoodleContent);
			bitmap = Bitmap.createBitmap(previousDoodle);
			bitmapCanvas.drawBitmap(bitmap, 0, 0, paintScreen);
			invalidate();
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
	

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	
		bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);
		bitmap.eraseColor(Color.argb(255, 39, 174, 96));
		//bitmap.eraseColor(Color.TRANSPARENT);
	}
	
	public void clear() {
		pathMap.clear();
		previousPointMap.clear();
		bitmap.eraseColor(Color.argb(255, 39, 174, 96));
		invalidate();
	}
	
	public void setDrawingColor(int color) {
		paintLine.setColor(color);
	}
	
	public int getDrawingColor() {
		return paintLine.getColor();
	}
	
	public void setLineWidth (int width) {
		paintLine.setStrokeWidth(width);
	}
	
	public int getLineWidth() {
		return (int) paintLine.getStrokeWidth();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if(!previousDoodleContent.isEmpty()) {
			changeBitmap();
		}
		
		canvas.drawBitmap(bitmap, 0, 0, paintScreen);
		
		for(Integer key: pathMap.keySet()) 
			canvas.drawPath(pathMap.get(key), paintLine);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getActionMasked();
		int pointerIndex = event.getActionIndex();
		
		if(action == MotionEvent.ACTION_DOWN || 
		   action == MotionEvent.ACTION_POINTER_DOWN) {
			
			touchStarted(event.getX(), event.getY(), event.getPointerId(pointerIndex));
			
		} else if (action == MotionEvent.ACTION_UP ||
				   action == MotionEvent.ACTION_POINTER_UP) {
			
			touchEnded(event.getPointerId(pointerIndex));
			
		} else if (action == MotionEvent.ACTION_MOVE) {
			
			touchMoved(event);
		}
		
		invalidate();
		return true;
	}
	
	private void touchStarted (float x, float y, int lineID) {
		Path path;
		Point point;
		
		if(pathMap.containsKey(lineID)) {
			path = pathMap.get(lineID);
			path.reset();
			point = previousPointMap.get(lineID);
		} else {
			path = new Path();
			pathMap.put(lineID, path);
			point = new Point();
			previousPointMap.put(lineID, point);
		}
		
		path.moveTo(x, y);
		point.x = (int) x;
		point.y = (int) y;
	}
	
	private void touchEnded (int lineID) {
		Path path = pathMap.get(lineID);
		bitmapCanvas.drawPath(path, paintLine);
		path.reset();
	}
	
	private void touchMoved (MotionEvent event) {
		
		for(int i=0; i<event.getPointerCount(); i++) {
			int pointerId = event.getPointerId(i);
			int pointerIndex = event.findPointerIndex(pointerId);
			
			if(pathMap.containsKey(pointerId)) {
				
				float newX = event.getX(pointerIndex);
				float newY = event.getY(pointerIndex);
				
				Point point = previousPointMap.get(pointerId);
				Path path = pathMap.get(pointerId);
				
				float deltaX = Math.abs(newX - point.x);
				float deltaY = Math.abs(newY - point.y);
				
				if(deltaX >= TOUCH_TOLERANCE && deltaY >= TOUCH_TOLERANCE) {
					path.quadTo(point.x, point.y, (newX + point.x)/2, (newY + point.y)/2);
					point.x = (int) newX;
					point.y = (int) newY;
				}
				
			}
			
		}
	}
	
	
	
	private String convertFromImageToJSON(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);   
		byte[] byteArrayImage = baos.toByteArray(); 
		
		return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
	}
	
	public static Bitmap convertToMutable(Bitmap imgIn) {
	    try {
	        //this is the file going to use temporally to save the bytes. 
	        // This file will not be a image, it will store the raw image data.
	        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

	        //Open an RandomAccessFile
	        //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
	        //into AndroidManifest.xml file
	        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

	        // get the width and height of the source bitmap.
	        int width = imgIn.getWidth();
	        int height = imgIn.getHeight();
	        Config type = imgIn.getConfig();

	        //Copy the byte to the file
	        //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
	        FileChannel channel = randomAccessFile.getChannel();
	        MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
	        imgIn.copyPixelsToBuffer(map);
	        //recycle the source bitmap, this will be no longer used.
	        imgIn.recycle();
	        System.gc();// try to force the bytes from the imgIn to be released

	        //Create a new bitmap to load the bitmap again. Probably the memory will be available. 
	        imgIn = Bitmap.createBitmap(width, height, type);
	        map.position(0);
	        //load it back from temporary 
	        imgIn.copyPixelsFromBuffer(map);
	        //close the temporary file and channel , then delete that also
	        channel.close();
	        randomAccessFile.close();

	        // delete the temp file
	        file.delete();

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 

	    return imgIn;
	}

	public boolean isEmpty() {
		return pathMap.isEmpty() && previousPointMap.isEmpty();
	}
	
	public String getContent() {
		if(isEmpty())
			return "";
		
		return convertFromImageToJSON(bitmap);
	}
	
	public String getJSONString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		byte[] byteArrayImage = baos.toByteArray(); 
		
		return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
	}
	
	/*public void saveImage() {
		
		String fileName = "Doodlz" + System.currentTimeMillis();
		
		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, fileName);
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, "images/jpg");
		
		Uri uri = getContext().getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		
		try {
			
			OutputStream outStream = getContext().getContentResolver().openOutputStream(uri);
			
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			
			outStream.flush();
			outStream.close();
			
			//display toast message
			Toast toast = Toast.makeText(getContext(), R.string.message_saved, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, toast.getXOffset()/2, toast.getYOffset()/2);
			toast.show();
			
		} catch (FileNotFoundException e){
			
		} catch(IOException e) {
			Toast toast = Toast.makeText(getContext(), R.string.message_error_saving, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, toast.getXOffset()/2, toast.getYOffset()/2);
			toast.show();
		}
	}*/
	
	/*public void setBitmap(String jsonString) {
	JSONObject json;
	JSONObject jsonPath = null;
	JSONObject jsonPoint = null;
	
	try {
		json = new JSONObject(jsonString);
		jsonPath = json.getJSONObject(Constant.JSON_PATH);
		jsonPoint = json.getJSONObject(Constant.JSON_POINT);
	} catch (JSONException e) {
		e.printStackTrace();
	}
	
	
	pathMap = convertPathInteger(jsonPath);
	previousPointMap = convertPointInteger(jsonPoint);
	invalidate();
}

	private HashMap<Integer, Point> convertPointInteger(JSONObject jsonPoint) {
		HashMap<Integer, Point> points = new HashMap<Integer, Point>();
		Iterator<String> iter = jsonPoint.keys();
		
		while(iter.hasNext()) {
			String key = iter.next();
			Point point = null;
			
			try {
				point = (Point) jsonPoint.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			points.put(Integer.parseInt(key), point);
		}
		
		return points;
	}
	
	private HashMap<Integer, Path> convertPathInteger(JSONObject jsonPath) {
		HashMap<Integer, Path> paths = new HashMap<Integer, Path>();
		Iterator<String> iter = jsonPath.keys();
		
		while(iter.hasNext()) {
			String key = iter.next();
			Path path = null;
			
			try {
				path = (Path) jsonPath.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			paths.put(Integer.parseInt(key), path);
		}
		
		return paths;
	}*/
	
	/*
	public String getContent() {
		JSONObject json = new JSONObject();
		JSONObject points = new JSONObject(convertPointString(previousPointMap));
		JSONObject paths = new JSONObject(convertPathString(pathMap));
		
		try {
			json.put(Constant.JSON_BITMAP, getJSONString());
			json.put(Constant.JSON_PATH, paths);
			json.put(Constant.JSON_POINT, points);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
		
	}*/
	
	
	/*
	private Map<String, Point> convertPointString(HashMap<Integer, Point> previousPointMap) {
		Map<String, Point> map = new HashMap<String, Point>();
		
		for(Map.Entry <Integer, Point> entry : previousPointMap.entrySet()) {
			Integer key = entry.getKey();
			Point point = entry.getValue();
			
			map.put(String.valueOf(key), point);
		}
	
		return map;
	}
	
	private Map<String, Path> convertPathString(HashMap<Integer, Path> pathMap) {
		Map<String, Path> map = new HashMap<String, Path>();
		
		for(Map.Entry <Integer, Path> entry : pathMap.entrySet()) {
			Integer key = entry.getKey();
			Path path = entry.getValue();
			
			map.put(String.valueOf(key), path);
		}
	
		return map;
	}*/
	
	
}
