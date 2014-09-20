package com.adrapp.jornais;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockActionBarDrawerToggle;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.adrapp.jofav.R;
import com.adrapp.jornais.DragAndDropList.AddSortActivity;
import com.adrapp.jornais.LeftDrawer.CriaLista;
import com.adrapp.jornais.LeftDrawer.Item;
import com.adrapp.jornais.LeftDrawer.TwoTextArrayAdapter;

public class MainActivity extends SherlockActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SherlockActionBarDrawerToggle mDrawerToggle;
	private SiteFragment site;
	private TwoTextArrayAdapter textAdapter;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light);

		super.onCreate(savedInstanceState);
		// Adds Progress bar Support
		//requestWindowFeature(Window.FEATURE_PROGRESS);
		
		
		//setSupportProgressBarIndeterminateVisibility(true);

		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		//copiar o ficheiro com os sites para a mem Interna, se ainda nao existir
		fileExistance("drawerLinks.jrn");
		
		// set up the drawer's list view with items and click listener
		List<Item> items = new CriaLista(this).getItems();
		textAdapter = new TwoTextArrayAdapter(this,items);
		mDrawerList.setAdapter(textAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new SherlockActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			@Override
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			site = new SiteFragment();
			site.setTextAdapter(textAdapter);
			selectItem(1);
		}
		
		// create new ProgressBar and style it
		final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 24));
		//progressBar.setProgress(65);

		// retrieve the top view of our application
		final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
		decorView.addView(progressBar);
		
		// Here we try to position the ProgressBar to the correct position by looking
		// at the position where content area starts. But during creating time, sizes 
		// of the components are not set yet, so we have to wait until the components
		// has been laid out
		// Also note that doing progressBar.setY(136) will not work, because of different
		// screen densities and different sizes of actionBar
		ViewTreeObserver observer = progressBar.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
		        View contentView = decorView.findViewById(android.R.id.content);
		        progressBar.setY(contentView.getY() - 10);

		        ViewTreeObserver observer = progressBar.getViewTreeObserver();
		        observer.removeGlobalOnLayoutListener(this);
		    }
		});
		site.setProgress(progressBar);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_sites, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true; 
		}
		// Handle action buttons
		switch(item.getItemId()) {
		case R.id.action_refresh:
			site.fazRefresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Item item = textAdapter.getItem(position);
		site.loadURL(item.getSite());

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, site).commit();

		// close the drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public void editSitesOnClickHandler(View v) {
		Intent intent = new Intent(this, AddSortActivity.class);
		startActivityForResult(intent, 0);
	}
	
	public void guardaSites(){
		FileOutputStream fos = null;
		PrintStream pStream = null;
		try {
			fos = this.openFileOutput("drawerLinks.jrn", Context.MODE_PRIVATE);
			pStream = new PrintStream(fos);

			if (pStream != null) {
				pStream.println (this.textAdapter.toString());
				pStream.close();
				Log.d("Gravacao", "Save guardado em memoria interna!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void fileExistance(String fname){
		File file = getBaseContext().getFileStreamPath(fname);
		if(!file.exists()){ // se o ficheiro nao existir na memoria interna do telemovel
			try {
				CopyFromAssetsToStorage(fname); //copiar
			} catch (IOException e) {
				e.printStackTrace();
			}
		}    
	}

	private void CopyFromAssetsToStorage(String SourceFile) throws IOException {
		InputStream IS = this.getAssets().open(SourceFile);
		FileOutputStream fos = this.openFileOutput("drawerLinks.jrn", Context.MODE_PRIVATE);
		CopyStream(IS, fos);
		fos.flush();
		fos.close();
		IS.close();
	}
	private void CopyStream(InputStream Input, OutputStream Output) throws IOException {
		byte[] buffer = new byte[5120];
		int length = Input.read(buffer);
		while (length > 0) {
			Output.write(buffer, 0, length);
			length = Input.read(buffer);
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		Log.d("shsdgd", "on activity result");
		List<Item> items = new CriaLista(this).getItems();
		textAdapter = new TwoTextArrayAdapter(this,items);
		mDrawerList.setAdapter(textAdapter);
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		site.onConfigurationChanged(newConfig);

		super.onConfigurationChanged(newConfig);

		//		// Load the layout resource for the new configuration
		//	    setContentView(R.layout.activity_main);

		// Reinitialize the UI
		site.initUI();

		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);

		// Save the state of the WebView
		site.onSaveInstanceState(outState);
	}
	
	public TwoTextArrayAdapter getTextAdapter(){
		return textAdapter;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		Log.d("Restore", "RESTOOOOOOOOOOOOOOOOREEEEEEEE");
		// Restore the state of the WebView
		super.onRestoreInstanceState(savedInstanceState);
//		site.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		//fechar a drawer se esta estiver aberta
		if(mDrawerLayout.isDrawerOpen(mDrawerList)){
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}

		//caso exista uma webview aberta e o site que esta aberto puder retroceder
		if(site != null){
			if (site.canGoBack()) {
				site.goBack(); //retroceder
				return;
			}
		}

		// Otherwise defer to system default behavior.
		super.onBackPressed();
	}
}