package com.example.viewfinderee368;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class Settingactiv extends Activity {

	public int tempselected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingactiv);
		
		Button OKButton=(Button)findViewById(R.id.Button01);
		
		 OKButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on click
	            	ViewfinderEE368.ColorText.setTextColor(tempselected);
	            	 finish();
	            }
	        });
		 Button CancelButton=(Button)findViewById(R.id.Button02);
			
		 CancelButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on click
	            	  // ColorText.setTextColor(Color.BLACK);
	            	 finish();
	            }
	        });
		 
		 RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiocolor);
	        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

	            @Override
	            public void onCheckedChanged(RadioGroup group, int checkedId) 
	            {
	                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
	                String text = checkedRadioButton.getText().toString();

	              if(text.equals("Red"))
	              {
	            	  tempselected=Color.RED;
	            	  
	              }
	              else if(text.equals("Green"))
	              {
	            	  tempselected=Color.GREEN;
	            	  
	              }
	              else if(text.equals("Blue"))
	              {
	            	  tempselected=Color.BLUE;
	            	  
	              }
	              else if(text.equals("Cyan"))
	              {
	            	  tempselected=Color.CYAN;
	            	  
	              }
	              else if(text.equals("Purple"))
	              {
	            	  tempselected=Color.MAGENTA;
	            	  
	              }
	              else if(text.equals("Violet"))
	              {
	            	  tempselected=Color.DKGRAY;
	            	  
	              }
	              else if(text.equals("Yellow"))
	              {
	            	  tempselected=Color.YELLOW;
	            	  
	              }
	              else 
	              {
	            	  
	              }
	               
	            }
	        });

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settingactiv, menu);
		return true;
	}

}
