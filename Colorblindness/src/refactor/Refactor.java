package refactor;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

enum COLOR{Red,Orange,Yellow,Green,Cyan,Blue,Violet,Purple,none}

public class Refactor {
	
	static final Map<COLOR, Integer> HSV_VALUES = new HashMap<COLOR, Integer>();
	static 
	{
		HSV_VALUES.put(COLOR.Red, 0);
		HSV_VALUES.put(COLOR.Orange, 30);
		HSV_VALUES.put(COLOR.Yellow, 60);
		HSV_VALUES.put(COLOR.Green, 120);
		HSV_VALUES.put(COLOR.Cyan, 180);
		HSV_VALUES.put(COLOR.Blue, 240);
		HSV_VALUES.put(COLOR.Violet, 270);
		HSV_VALUES.put(COLOR.Purple, 300);
	}
	
	static final int RADIUS = 7;

	COLOR guessAreaColor(float[] hsv, int position, int imageWidth) {
		Map<COLOR, Integer> colorCounts = new HashMap<COLOR, Integer>();
		
		for (COLOR col : COLOR.values()) {
			colorCounts.put(col, 0);
		}
		
		for(int row = -RADIUS; row <= RADIUS; row ++) {
			for (int col = -RADIUS; col <= RADIUS; col++) {
				int posOffset = (((row * imageWidth) + col) * 3);
				int pos = position + posOffset;
				if (pos >= 0 && pos < hsv.length) {
					COLOR pixelColor = guessPixelColor(hsv, pos);
					colorCounts.put(pixelColor, (colorCounts.get(pixelColor) + 1));
				}
			}
		}
		
		COLOR mode = COLOR.none;
		for(COLOR col : colorCounts.keySet()) {
			if (colorCounts.get(col) > colorCounts.get(mode)) {
				mode = col;
			}
		}
		
		return mode;
	}
	
	COLOR guessPixelColor(float [] hsv, int position)
	{
			if(hsv[position]<=15 || hsv[position]>=316)
			{	
				return COLOR.Red;
			}
			else if(hsv[position]>=16 && hsv[position]<=45)
			{
				return COLOR.Orange;
				
			}else if(hsv[position]>=46 && hsv[position]<=75)
			{
				return COLOR.Yellow;
				
			}else if(hsv[position]>=76 && hsv[position]<=165)
			{
				return COLOR.Green;
				
			}else if(hsv[position]>=166 && hsv[position]<=195)
			{
				return COLOR.Cyan;
				
			}else if(hsv[position]>=196 && hsv[position]<=255)
			{
				return COLOR.Blue;
				
			}else if(hsv[position]>=256 && hsv[position]<=285)
			{
				return COLOR.Violet;
				
			}else if(hsv[position]>=286 && hsv[position]<=315)
			{
				return COLOR.Purple;
			}
	    	
	       return COLOR.none;
	    }
		    
	byte [] convertFromBitmapToRgbByteArray(Bitmap bitmap)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4 * bitmap.getWidth() * bitmap.getHeight());
		 bitmap.copyPixelsToBuffer(buffer); 
		 return buffer.array();
	}
	
	float [] convertFromRgbToHsv(byte [] imagebyte)
	{
		int size=(imagebyte.length /4)*3;
		float [] hsvarray=new float[size];
		int j=0;
		float []temp=new float[3];
		for(int i=0;i<imagebyte.length;i+=4)
		{
			android.graphics.Color.RGBToHSV(imagebyte[i],imagebyte[i+1],imagebyte[i+2],temp);
			hsvarray[j]=temp[i];
			hsvarray[j+1]=temp[i+1];
			hsvarray[j+2]=temp[i+2];
			j+=3;
		}
		return hsvarray;
	}
	
	int[] convertFromHsvToRgb(float h, float s, float v) {
		int r, g, b;
		
		float hFloat = h/60;
		int hInt = (int) (h/60);
		float f = hFloat - hInt;
		float p = v * (1 - s);
		float q = v * (1 - s * f);
		float t = v * (1-s * (1 - f));
		
		switch(hInt) {
		case 0:
			r = (int) v;
			g = (int) t;
			b = (int) p;
			break;
		case 1:
			r = (int) q;
			g = (int) v;
			b = (int) p;
			break;
		case 2:
			r = (int) p;
			g = (int) v;
			b = (int) t;
			break;
		case 3:
			r = (int) p;
			g = (int) q;
			b = (int) v;
			break;
		case 4:
			r = (int) t;
			g = (int) p;
			b = (int) v;
			break;
		default:	//case 5
			r = (int) v;
			g = (int) p;
			b = (int) q;
			break;
		}
		
		return new int[]{r, g, b};
	}
	
	Bitmap convertFromHsvToBitmap(float[] hsv, int width, int height) {
		int size=(hsv.length) * 4 / 3;
		int [] rgb=new int[size];
		int j=0;
		int [] temp=new int[3];
		for(int i=0;i<hsv.length;i+=3)
		{
			temp = convertFromHsvToRgb(hsv[i],hsv[i+1],hsv[i+2]);
			rgb[j]=temp[i];
			rgb[j+1]=temp[i+1];
			rgb[j+2]=temp[i+2];
			rgb[j+3] = 0;
			j+=4;

		}
		return Bitmap.createBitmap(rgb, width, height, Bitmap.Config.ARGB_8888);
	}
	
	Bitmap change(Bitmap bitmap, int x, int y, COLOR newColor)
	{
		
		float[] hsv=convertFromRgbToHsv(convertFromBitmapToRgbByteArray(bitmap));
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int position = (y * width + x) * 3;
		COLOR targetColor = guessAreaColor(hsv, position, width);
		int newColorValue = HSV_VALUES.get(newColor);
		
		for(int i = 0; i < hsv.length; i+=3)
		{
			COLOR c = guessPixelColor(hsv, i);
			if (c == targetColor)
			{
				hsv[i] = newColorValue;
			}
		}
		return convertFromHsvToBitmap(hsv, width, height);
	}
}
