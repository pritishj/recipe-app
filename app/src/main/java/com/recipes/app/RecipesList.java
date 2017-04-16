package com.recipes.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.recipes.app.R;

public class RecipesList extends Activity {
	
	ImageView imgAbout, imgSearchNav;
	Button btnSearch,btnSearchIngredients;
	EditText edtSearch;
	LinearLayout lytSearchForm;
	ListView listRecipes;
	ProgressBar prgLoading;
	TextView txtAlert;
	
	String RecipeNameKeyword = "";
	String RecipeIngredientKeyword = "";
	
	static DBHelper dbhelper;
	ArrayList<ArrayList<Object>> data;
	ListAdapter la;
	
	static int[] id;
	static String[] RecipeName;
	static String[] Preview;
	static String[] CookTime;
	static String[] Ingredients;
	
	
	/** This class is used to create custom listview */
	static class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context ctx;
		
		public ListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			ctx = context;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return RecipeName.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			
			if(convertView == null){
				convertView = inflater.inflate(R.layout.row, null);
				holder = new ViewHolder();
				holder.txtRecipeName = (TextView) convertView.findViewById(R.id.txtRecipeName);
				holder.txtReadyIn = (TextView) convertView.findViewById(R.id.txtReadyIn);
				holder.imgPreview = (ImageView) convertView.findViewById(R.id.imgPreview);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			
			holder.txtRecipeName.setText(RecipeName[position]);
			holder.txtReadyIn.setText("Cook time "+CookTime[position]);
			int imagePreview = ctx.getResources().getIdentifier(Preview[position], "drawable", ctx.getPackageName());
			holder.imgPreview.setImageResource(imagePreview);
			
			
			return convertView;
		}
		
		static class ViewHolder {
			TextView txtRecipeName, txtReadyIn;
			ImageView imgPreview;
		}
		
	}

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_list);
        
        dbhelper = new DBHelper(this);
        la = new ListAdapter(this);
        
        imgAbout = (ImageView) findViewById(R.id.imgAbout);
        imgSearchNav = (ImageView) findViewById(R.id.imgSearchNav);
        btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearchIngredients = (Button) findViewById(R.id.btnSearchIngredients);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lytSearchForm = (LinearLayout) findViewById(R.id.lytSearchForm);
        listRecipes = (ListView) findViewById(R.id.listRecipes);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        
        /**
         * when this app's installed at the first time, code below will
         * copy database stored in assets to
         * /data/data/com.recipes.app/databases/
         */
        try {
			dbhelper.createDataBase();
		}catch(IOException ioe){
			throw new Error("Unable to create database");
		}
		
        /** then, the database will be open to use */
		try{
			dbhelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
		
		new getDataTask().execute();
        
		
		listRecipes.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				/**
				 * when one of item in the list is clicked, this app will access 
				 * RecipeDetail.class. it also send id value to that class
				 */
				Intent i = new Intent(RecipesList.this, RecipeDetail.class);
				i.putExtra("id_for_detail", id[position]);
				startActivity(i);
			}
		});
        
        
        imgSearchNav.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/** this code is used to hide and show the search form */
				if(lytSearchForm.getVisibility() == 8){
					lytSearchForm.setVisibility(0);
					imgSearchNav.setImageResource(R.drawable.nav_down);
				}else{
					lytSearchForm.setVisibility(8);
					imgSearchNav.setImageResource(R.drawable.nav_up);
				}
			}
		});
        
        btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RecipeNameKeyword = edtSearch.getText().toString();
				try{
					dbhelper.openDataBase();
				}catch(SQLException sqle){
					throw sqle;
				}
				new getDataTask().execute();
			}

		});

		btnSearchIngredients.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				RecipeIngredientKeyword = edtSearch.getText().toString();
				try{
					dbhelper.openDataBase();
				}catch(SQLException sqle){
					throw sqle;
				}
				new getDataTask().execute();
			}

		});
        
        imgAbout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/** when about icon is clicked, it will access AboutApp.class */
				Intent i = new Intent(RecipesList.this, AboutApp.class);
				startActivity(i);
			}
		});
    }
    
    /** this class is used to handle thread */
    public class getDataTask extends AsyncTask<Void, Void, Void>{
    	
    	getDataTask(){
    		if(!prgLoading.isShown()){
    			prgLoading.setVisibility(0);
				txtAlert.setVisibility(8);
    		}
    	}
    	
    	@Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
    		
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			getDataFromDatabase(RecipeNameKeyword,RecipeIngredientKeyword);
			RecipeIngredientKeyword=RecipeNameKeyword="";
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			prgLoading.setVisibility(8);
			if(id.length > 0){
				listRecipes.setVisibility(0);
				listRecipes.setAdapter(la);
			}else{
				txtAlert.setVisibility(0);
			}
			dbhelper.close();
		}
    }
    
    /**
     * this code is used to get data from database and store them
     * to array attributes
     */
    public void getDataFromDatabase(String RecipeNameKeyword, String RecipeIngredientKeyword){
    	data = dbhelper.getAllData(RecipeNameKeyword,RecipeIngredientKeyword);
    	
    	id = new int[data.size()];
    	RecipeName = new String[data.size()];
    	Preview = new String[data.size()];
    	CookTime = new String[data.size()];
    	Ingredients = new String[data.size()];
    	for(int i=0;i<data.size();i++){
    		ArrayList<Object> row = data.get(i);
    		
    		id[i] = Integer.parseInt(row.get(0).toString());
    		RecipeName[i] = row.get(1).toString();
    		Preview[i] = row.get(2).toString().trim();
    		CookTime[i] = row.get(3).toString();
    		Ingredients[i] = row.get(4).toString();
    	}
    }
   
    
    @Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}
    
    
}