package com.dotrow.diaempresario.sponsors;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import com.dotrow.diaempresario.ErrorMessage;
import com.dotrow.diaempresario.HTTPRequest;
import com.dotrow.diaempresario.HTTPResponse;
import flexjson.JSONDeserializer;

import java.io.FileOutputStream;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 22/01/14 11:08 PM
 */
public class SponsorListLoader extends AsyncTaskLoader<List<Sponsor>> {
	private ErrorMessage errorMessage;

	public SponsorListLoader( Context context ) {
		super( context );
	}

	@Override
	public List<Sponsor> loadInBackground() {
		SharedPreferences settings = getContext().getSharedPreferences( "Sponsors", 0 );
		String version = settings.getString( "version", "0" );

		List<Sponsor> sponsors = null;

		HTTPRequest request = new HTTPRequest( getContext() );
		request.setHost( "http://conferences.dotrow.com" );
		request.setPath( "/_sponsors.json" );
		request.setLastModified( version );

		HTTPResponse response = request.execute();
		if( response.getResponseCode() == 200 ) {
			String body = response.getResponseBody().replaceAll( "_id", "id" );
			JSONDeserializer<List<Sponsor>> deserializer = new JSONDeserializer<List<Sponsor>>();
			sponsors = deserializer
					.use( "values", Sponsor.class )
					.deserialize( body );

			if( saveSponsorsFile( body ) ) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putString( "version", response.getLastModified() );
				editor.commit();
			}
		} else {
			errorMessage = new ErrorMessage( "Error: " + response.getResponseCode(), response.getResponseMessage() );
		}

		return sponsors;
	}

	private boolean saveSponsorsFile( String json ) {
		FileOutputStream outputStream;
		try {
			outputStream = getContext().openFileOutput( "sponsors.json", Context.MODE_PRIVATE );
			outputStream.write( json.getBytes() );
			outputStream.close();
			return true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
}
