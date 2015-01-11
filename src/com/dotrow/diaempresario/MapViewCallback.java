package com.dotrow.diaempresario;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/03/14 06:55 PM
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.dotrow.diaempresario.sponsors.Sponsor;
import com.dotrow.diaempresario.sponsors.SponsorDetailActivity;
import com.dotrow.diaempresario.sponsors.SponsorListLoader;
import flexjson.JSONDeserializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MapViewCallback extends WebViewClient implements LoaderManager.LoaderCallbacks<List<Sponsor>> {
	private List<Sponsor> sponsors;
	private String sponsorId;
	private Activity activity;
	private ProgressBar progressBar;

	public MapViewCallback( Activity activity, ProgressBar progressBar ) {
		this.activity = activity;
		this.progressBar = progressBar;
	}

	@Override
	public boolean shouldOverrideUrlLoading( WebView view, String url ) {
		if( url.startsWith( "sponsor:" )){
			//System( "Redirecting to sponsor: " + url.substring( url.indexOf( ':' ) ) );
			sponsorId = url.substring( url.indexOf( ':' ) + 1 );
			if( sponsorsFileExist() ) {
				String json = readSponsorsFile();

				JSONDeserializer<List<Sponsor>> deserializer = new JSONDeserializer<List<Sponsor>>();
				sponsors = deserializer
						.use( "values", Sponsor.class )
						.deserialize( json );
				redirectToActivity();
			} else {
				activity.getLoaderManager().initLoader( 0, null, this ).forceLoad();
			}
			return true;
		}

		return false;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		progressBar.setVisibility( View.GONE);
		super.onPageFinished(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		progressBar.setVisibility(View.VISIBLE);
		super.onPageStarted(view, url, favicon);
	}

	private void redirectToActivity(){

		Sponsor sponsor = null;
		for( Sponsor _sponsor : sponsors ) {
			if( _sponsor.getId().equals( sponsorId ) ){
				sponsor = _sponsor;
				break;
			}
		}

		if( sponsor != null ){
			Intent i = new Intent( activity.getApplicationContext(), SponsorDetailActivity.class );
			i.putExtra( "sponsor", sponsor );
			activity.startActivity( i );
			//activity.overridePendingTransition(0, R.animator.event_detail_slidedown);
		}
	}

	@Override
	public Loader<List<Sponsor>> onCreateLoader( int i, Bundle bundle ) {
		return new SponsorListLoader( activity.getApplicationContext() );
	}

	@Override
	public void onLoadFinished( Loader<List<Sponsor>> listLoader, List<Sponsor> sponsors ) {
		if( sponsors != null ) {
			this.sponsors = sponsors;
			redirectToActivity();
		} else if( sponsors == null ) {
			SponsorListLoader loader = (SponsorListLoader) listLoader;
			ErrorMessage errorMessage = loader.getErrorMessage();
			AlertDialog.Builder builder = new AlertDialog.Builder( activity );
			builder.setMessage( errorMessage.getMessage() )
					.setTitle( errorMessage.getTitle() )
					.setCancelable( false )
					.setNeutralButton( "Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick( DialogInterface dialog, int id ) {
									dialog.cancel();
									activity.getLoaderManager().destroyLoader( 0 );
								}
							} );
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public void onLoaderReset( Loader<List<Sponsor>> listLoader ) {
	}

	private boolean sponsorsFileExist() {
		File file = new File( activity.getApplicationContext().getFilesDir(), "sponsors.json" );
		return file.exists();
	}

	private String readSponsorsFile() {
		String ret = "";
		try {
			InputStream inputStream = activity.openFileInput( "sponsors.json" );

			if( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
				BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while( ( receiveString = bufferedReader.readLine() ) != null ) {
					stringBuilder.append( receiveString );
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}
}