package com.dotrow.diaempresario;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import flexjson.JSONDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 22/01/14 11:08 PM
 */
public class SigninLoader extends AsyncTaskLoader<Boolean> {
	private ErrorMessage errorMessage;
	private String uid;

	public SigninLoader( String uid, Context context ) {
		super( context );
		this.uid = uid;
	}

	@Override
	public Boolean loadInBackground() {
		HTTPRequest request = new HTTPRequest( getContext() );
		request.setHost( "http://conferences.dotrow.com" );
		request.setPath( "/signin/" + uid );

		HTTPResponse response = request.execute();
		switch( response.getResponseCode() ) {
			case 200:
				JSONDeserializer<HashMap> deserializer = new JSONDeserializer<HashMap>();
				Map data = deserializer.deserialize( response.getResponseBody() );
				// TODO: generate more complex id and cipher that based on phone specs
				saveConfigurations( data );
				return true;
			case 404:
				errorMessage = new ErrorMessage( "El código de registro no existe", "Vuelva a introducir o escanear su código de registro" );
				break;
			default:
				errorMessage = new ErrorMessage( "Error: " + response.getResponseCode(), response.getResponseMessage() );
		}

		return false;
	}

	private boolean saveConfigurations( Map data ) {
		SharedPreferences settings = getContext().getSharedPreferences( "Configuration", 0 );
		SharedPreferences.Editor editor = settings.edit();
		editor.putString( "hash", data.get( "hash" ) + "" );
		editor.putLong( "mindate", (Long) data.get( "mindate" ) );
		editor.putLong( "maxdate", (Long) data.get( "maxdate" ) );
		return editor.commit();
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
}
