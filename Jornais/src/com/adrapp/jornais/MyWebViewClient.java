package com.adrapp.jornais;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {  
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.endsWith(".mp4")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(url), "video/*");

			view.getContext().startActivity(intent);
			return true;
		} 
		else {
			return super.shouldOverrideUrlLoading(view, url);
		}
	}
	
	
}
