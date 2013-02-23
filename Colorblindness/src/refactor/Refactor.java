package refactor;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;

enum color{Red,Orange,Yellow,Green,Cyan,Blue,Violet,Purple,none}

public class Refactor {

		color colorguesser(float [] hsv,int position)
		{
				if(hsv[position]<=15 || hsv[position]>=316)
				{	
					return color.Red;
				}
				else if(hsv[position]>=16 && hsv[position]<=45)
				{
					return color.Orange;
					
				}else if(hsv[position]>=46 && hsv[position]<=75)
				{
					return color.Yellow;
					
				}else if(hsv[position]>=76 && hsv[position]<=165)
				{
					return color.Green;
					
				}else if(hsv[position]>=166 && hsv[position]<=195)
				{
					return color.Cyan;
					
				}else if(hsv[position]>=196 && hsv[position]<=255)
				{
					return color.Blue;
					
				}else if(hsv[position]>=256 && hsv[position]<=285)
				{
					return color.Violet;
					
				}else if(hsv[position]>=286 && hsv[position]<=315)
				{
					return color.Purple;
				}
		    	
		       return color.none;
		    }
		    
		byte [] covertfrombitmaptobytearray(Bitmap bitmap)
		{
			ByteBuffer buffer = ByteBuffer.allocate(4 * bitmap.getWidth() * bitmap.getHeight());
			 bitmap.copyPixelsToBuffer(buffer); 
			 return buffer.array();
		}
		
		float [] covertfromrgbtohsv(byte [] imagebyte)
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
		
		float[] change(Bitmap bitmap,int mode)
		{
			
			float[] hsv=covertfromrgbtohsv(covertfrombitmaptobytearray(bitmap));
			float[] res=new float[hsv.length];
			for(int i=0;i<hsv.length;i+=3)
			{
				color c=colorguesser(hsv,i);
				switch(c)
				{
				case Red:
				case Orange:
				case Yellow:
				case Green:
				case Cyan:
				case Blue:
				case Violet:
				case Purple:
				}
			}
			return res;
		}
	
}
