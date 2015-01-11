package com.dotrow.diaempresario;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.qozix.mapview.MapView;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 27/01/14 08:02 PM
 */
public class MapFragment extends Fragment {

	private MapView mapView;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.map, container, false );


		WebView wv = (WebView) rootView.findViewById( R.id.webView1 );
		//wv.setWebChromeClient( new WebChromeClient() );
		wv.setWebViewClient( new MapViewCallback( getActivity(), (ProgressBar) rootView.findViewById( R.id.progressBar ) ) );
		WebSettings webSettings = wv.getSettings();
		webSettings.setBuiltInZoomControls( true );
		//webSettings.setAllowUniversalAccessFromFileURLs(true);
		webSettings.setJavaScriptEnabled( true );
		webSettings.setJavaScriptCanOpenWindowsAutomatically( true );
		wv.loadUrl( "file:///android_asset/mapa/index.html" );
		//wv.loadUrl( "http://google.com" );

		return rootView;
	}

}
