package com.dotrow.diaempresario.social;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 10/02/14 11:44 AM
 */
public class SocialService extends IntentService {
	private final static String INTENT_RECEIVER = "com.example.Conferencias.SocialReceiver";
	private final static String UPDATES_URL = "http://www.dotrow.com/ceron/social/index.php";
	private final static int NEW_UPDATES = 1;

	public SocialService() {
		super( "Twitter Service" );
	}

	@Override
	protected void onHandleIntent( Intent intent ) {
		SharedPreferences settings = getSharedPreferences( "Posts", 0 );
		long currentVersion = settings.getLong( "version", 0l );

		PostJson postJson = new PostJson();

		String responseJson = request( UPDATES_URL + "?version=" + currentVersion );

		/*if( responseJson != null ) {
			JSONDeserializer<PostJson> deserializer = new JSONDeserializer<PostJson>();
			postJson = deserializer
					.use( null, PostJson.class )
					.use( "posts.values", Post.class )
					.use( "posts.values.date", new DateTransformer( "EEE MMM dd HH:mm:ss ZZZZZ yyyy" ) )
					.deserialize( responseJson );

			if( postJson.getStatus() == NEW_UPDATES ) {
				savePostsFile( responseJson );

				SharedPreferences.Editor editor = settings.edit();
				editor.putLong( "version", postJson.getVersion() );
				editor.commit();
			}
		}*/

		broadcastResult( postJson );
	}

	private void broadcastResult( PostJson result ) {
		Intent intent = new Intent( INTENT_RECEIVER );
		intent.putExtra( "result", result );
		sendBroadcast( intent );
	}

	private void savePostsFile( String json ) {
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput( "events.json", Context.MODE_PRIVATE );
			outputStream.write( json.getBytes() );
			outputStream.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private String request( String url ) {
		InputStream is = null;
		String response = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL( url ).openConnection();
			conn.setReadTimeout( 100 * 1000 );
			conn.setConnectTimeout( 150 * 1000 );
			conn.setRequestMethod( "GET" );
			conn.setDoInput( true );
			conn.connect();

			is = conn.getInputStream();

			response = readStream( is );
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if( is != null ) {
				try {
					is.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	private String readStream( InputStream in ) throws IOException {
		BufferedReader r;
		r = new BufferedReader( new InputStreamReader( in ) );
		StringBuilder total = new StringBuilder();
		String line;
		while( ( line = r.readLine() ) != null ) {
			total.append( line );
		}
		if( r != null ) {
			r.close();
		}
		in.close();
		return total.toString();
	}
}
