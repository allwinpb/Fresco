package icreate.doodle;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
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
		previousDoodleContent = jsonString;
		Bitmap previousDoodle = convertFromJSONToImage(previousDoodleContent);
		setBackground(new BitmapDrawable(getResources(), previousDoodle));
		Log.d("DoodleView", "setBackgroundOfPreviousDoodle");
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
		//bitmap.eraseColor(Color.argb(255, 39, 174, 96));
		bitmap.eraseColor(Color.TRANSPARENT);
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
}
