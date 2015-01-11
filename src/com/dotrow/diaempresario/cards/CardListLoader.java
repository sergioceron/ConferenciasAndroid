package com.dotrow.diaempresario.cards;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.dotrow.diaempresario.ErrorMessage;
import com.dotrow.diaempresario.HTTPRequest;
import com.dotrow.diaempresario.HTTPResponse;
import com.dotrow.diaempresario.SigninActivity;
import flexjson.JSONDeserializer;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 22/01/14 11:08 PM
 */
public class CardListLoader extends AsyncTaskLoader<ContactCard> {
	private ErrorMessage errorMessage;
	private String uid;

	public CardListLoader( String uid, Context context ) {
		super( context );
		this.uid = uid;
	}

	@Override
	public ContactCard loadInBackground() {
		ContactCard contact = null;

		HTTPRequest request = new HTTPRequest( getContext() );
		request.setHost( "http://conferences.dotrow.com" );
		request.setPath( "/api/users/" + uid + "?hash=" + SigninActivity.getHash( getContext().getApplicationContext() )  );

		HTTPResponse response = request.execute();
		switch( response.getResponseCode() ) {
			case 200:
				JSONDeserializer<ContactCard> deserializer = new JSONDeserializer<ContactCard>();
				contact = deserializer
						.use( null, ContactCard.class )
						.deserialize( response.getResponseBody() );
		        break;
			case 404:
				errorMessage = new ErrorMessage( "El código de registro no existe", "Vuelva a escanear el código de barras" );
				break;
			default:
				errorMessage = new ErrorMessage( "Error: " + response.getResponseCode(), response.getResponseMessage() );
		}

		return contact;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
}
