
package com.example.viewfinderee368;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

// ----------------------------------------------------------------------

public class ViewfinderEE368 extends Activity {    
    private Preview mPreview;
    private DrawOnTop mDrawOnTop;
    //private View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Create our Preview view and set it as the content of our activity.
        // Create our DrawOnTop view.
        //mDrawOnTop = new View(this);
        mDrawOnTop = new DrawOnTop(this);
        mPreview = new Preview(this,mDrawOnTop);
        setContentView(mPreview);
        addContentView(mDrawOnTop, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
}

//----------------------------------------------------------------------

class DrawOnTop extends View {
	Bitmap mBitmap;
	int width=0;
	int height=0;
	byte[] mYUVData;
	int[] mRGBData;
	
    public DrawOnTop(Context context) {
    	super(context);
 
    }
    
    protected void onDraw(Canvas canvas) {
    	if(mBitmap!=null){

    	
    	decodeYUV420SP(mRGBData, mYUVData, width, height);
    	mBitmap.setPixels(mRGBData, 0, width, 0, 0, 
    			width, height);
    	
    	
    	Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
    	canvas.drawColor(Color.BLUE);
    	canvas.drawBitmap(mBitmap,rect,rect,null);
    	
    	}
    }
    
    
    static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
    	final int frameSize = width * height;
    	
    	for (int j = 0, yp = 0; j < height; j++) {
    		int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
    		for (int i = 0; i < width; i++, yp++) {
    			int y = (0xff & ((int) yuv420sp[yp])) - 16;
    			if (y < 0) y = 0;
    			if ((i & 1) == 0) {
    				v = (0xff & yuv420sp[uvp++]) - 128;
    				u = (0xff & yuv420sp[uvp++]) - 128;
    			}
    			
    			int y1192 = 1192 * y;
    			int r = (y1192 + 1634 * v);
    			int g = (y1192 - 833 * v - 400 * u);
    			int b = (y1192 + 2066 * u);
    			
    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
    			
    			rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    		}
    	}
    }
    
}

// ----------------------------------------------------------------------

class Preview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;
    DrawOnTop mDrawOnTop;
    boolean mFinished;

    Preview(Context context, DrawOnTop drawOnTop) {
        super(context);
        
        mDrawOnTop = drawOnTop;
        mFinished = false;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
           mCamera.setPreviewDisplay(holder);
           
           // Preview callback used whenever new viewfinder frame is available
           mCamera.setPreviewCallback(new PreviewCallback() {
        	  public void onPreviewFrame(byte[] data, Camera camera)
        	  {
        		  if ( (mDrawOnTop == null) || mFinished )
        			  return;

        		  Camera.Parameters params = camera.getParameters();
        		  if(mDrawOnTop.mBitmap ==null){
        			  
        			  mDrawOnTop.height = params.getPreviewSize().height;
        			  mDrawOnTop.width = params.getPreviewSize().width;
        			  mDrawOnTop.mRGBData = new int[mDrawOnTop.width * mDrawOnTop.height]; 
        			  mDrawOnTop.mYUVData = new byte[data.length];  
        		  }
        			 mDrawOnTop.mBitmap = Bitmap.createBitmap(mDrawOnTop.width, mDrawOnTop.height, Bitmap.Config.RGB_565);
        		  
        			 System.arraycopy(data, 0, mDrawOnTop.mYUVData, 0, data.length);

    			  mDrawOnTop.invalidate();
        	  }
           });
        } 
        catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
    	mFinished = true;
    	mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(320, 240);
        mCamera.setDisplayOrientation(180);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_NIGHT);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        
    }

}
