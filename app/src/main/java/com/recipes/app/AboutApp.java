package com.recipes.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.recipes.app.R;

public class AboutApp extends Activity{
	
	TextView txtAbout;
	
	/**
	 * the value of this attribute is about application description. 
	 * you can change it with your own. it also accept html tag.
	 */
	String aboutContent = "Recipes App is an application under Android platform " +
			"&copy; 2017 Recipes App. All rights reserved.";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);
		
		txtAbout = (TextView) findViewById(R.id.txtAbout);
		txtAbout.setText(Html.fromHtml(aboutContent));
	}
}
