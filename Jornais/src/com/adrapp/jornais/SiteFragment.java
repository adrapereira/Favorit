package com.adrapp.jornais;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adrapp.jofav.R;
import com.adrapp.jornais.LeftDrawer.TwoTextArrayAdapter;

public class SiteFragment extends Fragment{
	private WebView webview = null;
	private TwoTextArrayAdapter textAdapter;
	private Bundle saveState;
	private ProgressBar progressBar;
	private boolean carregar = false;

	public SiteFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		if(webview == null)
			initUI();

		if(carregar){
			Log.d("Carregar", "Carregar é true");
			if(saveState != null){
				Log.d("Carregar", "Carregar saveState");
				if(webview != null){
					webview.restoreState(saveState);
					carregar = false;
				}
			}
		}

		return webview;
	}

	public void initUI(){	
		if(webview == null){
			MainActivity act = (MainActivity)this.getActivity();
			act.setSupportProgressBarIndeterminateVisibility(true);
			
			Log.d("sup", "Criar WebView");
			webview = new WebView(getActivity());
			webview.setWebViewClient(new MyWebViewClient());

			// Sets the Chrome Client, and defines the onProgressChanged
			// This makes the Progress bar be updated.
			final Activity MyActivity = getActivity();
			webview.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int progress){
					//Make the bar disappear after URL is loaded, and changes string to Loading...
					MyActivity.setTitle("Carregando...");
					MyActivity.setProgress(progress * 100); //Make the bar disappear after URL is loaded

					// Return the app name after finish loading
					if(progress == 100)
						MyActivity.setTitle(R.string.app_name);

				}
			});

			webview.getSettings().setBuiltInZoomControls(true); 
			webview.getSettings().setSupportZoom(true);
			//			webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
			webview.getSettings().setJavaScriptEnabled(true); 
			webview.getSettings().setAllowFileAccess(true); 
			webview.getSettings().setDomStorageEnabled(true);
			//webview.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
			webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			webview.setScrollbarFadingEnabled(true);
			webview.getSettings().setLoadsImagesAutomatically(true);
			webview.setWebChromeClient(new WebChromeClient() { //set progress
	            @Override
				public void onProgressChanged(WebView view, int progress) 
	               {
	               if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
	                   progressBar.setVisibility(ProgressBar.VISIBLE);
	                   //txtview.setVisibility(View.VISIBLE);
	               }
	               progressBar.setProgress(progress);
	               if(progress == 100) {
	                   progressBar.setVisibility(ProgressBar.GONE);
	                   //txtview.setVisibility(View.GONE);
	               }
	            }
            });

			int i = 0;
			String site = null;
			
			if(textAdapter == null){
				Log.d("erro", "TextAdapter = null");
				MainActivity a = (MainActivity)this.getActivity();
				textAdapter = a.getTextAdapter();
				if(textAdapter == null)
					Log.d("erro", "ainda nao...");
			}
			while(i < textAdapter.getCount() && (site = textAdapter.getItem(i).getSite()) == null) i++; //procurar um site na lista

			if(site == null) site = "google.com"; //se nao existir nenhum site na lista, carregar o google
			else site = textAdapter.getItem(i).getSite();

//			if(isOnline())
//				webview.loadUrl(site);
//			else erroNoInternet();
			loadURL(site);
			
			
			
		}
	}

	public boolean canGoBack(){
		if(webview != null)	
			return webview.canGoBack();
		return false; //retornar falso caso nao exista webview, o que significa que algo correu mal e o onbackpressed ira terminar a aplicacao
	}

	public void goBack(){
		webview.goBack();
	}

	public void loadURL(String site){
		if(this.isAdded()){
			if(site != null){
				if(isOnline()){
					Log.d("LoadURL", "LoadUrl");
					webview.loadUrl(site);
				}else erroNoInternet();
			}
		}
	}
	
	public void setProgress(ProgressBar pro){
		progressBar = pro;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	private void erroNoInternet(){
		Toast toast = Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT);
		toast.show();
	}

	public void setTextAdapter(TwoTextArrayAdapter tAdapt){
		this.textAdapter = tAdapt;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){
		Log.d("Orientation", "On orientation changed");
		// Remove the WebView from the old placeholder
		webview.saveState(saveState);
		carregar  = true;

		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);

		// Save the state of the WebView
		webview.saveState(outState);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState){
		// Restore the state of the WebView
		this.onViewStateRestored(savedInstanceState);
		webview.restoreState(savedInstanceState);

		Log.d("showing", "" + webview.isShown());
	}

	public void fazRefresh(){
		webview.reload();
	}
}
