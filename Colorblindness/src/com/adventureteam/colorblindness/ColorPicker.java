package com.adventureteam.colorblindness;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ColorPicker extends Activity{

	PopupWindow popUp;
	LinearLayout mainLayout;
	Button but;
	boolean click = true;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		popUp = new PopupWindow(this);
		mainLayout = new LinearLayout(this);
		but = new Button(this);
		but.setText("Pick new color");
		but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (click) {
					popUp.showAtLocation(mainLayout, Gravity.BOTTOM, 10, 10);
					popUp.update(50, 50, 300, 80);
					click = false;
				} else {
					popUp.dismiss();
					click = true;
				}
				
			}
		});
	}
}
