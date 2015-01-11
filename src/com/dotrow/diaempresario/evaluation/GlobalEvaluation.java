package com.dotrow.diaempresario.evaluation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.dotrow.diaempresario.*;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 8/03/14 01:55 PM
 */
public class GlobalEvaluation extends Activity {

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.evaluation_global );


		final RatingBar ratingEvent = ( (RatingBar) findViewById( R.id.ratingEvent ) );
		final RatingBar ratingConfe = ( (RatingBar) findViewById( R.id.ratingConferencistas ) );
		final RatingBar ratingNetwo = ( (RatingBar) findViewById( R.id.ratingNetworking ) );
		final RatingBar ratingOrgan = ( (RatingBar) findViewById( R.id.ratingOrganization ) );
		final RadioGroup options = ( (RadioGroup) findViewById( R.id.options ) );
		final EditText comment = ( (EditText) findViewById( R.id.comment ) );

		Button send = ( (Button) findViewById( R.id.btnSend ) );

		send.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				String opt = "";
				if( options.getCheckedRadioButtonId() != -1 ) {
					RadioButton selected = (RadioButton) findViewById( options.getCheckedRadioButtonId() );
					opt = selected.getText().toString();
				}
				evaluation( ratingEvent.getRating(), ratingConfe.getRating(), ratingNetwo.getRating(), ratingOrgan.getRating(), opt, comment.getText().toString() );
			}
		} );

		OnBootReceiver.setAlarm( this.getApplicationContext(), 8, 20, 30 );
	}

	private void evaluation(float r1, float r2, float r3, float r4, String option, String comment){
		HTTPRequest request = new HTTPRequest( getApplicationContext() );
		request.setHost( "http://conferences.dotrow.com" );
		request.setPath( "/evaluate?hash=" + SigninActivity.getHash( this.getApplicationContext() ) );
		request.setMethod( "POST" );

		request.setContent( String.format( "{ \"rateEvent\": \"%.1f\", \"rateConfe\": \"%.1f\", \"rateNetwo\": \"%.1f\", \"rateOrgan\": \"%.1f\", \"option\": \"%s\", \"comment\": \"%s\" }",
				r1, r2, r3, r4,
				option, comment ) );

		AlertDialog.Builder builder = new AlertDialog.Builder( this );

		HTTPResponse response = request.execute();
		switch( response.getResponseCode() ) {
			case 200:
				SharedPreferences settings = getSharedPreferences( "Global", 0 );
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean( "evaluated", true );
				editor.commit();
				builder.setMessage( "Gracias por darnos tu opinión." )
						.setTitle( "Evaluación" );
				builder.setCancelable( false )
						.setNeutralButton( "Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick( DialogInterface dialog, int id ) {
										finish();
									}
								} );
				break;
			default:
				builder.setMessage( "Error al evaluar el evento, intente de nuevo" )
						.setTitle( "Evaluación" );
				builder.setCancelable( false )
						.setNeutralButton( "Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick( DialogInterface dialog, int id ) {
										dialog.cancel();
									}
								} );
		}

		AlertDialog alert = builder.create();
		alert.show();
	}
}
