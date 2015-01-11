package com.dotrow.diaempresario.events;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import com.dotrow.diaempresario.ErrorMessage;
import com.dotrow.diaempresario.HTTPRequest;
import com.dotrow.diaempresario.HTTPResponse;
import flexjson.JSONDeserializer;
import flexjson.locators.TypeLocator;
import flexjson.transformer.DateTransformer;

import java.io.FileOutputStream;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 22/01/14 11:08 PM
 */
public class EventListLoader extends AsyncTaskLoader<List<Event>> {
	private ErrorMessage errorMessage;

	public EventListLoader( Context context ) {
		super( context );
	}

	@Override
	public List<Event> loadInBackground() {
		SharedPreferences settings = getContext().getSharedPreferences( "Events", 0 );
		String version = settings.getString( "version", "0" );

		List<Event> events = null;

		HTTPRequest request = new HTTPRequest( getContext() );
		request.setHost( "http://conferences.dotrow.com" );
		request.setPath( "/_events.json" );
		request.setLastModified( version );

		HTTPResponse response = request.execute();
		if( response.getResponseCode() == 200 ) {
			String body = response.getResponseBody().replaceAll( "_id", "id" );
			JSONDeserializer<List<Event>> deserializer = new JSONDeserializer<List<Event>>();

			TypeLocator<String> add = new TypeLocator<String>( "type" )
					.add( "conference", Conference.class )
					.add( "stand", Stand.class );

			events = deserializer
					.use( "values", add )
					.use( "values.date", new DateTransformer( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" ) )
					.use( "values.startDate", new DateTransformer( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" ) )
					.use( "values.endDate", new DateTransformer( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" ) )
					.deserialize( body );

			if( saveEventsFile( body ) ) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putString( "version", response.getLastModified() );
				editor.commit();
			}
		} else {
			errorMessage = new ErrorMessage( "Error: " + response.getResponseCode(), response.getResponseMessage() );
		}

		return events;
	}

	private boolean saveEventsFile( String json ) {
		FileOutputStream outputStream;
		try {
			outputStream = getContext().openFileOutput( "events.json", Context.MODE_PRIVATE );
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
